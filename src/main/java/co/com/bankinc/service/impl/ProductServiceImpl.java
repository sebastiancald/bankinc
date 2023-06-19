package co.com.bankinc.service.impl;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.com.bankinc.entity.ProductEntity;
import co.com.bankinc.entity.ProductTypeEntity;
import co.com.bankinc.exception.BadRequestException;
import co.com.bankinc.exception.NotFoundException;
import co.com.bankinc.repository.IClientRepository;
import co.com.bankinc.repository.IProductRepository;
import co.com.bankinc.repository.IProductTypeRepository;
import co.com.bankinc.service.IProductService;
import co.com.bankinc.util.Constants;
import co.com.bankinc.util.Currency;
import co.com.bankinc.util.Status;

@Service
public class ProductServiceImpl implements IProductService {

	@Autowired
	private IProductRepository productRepository;

	@Autowired
	private IProductTypeRepository productTypeRepository;

	@Autowired
	private IClientRepository clientRepository;

	@Override
	public ProductEntity generateCardNumber(Integer productTypeId) {

		ProductEntity savedCard;
		try {
			ProductTypeEntity existingProduct = productTypeRepository.findById(productTypeId)
					.orElseThrow(() -> new NotFoundException(Constants.PRODUCT_NOT_FOUND));

			ProductEntity newCard = new ProductEntity();
			newCard.setProductType(existingProduct);
			newCard.setCardStatus(Status.INACTIVO);
			newCard.setExpirationDate(LocalDate.now().plusYears(3).toString());
			newCard.setBalance(BigDecimal.ZERO);
			newCard.setCardNumber(generateRandomNumber(Integer.parseInt(existingProduct.getProductTypeCode())));
			newCard.setCurrency(Currency.USD);

			savedCard = productRepository.save(newCard);
		} catch (NotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new BadRequestException(Constants.ERROR_GENERATING_CARD);
		}

		LocalDate expirationDate = LocalDate.parse(savedCard.getExpirationDate());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
		savedCard.setExpirationDate(expirationDate.format(formatter));

		return savedCard;
	}

	private String generateRandomNumber(int number) throws NoSuchAlgorithmException {
		String firstDigits = String.format("%06d", number);

		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");

		StringBuilder sb = new StringBuilder();
		while (sb.length() < 10) {
			int randomDigit = secureRandom.nextInt(10);
			sb.append(randomDigit);
		}

		String remainingDigits = sb.toString();

		return firstDigits + remainingDigits;
	}

	@Override
	public ResponseEntity<String> activateProduct(String cardNumber) {
		ProductEntity product = findProductByCardNumber(cardNumber);
		product.setActivationDate(LocalDateTime.now(getColombiaZoneId()));
		product.setCardStatus(Status.ACTIVO);
		productRepository.save(product);
		return ResponseEntity.ok("Tarjeta Activada");
	}

	@Override
	public ResponseEntity<String> blockProduct(String cardNumber) {
		ProductEntity activeProduct = findActiveProductByCardNumber(cardNumber);
		if (activeProduct.getCardStatus().equals(Status.BLOQUEADO)) {
			throw new BadRequestException("Tarjeta ya se encuentra Bloqueada");
		}
		activeProduct.setCardStatus(Status.BLOQUEADO);
		productRepository.save(activeProduct);
		return ResponseEntity.ok("Tarjeta Bloqueada");
	}

	@Override
	public ResponseEntity<String> rechargeBalance(String cardNumber, BigDecimal amount) {
		ProductEntity activeCard = findActiveProductByCardNumber(cardNumber);
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new BadRequestException("El valor de 'amount' debe ser mayor a cero.");
		}
		activeCard.setBalance(activeCard.getBalance().add(amount));
		productRepository.save(activeCard);
		return ResponseEntity.ok("Has recargado " + amount + " USD.");
	}

	@Override
	public BigDecimal getBalance(String cardNumber) {
		ProductEntity activeCard = findActiveProductByCardNumber(cardNumber);
		return activeCard.getBalance();
	}

	@Override
	public ResponseEntity<String> assignCardToClient(String cardNumber, String documentNumber) {
		var user = clientRepository.findByDocumentNumber(documentNumber);
		var card = productRepository.findByActiveCardNumberWithOutClient(cardNumber);
		if (user == null || card == null) {
			throw new NotFoundException(Constants.CLIENT_OR_CARD_NOT_FOUND);
		} else {
			card.setClient(user);
			card.setCardHoldername(user.getFirstName()+" "+user.getFirstSurname());
			productRepository.save(card);
		}
		var cardNumberMask = maskCardNumber(card.getCardNumber());
		return ResponseEntity.ok("Se ha asociado exitosamente el usuario " + card.getCardHoldername() + " con la tarjeta "
				+ cardNumberMask);
	}

	
	private ProductEntity findProductByCardNumber(String cardNumber) {
	   var productOptional = productRepository.findProductByCardNumber(cardNumber);
	    if (productOptional != null) {
	        return productOptional;
	    } else {
	        throw new BadRequestException(Constants.CARD_NOT_FOUND);
	    }
	}

	private ProductEntity findActiveProductByCardNumber(String cardNumber) {
		ProductEntity activeProduct = productRepository.findByCardNumber(cardNumber);
		if (activeProduct == null) {
			throw new BadRequestException(Constants.CARD_NOT_FOUND);
		}
		return activeProduct;
	}

	private ZoneId getColombiaZoneId() {
		return ZoneId.of("America/Bogota");
	}

	private String maskCardNumber(String cardNumber) {
		String maskedNumber = cardNumber.substring(0, 12).replaceAll("\\d", "*");
		maskedNumber += cardNumber.substring(12);
		return maskedNumber;
	}
}
