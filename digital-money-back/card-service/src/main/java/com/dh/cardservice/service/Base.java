package com.dh.cardservice.service;

import com.dh.cardservice.exceptions.ResourceNotFoundException;

import java.util.List;

public interface Base <T> {

        T crear(T t);
        T buscar(Long id)throws ResourceNotFoundException;
        void eliminar(Long id)throws ResourceNotFoundException;
        List<T> buscarTodos();
        void actualizar(T t)throws ResourceNotFoundException;

}
