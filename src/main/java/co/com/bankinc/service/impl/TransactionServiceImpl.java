package co.com.bankinc.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.com.bankinc.dto.TransactionResponseDto;
import co.com.bankinc.entity.ProductEntity;
import co.com.bankinc.entity.TransactionEntity;
import co.com.bankinc.exception.BadRequestException;
import co.com.bankinc.exception.NotFoundException;
import co.com.bankinc.repository.IProductRepository;
import co.com.bankinc.repository.ITransactionRepository;
import co.com.bankinc.service.ITransactionService;
import co.com.bankinc.util.Constants;
import co.com.bankinc.util.Status;
import co.com.bankinc.util.TransactionStatus;

@Service
public class TransactionServiceImpl implements ITransactionService {

	@Autowired
	private ITransactionRepository transactionRepository;
	@Autowired
	private IProductRepository productRepository;

	@Override
	public ResponseEntity<TransactionResponseDto> makePurchase(String cardId, BigDecimal price) {

		var card = validatePurchase(cardId, price);

		var transaction = createTransaction(card, price);

		updateCardAndSaveTransaction(card, transaction);

		var maskedCardNumber = maskCardNumber(card.getCardNumber());

		var transactionResponse = createTransactionResponse(transaction, maskedCardNumber);

		return ResponseEntity.ok(transactionResponse);
	}

	private ProductEntity validatePurchase(String cardId, BigDecimal price) {
		var card = productRepository.findByActiveCardNumber(cardId);

		if (card == null) {
			throw new BadRequestException(Constants.ERROR_CARD_NOT_FOUND);
		}

		if (card.getCardStatus().equals(Status.INACTIVO)) {
			throw new BadRequestException(Constants.ERROR_INACTIVE_CARD);
		}

		if (LocalDate.now().isAfter(LocalDate.parse(card.getExpirationDate()))) {
			throw new BadRequestException(Constants.ERROR_EXPIRED_CARD);
		}

		if (card.getBalance().compareTo(price) < 0) {
			throw new BadRequestException(Constants.ERROR_INSUFFICIENT_FUNDS);
		}

		if (price.compareTo(BigDecimal.ZERO) <= 0) {
			throw new BadRequestException(Constants.ERROR_INVALID_PRICE);
		}
		
		return card;
	}

	private TransactionEntity createTransaction(ProductEntity card, BigDecimal price) {
		var transaction = new TransactionEntity();
		transaction.setProduct(card);
		transaction.setPurchaseAmount(price);
		var colombiaZone = ZoneId.of("America/Bogota");
		transaction.setPurchaseDate(LocalDateTime.now(colombiaZone));
		transaction.setTransactionStatus(TransactionStatus.APROBADA);
		return transaction;
	}

	private void updateCardAndSaveTransaction(ProductEntity card, TransactionEntity transaction) {
		transactionRepository.save(transaction);
		card.setBalance(card.getBalance().subtract(transaction.getPurchaseAmount()));
		productRepository.save(card);
	}

	private String maskCardNumber(String cardNumber) {
		String maskedNumber = cardNumber.substring(0, 12).replaceAll("\\d", "*");
		maskedNumber += cardNumber.substring(12);
		return maskedNumber;
	}

	private TransactionResponseDto createTransactionResponse(TransactionEntity response, String maskedCardNumber) {
		var transactionResponse = new TransactionResponseDto();
			transactionResponse.setCardNumber(maskedCardNumber);
			transactionResponse.setPurchaseAmount(response.getPurchaseAmount());
			transactionResponse.setTransactionEstatus(response.getTransactionStatus());
			transactionResponse.setTransactionId(response.getTransactionId());
		return transactionResponse;
	}

	@Override
	public ResponseEntity<String> cancelTransaction(String cardId, Integer transactionId) {
		var transaction = transactionRepository.findByTransactionIdAndCardNumber(cardId, transactionId);
		if (transaction == null) {
			throw new NotFoundException("NOT_FOUND_INFO");
		}
		
		var card = productRepository.findByActiveCardNumber(cardId);
		var purchaseDateIsPast24Hours = transaction.getPurchaseDate().plusHours(24).isBefore(LocalDateTime.now());


		if (transaction.getTransactionStatus().equals(TransactionStatus.ANULADA)) {
			throw new BadRequestException("TRANSACTION_IS_ALREADY_CANCELLED");
		}

		if (purchaseDateIsPast24Hours) {
			throw new BadRequestException("TRANSACTION_CANT_GET_CANCEL_AFTER_24_HOURS");
		}

		transaction.setTransactionStatus(TransactionStatus.ANULADA);
		transactionRepository.save(transaction);

		card.setBalance(card.getBalance().add(transaction.getPurchaseAmount()));
		productRepository.save(card);

		return ResponseEntity.ok("La transacción " + transactionId + " ha sido anulada.");
	}

	@Override
	public TransactionEntity getTransaction(Integer transactionId) {
		return transactionRepository.findById(transactionId)
				.orElseThrow(() -> new NotFoundException("NOT_FOUND_INFO"));
	}

}
