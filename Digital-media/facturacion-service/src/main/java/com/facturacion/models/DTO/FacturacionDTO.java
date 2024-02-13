package com.facturacion.models.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class FacturacionDTO {

  private String idFacturacion;

  private Date facturacionDate;

  private Double totalPrice;

  private UserDTO userDTO;

}
