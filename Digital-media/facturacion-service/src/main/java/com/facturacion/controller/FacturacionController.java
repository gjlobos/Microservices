package com.facturacion.controller;

import com.facturacion.models.DTO.FacturacionDTO;
import com.facturacion.models.Facturacion;
import com.facturacion.service.FacturacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facturacion")
@RequiredArgsConstructor
public class FacturacionController {

  private final FacturacionService service;

  @GetMapping("/all")
  public ResponseEntity<List<Facturacion>> getAll() {
    return ResponseEntity.ok().body(service.getAllFacturacion());
  }


  @PostMapping()
  @PreAuthorize("hasAuthority('GROUP_provider') AND hasAuthority('SCOPE_facturacion:gestion')")
  public ResponseEntity<Facturacion> saveFacturacion(@RequestBody Facturacion facturacion) {
    return ResponseEntity.ok().body(service.saveFacturacion(facturacion));
  }

  @GetMapping("/findBy")
  public ResponseEntity<Facturacion> findByCustomer(@RequestParam String customer) {
    Facturacion facturacion = service.findByCustomer(customer);
    if (facturacion != null) {
      return ResponseEntity.ok().body(facturacion);
    }
    return ResponseEntity.notFound().build();
  }

  @GetMapping("/detail/{username}")
  public ResponseEntity<FacturacionDTO> getAllFacturacionsByUserName(@RequestParam String username) {
    FacturacionDTO facturacion = service.findByUserName(username);
    if (facturacion != null) {
      return ResponseEntity.ok().body(facturacion);
    }
    return ResponseEntity.notFound().build();
  }


}
