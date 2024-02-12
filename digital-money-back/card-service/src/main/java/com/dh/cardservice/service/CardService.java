package com.dh.cardservice.service;

import com.dh.cardservice.exceptions.ResourceNotFoundException;
import com.dh.cardservice.model.Card;
import com.dh.cardservice.repository.ICardRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class CardService implements Base<Card>{

    private final ICardRepository cardRepository;

    @Override
    public Card crear(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public Card buscar(Long id) throws ResourceNotFoundException {
        Optional<Card> optionalCard = cardRepository.findById(id);
        return optionalCard.orElseThrow(() -> new ResourceNotFoundException("Tarjeta no encontrada con ID: " + id));
    }

    @Override
    public void eliminar(Long id) throws ResourceNotFoundException {
        if (!cardRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tarjeta no encontrada con ID: " + id);
        }
        cardRepository.deleteById(id);
    }

    @Override
    public List<Card> buscarTodos() {
        return cardRepository.findAll();
    }

    @Override
    public void actualizar(Card card) throws ResourceNotFoundException {
        if (!cardRepository.existsById(card.getId())) {
            throw new ResourceNotFoundException("Tarjeta no encontrada con ID: " + card.getId());
        }
        cardRepository.save(card);
    }

    public List<Card> getCardsByAccount(Long accountId){
        return cardRepository.findByAccountId(accountId);
    }
}
