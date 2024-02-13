package com.facturacion.repositories;

import com.facturacion.models.Facturacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacturacionRepository extends JpaRepository<Facturacion, String> {

  Optional<Facturacion> findByCustomerFacturacion(String customer);
}
