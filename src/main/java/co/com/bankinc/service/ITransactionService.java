package co.com.bankinc.service;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;

import co.com.bankinc.dto.TransactionResponseDto;
import co.com.bankinc.entity.TransactionEntity;

public interface ITransactionService {

	ResponseEntity<TransactionResponseDto> makePurchase(String cardId, BigDecimal price);

	TransactionEntity getTransaction(Integer transactionId);

	ResponseEntity<String> cancelTransaction(String cardId, Integer transactionId);

}
