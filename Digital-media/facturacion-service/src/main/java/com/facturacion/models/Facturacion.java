package com.facturacion.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Facturacion {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid2")
  private String idFacturacion;

  private Date facturacionDate;

  private String customerFacturacion;

  private String productFacturacion;

  private Double totalPrice;

}
