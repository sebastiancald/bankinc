package co.com.bankinc.service;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;

import co.com.bankinc.entity.ProductEntity;

public interface IProductService {

	ProductEntity generateCardNumber(Integer productTYpeId);

	ResponseEntity<String> activateProduct(String cardNumber);

	ResponseEntity<String> blockProduct(String cardNumber);

	ResponseEntity<String> rechargeBalance(String cardNumber, BigDecimal amount);

	BigDecimal getBalance(String cardNumber);

	ResponseEntity<String> assignCardToClient(String cardNumber, String documentNumber);

}
