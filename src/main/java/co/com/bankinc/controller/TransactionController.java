package co.com.bankinc.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.io.BigDecimalParser;

import co.com.bankinc.dto.TransactionResponseDto;
import co.com.bankinc.entity.TransactionEntity;
import co.com.bankinc.exception.BadRequestException;
import co.com.bankinc.service.ITransactionService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/transaction")
public class TransactionController {
	
	@Autowired
	private ITransactionService transactionService;

	@PostMapping("/purchase")
	public ResponseEntity<TransactionResponseDto> makePurchase(@RequestBody Map<String, String> request){
		var cardNumber = request.get("cardNumber");
		var price = BigDecimal.ZERO;
		try {
			cardNumber = request.get("cardNumber");
			price = BigDecimalParser.parse(request.get("price"));
		} catch (NumberFormatException e) {
			throw new BadRequestException("El campo 'amount' debe ser un valor numérico.");
		}
		return transactionService.makePurchase(cardNumber, price);
	}
	
	@GetMapping()
	public TransactionEntity getTransactionById(@RequestParam Integer transactionid) {
		return transactionService.getTransaction(transactionid);
	}
	
	@PostMapping("/anulation")
	public ResponseEntity<String> cancelTransaction(@RequestBody Map<String, String> request) {
		var card = request.get("cardNumber");
		var transaction = Integer.parseInt(request.get("transactionId"));
		return transactionService.cancelTransaction(card, transaction);
	}
}
