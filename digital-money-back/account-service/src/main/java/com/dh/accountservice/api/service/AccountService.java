package com.dh.accountservice.api.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dh.accountservice.api.client.CardServiceClient;
import com.dh.accountservice.api.client.TransactionServiceClient;
import com.dh.accountservice.api.client.UserServiceClient;
import com.dh.accountservice.domain.dto.request.AccountRequestUpdateAccountInfoDto;
import com.dh.accountservice.domain.dto.request.TransferenceRequestDto;
import com.dh.accountservice.domain.dto.request.TransferenceResponseDto;
import com.dh.accountservice.domain.dto.response.AccountByTransferenceDto;
import com.dh.accountservice.domain.dto.response.AccountResponseDto;
import com.dh.accountservice.domain.dto.response.DestinationsResponseDto;
import com.dh.accountservice.domain.dto.response.TransactionResponseDto;
import com.dh.accountservice.domain.mapper.AccountMapper;
import com.dh.accountservice.domain.model.Account;
import com.dh.accountservice.domain.model.Card;
import com.dh.accountservice.domain.model.Transaction;
import com.dh.accountservice.domain.model.Transference;
import com.dh.accountservice.exceptions.ResourceNotFoundException;
import com.dh.accountservice.domain.repository.IAccountRepository;
import com.dh.accountservice.shared.handler.exception.ForbiddenException;
import com.dh.accountservice.shared.handler.exception.GoneException;
import com.dh.accountservice.shared.handler.exception.ResourceNotContent;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.dh.accountservice.domain.enums.EMessageCode.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@AllArgsConstructor
public class AccountService implements IAccountService {

    private static final int CODE_LENGTH = 22;
    private static final String WORDS_FILE_PATH = "listado-palabras.txt";
    private static final String OUTPUT_PATTERN = "dd/MM/yyyy";

    private final IAccountRepository accountRepository;
    private final AccountMapper accountMapper;

    private final UserServiceClient userServiceClient;
    private final CardServiceClient cardServiceClient;
    private final TransactionServiceClient transactionServiceClient;

    private final MessageSource messageSource;
    private final HttpServletRequest request;

    @Override
    public Account findById(Long id) throws ResourceNotFoundException {
        return accountRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(ACOUNT_NOT_FOUND_BY_ID.name(),
                        new Object[] { id }, Locale.getDefault())));
    }

    public void eliminar(Long id) throws ResourceNotFoundException {
        Account account = findById(id);
        accountRepository.delete(account);
    }

    public List<Account> buscarTodos() {
        return accountRepository.findAll();
    }

    public Account actualizar(Account account) throws ResourceNotFoundException {
        findById(account.getId()); // Verificar si la cuenta existe antes de actualizar
        accountRepository.save(account);
        return account;
    }

    @Override
    public Account createAccount(String userId) {
        String cvu = generateRandomCode();
        String alias = generateRandomAlias();
        Account account = new Account();
        account.setCvu(cvu);
        account.setAlias(alias);
        account.setAvailableAmount(0.0);
        account.setUserId(userId);
        return accountRepository.save(account);
    }

    @Override
    public Card createCard(Card card, Long accountId) throws ResourceNotFoundException {
        String cardNumber = generateRandomNumber(15);
        LocalDate expirationDate = LocalDate.now().plusYears(4);
        card.setAccountId(accountId);
        if(card.getProvider().equals("VISA")){
            card.setCardNumber("4" + cardNumber);
            String securityCode = generateRandomNumber(3);
            card.setSecurityCode(securityCode);
        } else {
            card.setCardNumber("5" + cardNumber);
            String securityCode = generateRandomNumber(4);
            card.setSecurityCode(securityCode);
        }
        card.setExpirationDate(expirationDate.toString());
        card.setName(userServiceClient.findNameById(findById(accountId).getUserId()).toUpperCase());
        cardServiceClient.crearTarjeta(card);
        return card;
    }

    @Override
    public Card registerCard(Card card, Long accountId) throws ResourceNotFoundException {
        card.setAccountId(accountId);
        cardServiceClient.crearTarjeta(card);
        return card;
    }

    @Override
    public List<Card> getCardsByAccountId(Long accountId) throws ResourceNotFoundException{
        String token = extractTokenFromHeader(request.getHeader("Authorization"));

        DecodedJWT jwt = JWT.decode(token);
        String s = jwt.getSubject();

        Account a = accountRepository.findByAccountByUserId(s).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(ACCOUNT_NOT_EXIST_BY_USER_ID.name(),
                        new Object[] { s }, Locale.getDefault())));

        if(!a.getId().equals(accountId)){
            throw new ForbiddenException(messageSource.getMessage(NOT_ACCES.name(),
                    new Object[] { }, Locale.getDefault()));
        }

        List<Card> cards = cardServiceClient.getCardsByAccount(accountId).getBody();

        if(cards.isEmpty()) throw new ResourceNotContent();

        return cards;
    }

    public List<Transaction> getFiveTransactionsByAccount(Long accountId) throws ResourceNotFoundException{
        return transactionServiceClient.getFiveTransactionsByAccount(accountId).getBody();
    }

    public Transaction getTransactionByIdByAccount(Long accountId, Long id) throws ResourceNotFoundException{
        Transaction transaction = transactionServiceClient.getTransactionById(id).getBody();
        if(transaction.getAccountId().toString().equals(accountId.toString())){
            return transaction;
        } else {
            throw new ResourceNotFoundException("Transaction not found");
        }
    }

    public void deleteCard(Long cardId) throws ResourceNotFoundException{
        cardServiceClient.eliminarTarjeta(cardId);
    }

    public Optional<AccountResponseDto> getAccountByUserId(String userId){
        Optional<Account> account = accountRepository.findByAccountByUserId(userId);

        if(account.isEmpty()){
            return Optional.empty();
        }

        return Optional.of(accountMapper.accountToAccountResponseDto(account.get()));
    }

    @Override
    public Optional<AccountResponseDto> update(AccountRequestUpdateAccountInfoDto accountRequestUpdateAccountInfoDto, String userId){
        Optional<Account> account = accountRepository.findByAccountByUserId(userId);

        String alias = accountRequestUpdateAccountInfoDto.getAlias();

        if(findAllAlias().contains(alias)){
            return Optional.empty();
        }

        account.get().setAlias(accountRequestUpdateAccountInfoDto.getAlias());
        account = Optional.of(accountRepository.save(account.get()));

        return Optional.of(accountMapper.accountToAccountResponseDto(account.get()));
    }

    public Optional<AccountByTransferenceDto> accountTransferenceByUserId(String userId){
        Optional<Account> account = accountRepository.findByAccountByUserId(userId);

        if(account.isEmpty()){
            return Optional.empty();
        }

        return Optional.of(accountMapper.accountToAccountByTransferenceDto(account.get()));
    }

    public Optional<AccountByTransferenceDto> accountTransferenceByCvu(String cvu){
        Optional<Account> account = accountRepository.findByAccountByCvu(cvu);

        if(account.isEmpty()){
            return Optional.empty();
        }

        return Optional.of(accountMapper.accountToAccountByTransferenceDto(account.get()));
    }

    private List<String> findAllAlias(){
        return accountRepository.findAllAlias();
    }

    public List<TransactionResponseDto> findTransactionsByAccountId(Long accountId) throws ResourceNotFoundException {
        String token = extractTokenFromHeader(request.getHeader("Authorization"));

        DecodedJWT jwt = JWT.decode(token);
        String s = jwt.getSubject();

        Account a = accountRepository.findByAccountByUserId(s).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(ACCOUNT_NOT_EXIST_BY_USER_ID.name(),
                        new Object[] { s }, Locale.getDefault())));

        if(!a.getId().equals(accountId)){
            throw new ForbiddenException(messageSource.getMessage(NOT_ACCES.name(),
                    new Object[] { }, Locale.getDefault()));
        }

        List<TransactionResponseDto> transactions = transactionServiceClient.findTransactionsByAccountId(accountId);

        if(transactions.isEmpty()) {
            throw new ResourceNotContent();
        }

        return transactions;
    }

    public List<DestinationsResponseDto> findDestinations(Long accountId) throws ResourceNotFoundException {
        String token = extractTokenFromHeader(request.getHeader("Authorization"));

        DecodedJWT jwt = JWT.decode(token);
        String s = jwt.getSubject();

        Account a = accountRepository.findByAccountByUserId(s).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(ACCOUNT_NOT_EXIST_BY_USER_ID.name(),
                        new Object[] { s }, Locale.getDefault())));

        if(!a.getId().equals(accountId)){
            throw new ForbiddenException(messageSource.getMessage(NOT_ACCES.name(),
                    new Object[] { }, Locale.getDefault()));
        }

        List<String> cvuDestination = transactionServiceClient.findDestinations(accountId);

        List<DestinationsResponseDto> destinationsResponseDtos = new ArrayList<>();

        for (int i = 0; i < cvuDestination.size(); i++){
            Optional<Account> account = accountRepository.findByAccountByCvu(cvuDestination.get(i));
            DestinationsResponseDto destinationsResponseDto = new DestinationsResponseDto();
            if(account.isPresent()){
                destinationsResponseDto.setCvu(account.get().getCvu());
                destinationsResponseDto.setAlias(account.get().getAlias());
                String name = userServiceClient.findNameById(account.get().getUserId());
                destinationsResponseDto.setName(name);
            } else {
                destinationsResponseDto.setCvu(cvuDestination.get(i));
            }
            destinationsResponseDtos.add(destinationsResponseDto);
        }

        if(cvuDestination.isEmpty()) {
            throw new ResourceNotContent();
        }

        return destinationsResponseDtos;
    }

    public TransferenceResponseDto sendMoney(Long accountId, TransferenceRequestDto transferenceRequestDto) throws ResourceNotFoundException {
        String token = extractTokenFromHeader(request.getHeader("Authorization"));

        DecodedJWT jwt = JWT.decode(token);
        String s = jwt.getSubject();

        Account a = accountRepository.findById(accountId).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(NOT_ACCES.name(),
                        new Object[] { }, Locale.getDefault())));

        if(!a.getUserId().equals(s) || !a.getCvu().equals(transferenceRequestDto.getOrigin())){
            throw new ForbiddenException(messageSource.getMessage(NOT_ACCES.name(),
                    new Object[] { }, Locale.getDefault()));
        }

        AccountByTransferenceDto accountOrigin = accountMapper.accountToAccountByTransferenceDto(a);


        if(accountOrigin.getAvailableAmount() < transferenceRequestDto.getAmount()){
            throw new GoneException(messageSource.getMessage(INSUFFICIENT_FUNDS.name(),
                    new Object[] { }, Locale.getDefault()));
        }

        Optional<AccountByTransferenceDto> accountDestination = accountTransferenceByCvu(transferenceRequestDto.getDestination());

        LocalDate currentDate = LocalDate.now();
        String today = currentDate.format(DateTimeFormatter.ofPattern(OUTPUT_PATTERN));

        transferenceRequestDto.setDated(today);

        if(accountDestination.isPresent()){
            updateAmount(accountDestination.get().getId(), transferenceRequestDto.getAmount(), "INCOME");

            Transaction expense = new Transaction();
            expense.setAccountId(Math.toIntExact(accountId));
            expense.setAmount(transferenceRequestDto.getAmount());
            expense.setDescription("Envío de dinero haciá la cuenta " + transferenceRequestDto.getDestination());
            expense.setDated(today);
            expense.setType("EXPENSE");
            expense.setDestination(transferenceRequestDto.getDestination());
            expense.setOrigin(transferenceRequestDto.getOrigin());
            transactionServiceClient.create(expense);
        }

        updateAmount(accountOrigin.getId(), transferenceRequestDto.getAmount(), "EXPENSE");

        Transaction income = new Transaction();
        income.setAccountId(Math.toIntExact(accountId));
        income.setAmount(transferenceRequestDto.getAmount());
        income.setDescription("Ingreso de dinero desde la cuenta " + transferenceRequestDto.getOrigin());
        income.setDated(today);
        income.setType("INCOME");
        income.setDestination(transferenceRequestDto.getDestination());
        income.setOrigin(transferenceRequestDto.getOrigin());
        transactionServiceClient.create(income);

        return accountMapper.transferenceRequestDtoToTransferenceResponseDto(transferenceRequestDto);
    }

    public void updateAmount(Long accountId, Double amount, String type){
        Optional<Account> account = accountRepository.findById(accountId);
        if(type == "INCOME"){
            account.get().setAvailableAmount(account.get().getAvailableAmount() + amount);
        } else {
            account.get().setAvailableAmount(account.get().getAvailableAmount() - amount);
        }
        accountRepository.save(account.get());
    }

    public ResponseEntity<String> transferir(Long accountId, Transference transference) throws ResourceNotFoundException {
        ResponseEntity<List<Card>> response = cardServiceClient.getCardsByAccount(accountId);
        List<Card> cards = response.getBody();

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

        Account accountResponse = findById(accountId);
        double montoActual = accountResponse.getAvailableAmount();

        double montoNuevo = montoActual + transference.getAmount();

        accountResponse.setAvailableAmount(montoNuevo);
        updateAmount(accountId, transference.getAmount(), "INCOME");

        Transaction transaction = new Transaction();
        transaction.setAccountId(Math.toIntExact(accountId));
        transaction.setAmount(transference.getAmount());
        transaction.setDescription("Ingreso de dinero desde la tarjeta: " + transference.getCardNumber());
        transaction.setDated(String.valueOf(new Date()));

        LocalDate currentDate = LocalDate.now();
        String today = currentDate.format(DateTimeFormatter.ofPattern(OUTPUT_PATTERN));

        Transaction expense = new Transaction();
        expense.setAccountId(Math.toIntExact(accountId));
        expense.setAmount(transference.getAmount());
        expense.setDescription("Ingreso de dinero desde la tarjeta " + transference.getCardNumber());
        expense.setDated(today);
        expense.setType("INCOME");
        expense.setDestination(accountResponse.getCvu());
        expense.setOrigin("xxxx xxxx xxxx xxxx");
        transactionServiceClient.create(expense);

        return new ResponseEntity<>("Transacción exitosa", HttpStatus.OK);
    }


    //metodos extras

    private String extractTokenFromHeader(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7).trim();
        }
        return null;
    }

    private String generateRandomCode() {
        // Lógica para generar un código de 22 dígitos aleatorios
        String allowedChars = "0123456789";
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(allowedChars.length());
            char randomChar = allowedChars.charAt(index);
            codeBuilder.append(randomChar);
        }
        return codeBuilder.toString();
    }

    private String generateRandomNumber(Integer n) {
        // Lógica para generar un código de 22 dígitos aleatorios
        String allowedChars = "0123456789";
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = random.nextInt(allowedChars.length());
            char randomChar = allowedChars.charAt(index);
            codeBuilder.append(randomChar);
        }
        return codeBuilder.toString();
    }

    private String generateRandomAlias() {
        // Lógica para generar un alias formado por 3 palabras aleatorias
        List<String> words = readWordsFromFile();
        Random random = new Random();
        StringBuilder aliasBuilder = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            int index = random.nextInt(words.size());
            String word = words.get(index).toUpperCase();
            aliasBuilder.append(word);
            if (i < 2) {
                aliasBuilder.append(".");
            }
        }
        return aliasBuilder.toString();
    }

    private List<String> readWordsFromFile() {
        List<String> words = new ArrayList<>();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(WORDS_FILE_PATH);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    public ResponseEntity<?> generarComprobante(Long accountID, Long transactionID) throws ResourceNotFoundException {
    //    Account cuenta = accountRepository.findById(accountID)
    //            .orElse(null);
    //    if (cuenta != null) {
          if (true) {
            Transaction transaction = transactionServiceClient.getTransactionById(transactionID).getBody();
            if (transaction != null) {
                try {
                    ResponseEntity<?> respuesta = transactionServiceClient.generatePDF(transactionID);
                    System.out.println(respuesta);
                    return respuesta;
                } catch (Exception e) {
                    // Manejar el error al generar el comprobante
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
        }
        return ResponseEntity.notFound().build();
    }
}