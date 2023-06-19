package co.com.bankinc.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import co.com.bankinc.dto.TransactionResponseDto;
import co.com.bankinc.entity.ClientEntity;
import co.com.bankinc.entity.ProductEntity;
import co.com.bankinc.entity.ProductTypeEntity;
import co.com.bankinc.entity.TransactionEntity;
import co.com.bankinc.exception.BadRequestException;
import co.com.bankinc.exception.NotFoundException;
import co.com.bankinc.repository.IProductRepository;
import co.com.bankinc.repository.ITransactionRepository;
import co.com.bankinc.util.Status;
import co.com.bankinc.util.TransactionStatus;

class TransactionServiceImplTest {

	@Mock
	private ITransactionRepository transactionRepository;
	@Mock
	private IProductRepository productRepository;
	
	@InjectMocks
	private TransactionServiceImpl serviceImpl;
	
	private ProductEntity  productEntity;
	
	private TransactionEntity transactionEntity;
	
	private TransactionResponseDto responseDto;
	
	private ProductTypeEntity productTypeEntity;
	
	private ClientEntity user;
	
	BigDecimal price = BigDecimal.valueOf(50);
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		
		user = new ClientEntity();
		user.setClientId(4);
		user.setDocumentNumber("1234");
		user.setFirstName("paco");
		user.setFirstSurname("rabane");
		
		productTypeEntity = new ProductTypeEntity();
		productTypeEntity.setProductTypeId(1);
		productTypeEntity.setProductName("TC");
		productTypeEntity.setProductTypeCode("1234569999");
		
		productEntity = new ProductEntity();
		productEntity.setProductId(1);
		productEntity.setCardNumber("1234567890123456");
		productEntity.setBalance(BigDecimal.valueOf(100));
		productEntity.setProductType(productTypeEntity);
		productEntity.setExpirationDate("2026-06-18");
		productEntity.setCardStatus(Status.ACTIVO);
		productEntity.setClient(user);
		
		
		transactionEntity = new TransactionEntity();
		transactionEntity.setTransactionId(3);
		transactionEntity.setProduct(productEntity);
		transactionEntity.setPurchaseAmount(BigDecimal.valueOf(50));
		transactionEntity.setPurchaseDate(LocalDateTime.now());
		transactionEntity.setTransactionStatus(TransactionStatus.APROBADA);
	}

	@Test
	void testMakePurchase() {
		responseDto = new TransactionResponseDto();
		responseDto.setTransactionId(null);
		responseDto.setCardNumber("************3456");
		responseDto.setPurchaseAmount(BigDecimal.valueOf(50));
		responseDto.setTransactionEstatus(TransactionStatus.APROBADA);
		when(productRepository.findByActiveCardNumber(any())).thenReturn(productEntity);
		var expected = ResponseEntity.ok(responseDto);
		var result = serviceImpl.makePurchase(productEntity.getCardNumber(), responseDto.getPurchaseAmount());
		assertEquals(expected, result);
	}
	
	@Test
	void testMakePurchaseWhenCardIsNull() {
		when(productRepository.findByActiveCardNumber(any())).thenReturn(null);
		assertThrows(BadRequestException.class, ()-> serviceImpl.makePurchase("847293487", price));
		
	}
	
	@Test
	void testMakePurchaseWhenStatusIsInactive() {

		productEntity.setCardStatus(Status.INACTIVO);
		when(productRepository.findByActiveCardNumber(any())).thenReturn(productEntity);
		assertThrows(BadRequestException.class, ()-> serviceImpl.makePurchase("723847293487", price));
	}
	
	@Test
	void testMakePurchaseWhenCardIsExpirated() {

		productEntity.setExpirationDate("2022-06-18");
		when(productRepository.findByActiveCardNumber(any())).thenReturn(productEntity);
		assertThrows(BadRequestException.class, ()-> serviceImpl.makePurchase("72384487", price));
	}
	
	@Test
	void testMakePurchaseWhenInsufficientFunds() {

		productEntity.setBalance(BigDecimal.ZERO);
		when(productRepository.findByActiveCardNumber(any())).thenReturn(productEntity);
		assertThrows(BadRequestException.class, ()-> serviceImpl.makePurchase("7238448457", price));
	}
	
	@Test
	void testMakePurchaseWhenPriceIsWront() {
		when(productRepository.findByActiveCardNumber(any())).thenReturn(productEntity);
		assertThrows(BadRequestException.class, ()-> serviceImpl.makePurchase("7238448457", BigDecimal.ZERO));
	}

	@Test
	void testCancelTransaction() {
		when(transactionRepository.findByTransactionIdAndCardNumber(any(), any())).thenReturn(transactionEntity);
		when(productRepository.findByActiveCardNumber(any())).thenReturn(productEntity);
		var expected = ResponseEntity.ok("La transacción 3 ha sido anulada.");
		var result = serviceImpl.cancelTransaction("", 3);
		assertEquals(expected, result);
	}
	
	@Test
	void testCancelTransactionWhenIsNull() {
		when(transactionRepository.findByTransactionIdAndCardNumber(any(), any())).thenReturn(null);
		assertThrows(NotFoundException.class, ()-> serviceImpl.cancelTransaction("", 3));
	}
	
	@Test
	void testCancelTransactionWhenIsCancelled() {
		transactionEntity.setTransactionStatus(TransactionStatus.ANULADA);
		when(transactionRepository.findByTransactionIdAndCardNumber(any(), any())).thenReturn(transactionEntity);
		when(productRepository.findByActiveCardNumber(any())).thenReturn(productEntity);
		assertThrows(BadRequestException.class, ()-> serviceImpl.cancelTransaction("", 3));
	}
	
	@Test
	void testCancelTransactionWhenIsTimeToCancelGone() {
		transactionEntity.setPurchaseDate(LocalDateTime.of(2023, 06, 10, 18, 32));
		when(transactionRepository.findByTransactionIdAndCardNumber(any(), any())).thenReturn(transactionEntity);
		when(productRepository.findByActiveCardNumber(any())).thenReturn(productEntity);
		assertThrows(BadRequestException.class, ()-> serviceImpl.cancelTransaction("", 3));
	}

	@Test
	void testGetTransaction() {
		when(transactionRepository.findById(any())).thenReturn(Optional.of(transactionEntity));
		var expected = transactionEntity;
		var result = serviceImpl.getTransaction(3);
		assertEquals(expected, result);
	}
	
	@Test
	void testGetTransactionException() {
		when(transactionRepository.findById(8)).thenReturn(Optional.of(transactionEntity));
		assertThrows(NotFoundException.class, ()-> serviceImpl.getTransaction(4));
	}

}
