package com.dh.transactionservice.model.mapper;

import com.dh.transactionservice.model.Transaction;
import com.dh.transactionservice.model.dto.response.TransactionResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    List<TransactionResponseDto> transactionListToTransactionResponseDtoList(List<Transaction> transactions);

}
//public abstract class TransactionMapper {

//    public abstract List<TransactionResponseDto> transactionListToTransactionResponseDtoList(List<Transaction> transactions);

//}