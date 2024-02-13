package com.facturacion.service;

import com.facturacion.models.DTO.FacturacionDTO;
import com.facturacion.models.DTO.UserDTO;
import com.facturacion.models.Facturacion;
import com.facturacion.repositories.FacturacionRepository;
import com.facturacion.repositories.FeignUsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacturacionService {

  private final FacturacionRepository repository;
  private final FeignUsersRepository usersRepository;

  public List<Facturacion> getAllFacturacion() {
    return repository.findAll();
  }

  public Facturacion saveFacturacion(Facturacion facturacion) {
    return repository.save(facturacion);
  }

  public Facturacion findByCustomer(String customer) {
    return repository.findByCustomerFacturacion(customer).orElse(null);
  }

  public FacturacionDTO findByUserName(String username) {
    Facturacion facturacion= repository.findByCustomerFacturacion(username).orElse(null);
    ResponseEntity<UserDTO> userDTO= usersRepository.findByUserName(username);
    FacturacionDTO facturacionDTO = null;
    if (facturacion != null) {
      facturacionDTO.setIdFacturacion(facturacion.getIdFacturacion());
      facturacionDTO.setFacturacionDate(facturacion.getFacturacionDate());
      facturacionDTO.setTotalPrice(facturacion.getTotalPrice());
      facturacionDTO.setUserDTO(userDTO.getBody());

    }
    return facturacionDTO;
  }

}
