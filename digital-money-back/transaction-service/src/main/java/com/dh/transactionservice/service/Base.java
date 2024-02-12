package com.dh.transactionservice.service;

import com.dh.transactionservice.exceptions.ResourceNotFoundException;

import java.util.List;

public interface Base <T> {

    T create(T t);
    T buscar(Long id) throws ResourceNotFoundException;
    void eliminar(Long id) throws ResourceNotFoundException;
    List<T> buscarTodos();
    void actualizar(T t) throws ResourceNotFoundException;

}