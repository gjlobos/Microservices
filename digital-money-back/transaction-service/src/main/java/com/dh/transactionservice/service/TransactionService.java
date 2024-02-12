package com.dh.transactionservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dh.transactionservice.api.client.AccountClient;
import com.dh.transactionservice.api.client.CardClient;
import com.dh.transactionservice.exceptions.ResourceNotFoundException;
import com.dh.transactionservice.model.Account;
import com.dh.transactionservice.model.Card;
import com.dh.transactionservice.model.dto.request.TransferenceRequestDto;
import com.dh.transactionservice.model.dto.response.*;
import com.dh.transactionservice.model.mapper.TransactionMapper;
import com.dh.transactionservice.repository.ITransactionRepository;
import com.dh.transactionservice.model.Transaction;
import com.dh.transactionservice.shared.handler.ErrorDetails;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyComponentPathImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import static com.dh.transactionservice.shared.handler.ResponseBuilder.*;
import static org.springframework.http.HttpStatus.*;


@Service
public class TransactionService implements Base<Transaction> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private AccountClient accountClient;

    @Autowired
    private CardClient cardClient;

    private final ITransactionRepository transactionRepository;

    private static int counter = 1;


    public TransactionService(ITransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction create(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction buscar(Long id) throws ResourceNotFoundException {
        Optional<Transaction> optionalCard = transactionRepository.findById(id);
        return optionalCard.orElseThrow(() -> new ResourceNotFoundException("Transaccion no encontrada con ID: " + id));
    }

    @Override
    public void eliminar(Long id) throws ResourceNotFoundException {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaccion no encontrada con ID: " + id);
        }
        transactionRepository.deleteById(id);
        Transaction transaction = buscar(id);
        transactionRepository.delete(transaction);
    }

    @Override
    public List<Transaction> buscarTodos() {
        return transactionRepository.findAll();
    }

    @Override
    public void actualizar(Transaction transaction) throws ResourceNotFoundException {
        if (!transactionRepository.existsById(transaction.getId())) {
            throw new ResourceNotFoundException("Transaccion no encontrada con ID: " + transaction.getId());
        }
        transactionRepository.save(transaction);
    }

    public List<Transaction> getFiveTransactionsByAccount(Long accountId) {
        return transactionRepository.findLastFiveTransactionsByAccountId(accountId);
    }

    public List<Transaction> getLastFiveTransactions() {
        return transactionRepository.findLastFiveTransactions();
    }
    
    public ResponseEntity<String> transferir(Long accountId, Transference transference) throws ResourceNotFoundException {
        // Obtiene el listado de tarjetas disponibles
        ResponseEntity<List<Card>> response = cardClient.getCardsByAccount(accountId);
        List<Card> cards = response.getBody();

        // Verifica si la tarjeta de la transferencia está disponible
        boolean tarjetaDisponible = false;
        for (Card card : cards) {
            if (card.getCardNumber().equals(transference.getCardNumber())) {
                tarjetaDisponible = true;
                break;
            }
        }

        if (!tarjetaDisponible) {
            return new ResponseEntity<>("La tarjeta no está disponible", HttpStatus.BAD_REQUEST);
        }

        // Obtiene el monto actual de la cuenta
        ResponseEntity<Account> accountResponse = accountClient.getAccount(accountId);
        Account account = accountResponse.getBody();
        double montoActual = account.getAvailableAmount();

        // Suma el monto de la transferencia al monto actual
        double montoNuevo = montoActual + transference.getAmount();

        // Actualiza el monto de la cuenta con el nuevo valor
        account.setAvailableAmount(montoNuevo);
        accountClient.actualizarCuenta(accountId, account);

        // Crea una nueva transacción
        Transaction transaction = new Transaction();
        transaction.setAccountId(Math.toIntExact(accountId));
        transaction.setAmount(transference.getAmount());
        transaction.setDescription("Ingreso de dinero desde la tarjeta: " + transference.getCardNumber());
        transaction.setDated(String.valueOf(new Date()));

        // Guarda la transacción
        transactionRepository.save(transaction);

        return new ResponseEntity<>("Transacción exitosa", HttpStatus.OK);
    }

    public TransferenceResponseDto sendMoney(Long accountId, TransferenceRequestDto transferenceRequestDto) throws ResourceNotFoundException {
        String token = extractTokenFromHeader(request.getHeader("Authorization"));
        if (token == null) {
            throw new IllegalArgumentException("Token not provided in the Authorization header.");
        }

        DecodedJWT jwt = JWT.decode(token);
        String s = jwt.getSubject();

        AccountByTransferenceDto accountOrigin = accountClient.accountTransferenceByUserId(s).get();

        System.out.println(accountOrigin);

        AccountByTransferenceDto accountDestination = accountClient.accountTransferenceByCvu(transferenceRequestDto.getDestination()).get();

        System.out.println(accountDestination);

        // Actualiza el monto de la cuenta con el nuevo valor


        // Crea una nueva transacción
        Transaction income = new Transaction();
        income.setAccountId(Math.toIntExact(accountId));
        income.setAmount(transferenceRequestDto.getAmount());
        income.setDescription("Ingreso de dinero desde la tarjeta: ");
        income.setDated(String.valueOf(new Date()));
        income.setType("INCOME");
        transactionRepository.save(income);

        //Account accountOrigin = ;
        //Account accountDestination = ;

        return new TransferenceResponseDto();
    }

    public List<TransactionResponseDto> findTransactionsByAccountId(Long accountId){
        List<TransactionResponseDto> transactions =  transactionMapper.transactionListToTransactionResponseDtoList(transactionRepository.findTransactionsByAccountId(accountId));
        return transactions;
    }

    public List<String> findDestinations(Long accountId){
        List<String> cvuDestinations =  transactionRepository.findDestinations(accountId);

        //List<String> destinations = null;
        //for(Transaction destination : cvuDestinations){
        //    destinations.add(destination.getDestination());
        //}
        return cvuDestinations;
    }

    private String extractTokenFromHeader(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7).trim();
        }
        return null;
    }

    public void generatePDF(Long transactionId) throws FileNotFoundException, DocumentException, ResourceNotFoundException {
        // Busca la transacción correspondiente al ID proporcionado en la base de datos
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        // Define la ruta y el nombre del archivo PDF
        String fileName = String.format("facturas/Factura_%d.pdf", counter++);

        // Crea un nuevo documento PDF y lo asocia con el archivo a guardar
        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);

            // Abre el documento PDF para agregar contenido
            document.open();
            // Agrega un nuevo párrafo al documento con los detalles de la transacción
            Paragraph header = new Paragraph("DIGITAL MONEY", new Font(Font.FontFamily.HELVETICA, 30, Font.BOLD));
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            document.add(new Paragraph("\n\n\n"));
            document.add(new Paragraph("\n\n\n"));
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 18);

            // Agrega detalles de la factura
            Paragraph number = new Paragraph("Nro de transacción: " + transactionId, normalFont);
                    number.setAlignment(Element.ALIGN_CENTER);
                    document.add(number);
            Paragraph date = new Paragraph("Fecha: " + transaction.getDated(), normalFont);
                    date.setAlignment(Element.ALIGN_CENTER);
                    document.add(date);
            Paragraph origen = new Paragraph("Origen: " + transaction.getOrigin(), normalFont);
                    origen.setAlignment(Element.ALIGN_CENTER);
                    document.add(origen);
            Paragraph destino = new Paragraph("Destino: " + transaction.getDestination(), normalFont);
                    destino.setAlignment(Element.ALIGN_CENTER);
                    document.add(destino);
            Paragraph monto = new Paragraph("Monto: " + transaction.getAmount(), normalFont);
                    monto.setAlignment(Element.ALIGN_CENTER);
                    document.add(monto);


            // Cierra el documento PDF
            document.close();

            System.out.println("Archivo PDF generado en: " + fileName);
        } catch (FileNotFoundException e) {
            System.err.println("No se pudo crear el archivo PDF: " + e.getMessage());
            throw e;
        } catch (DocumentException e) {
            System.err.println("Error al generar el archivo PDF: " + e.getMessage());
            throw e;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
