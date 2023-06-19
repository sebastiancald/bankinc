package co.com.bankinc.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.io.BigDecimalParser;

import co.com.bankinc.entity.ProductEntity;
import co.com.bankinc.exception.BadRequestException;
import co.com.bankinc.service.IProductService;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/card")
@Slf4j
public class ProductController {
	
	@Autowired
	private IProductService productService;
	
	@GetMapping("/productTypeId/number")
	public ProductEntity generateCard(@RequestParam Integer productTypeId) {
		log.info("Ingresa a: ", Thread.currentThread().getStackTrace()[1].getMethodName());
		return productService.generateCardNumber(productTypeId);
	}

	@PostMapping("/enroll")
	public ResponseEntity<String> activateCard(@RequestBody Map<String, String> request){
		log.info("Ingresa a: ", Thread.currentThread().getStackTrace()[1].getMethodName());
		String cardNumber = request.get("cardNumber");
		return productService.activateProduct(cardNumber);
	}
	
	@DeleteMapping()
	public ResponseEntity<String> blockingCard(@RequestParam String cardNumber){
		log.info("Ingresa a: ", Thread.currentThread().getStackTrace()[1].getMethodName());
		return productService.blockProduct(cardNumber);
	}
	
	@PostMapping("/balance")
	public ResponseEntity<String> rechargeBalance(@RequestBody Map<String, String> request){
		var card = "";
		var amount = BigDecimal.ZERO;
	try {
		card = request.get("cardNumber");
		amount = BigDecimalParser.parse(request.get("amount"));
	} catch (NumberFormatException e) {
		throw new BadRequestException("El campo 'amount' debe ser un valor numérico.");
	}
		return productService.rechargeBalance(card, amount);
		
	}
	
	@GetMapping("/balance")
	public BigDecimal getBalanceCard(@RequestParam String cardNumber){
		log.info("Ingresa a: ", Thread.currentThread().getStackTrace()[1].getMethodName());
		return productService.getBalance(cardNumber);
	}
	
	@PostMapping("/assingClient")
	public ResponseEntity<String> assignCardToClient(@RequestBody Map<String, String> request){
		var card = request.get("cardNumber");
		var client = request.get("documentNumber");
		return productService.assignCardToClient(card, client);
	}
}
