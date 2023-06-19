package co.com.bankinc.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import co.com.bankinc.entity.ClientEntity;
import co.com.bankinc.entity.ProductEntity;
import co.com.bankinc.entity.ProductTypeEntity;
import co.com.bankinc.exception.BadRequestException;
import co.com.bankinc.exception.NotFoundException;
import co.com.bankinc.repository.IClientRepository;
import co.com.bankinc.repository.IProductRepository;
import co.com.bankinc.repository.IProductTypeRepository;
import co.com.bankinc.util.Status;

class ProductServiceImplTest {

	@Mock
	private IProductRepository productRepository;
	
	@Mock
	private IProductTypeRepository productTypeRepository;

	@Mock
	private IClientRepository clientRepository;
	
	@InjectMocks
	private ProductServiceImpl productServiceImpl;
	
	private ProductTypeEntity productTypeEntity;
	
	private ProductEntity productEntity;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		
		productTypeEntity = new ProductTypeEntity();
		productTypeEntity.setProductTypeId(1);
		productTypeEntity.setProductName("TC");
		productTypeEntity.setProductTypeCode("1234569999");
		
		productEntity = new ProductEntity();
		productEntity.setProductId(1);
		productEntity.setBalance(BigDecimal.ZERO);
		productEntity.setProductType(productTypeEntity);
		productEntity.setExpirationDate("2026-06-18");
		
		
	}

	@Test
	void testGenerateCardNumber() {
		when(productTypeRepository.findById(1)).thenReturn(Optional.of(productTypeEntity));
		when(productRepository.save(any())).thenReturn(productEntity);
		var expected = productEntity;
		var result = productServiceImpl.generateCardNumber(1);
		assertEquals(expected, result);
	}
	
	@Test
	void testGenerateCardNumberException() {
		when(productTypeRepository.findById(99)).thenReturn(Optional.of(productTypeEntity));
		assertThrows(NotFoundException.class, ()->productServiceImpl.generateCardNumber(null));
		
	}
	
	@Test
	void testGenerateCardNumberBadRequestException() {
		productTypeEntity.setProductTypeCode("jfdfhs");
		when(productTypeRepository.findById(1)).thenReturn(Optional.of(productTypeEntity));
		when(productRepository.save(any())).thenReturn(productEntity);
		assertThrows(BadRequestException.class, ()-> productServiceImpl.generateCardNumber(1));
		
	}

	@Test
	void testActivateProduct() {
		productEntity.setCardNumber("6653747372443");
		when(productRepository.findProductByCardNumber(any())).thenReturn(productEntity);
		var expected = ResponseEntity.ok("Tarjeta Activada");
		var result = productServiceImpl.activateProduct("6653747372443");
		assertEquals(expected, result);
	}
	
	@Test
	void testActivateProductException() {
		
		when(productRepository.findProductByCardNumber(any())).thenReturn(null);
		assertThrows(BadRequestException.class, ()-> productServiceImpl.activateProduct("66537477772443"));
		
	}

	@Test
	void testBlockProduct() {
		productEntity.setCardStatus(Status.ACTIVO);
		when(productRepository.findByCardNumber(any())).thenReturn(productEntity);
		var expected = ResponseEntity.ok("Tarjeta Bloqueada");
		var result = productServiceImpl.blockProduct("6653747372443");
		assertEquals(expected, result);
		
	}
	
	@Test
	void testBlockProductIsBlocked() {
		productEntity.setCardStatus(Status.BLOQUEADO);
		when(productRepository.findByCardNumber(any())).thenReturn(productEntity);
		assertThrows(BadRequestException.class, ()-> productServiceImpl.blockProduct("6653747372443"));
		
		
	}
	
	@Test
	void testBlockProductexceptionIsNull() {
		productEntity.setCardStatus(Status.BLOQUEADO);
		when(productRepository.findByCardNumber(any())).thenReturn(null);
		assertThrows(BadRequestException.class, ()-> productServiceImpl.blockProduct("6653747372443"));
		
		
	}

	@Test
	void testRechargeBalance() {
		var amount = BigDecimal.valueOf(20);
		productEntity.setCardStatus(Status.ACTIVO);
		when(productRepository.findByCardNumber(any())).thenReturn(productEntity);
		var expected = ResponseEntity.ok("Has recargado " + amount + " USD.");
		var result = productServiceImpl.rechargeBalance("6653747372443", amount);
		assertEquals(expected, result);
	}
	
	@Test
	void testRechargeBalanceAmountException() {
		var amount = BigDecimal.valueOf(-20);
		productEntity.setCardStatus(Status.ACTIVO);
		when(productRepository.findByCardNumber(any())).thenReturn(productEntity);
		assertThrows(BadRequestException.class, ()-> productServiceImpl.rechargeBalance("6653747372443", amount));
		
	}

	@Test
	void testGetBalance() {
		var amount = BigDecimal.valueOf(20);
		productEntity.setBalance(amount);
		productEntity.setCardStatus(Status.ACTIVO);
		when(productRepository.findByCardNumber(any())).thenReturn(productEntity);
		var expected = productEntity.getBalance();
		var result = productServiceImpl.getBalance("786358934");
		assertEquals(expected, result);
	}

	@Test
	void testAssignCardToClient() {
		var user = new ClientEntity();
		user.setClientId(2);
		user.setDocumentNumber("1234");
		user.setFirstName("Juan");
		user.setFirstSurname("Arnau");
		when(clientRepository.findByDocumentNumber(any())).thenReturn(user);
		productEntity.setCardStatus(Status.ACTIVO);
		productEntity.setCardNumber("6653747372443456");
		productEntity.setClient(null);
		when(productRepository.findByActiveCardNumberWithOutClient(any())).thenReturn(productEntity);
		var expected = ResponseEntity.ok("Se ha asociado exitosamente el usuario Juan Arnau con la tarjeta "
				+ "************3456");
		var result = productServiceImpl.assignCardToClient("6653747372443456", "1234");
		assertEquals(expected, result);
	}
	
	@Test
	void testAssignCardToClientExceptionWhenIsNull() {
		var user = new ClientEntity();
		user.setClientId(2);
		user.setDocumentNumber("1234");
		user.setFirstName("Juan");
		user.setFirstSurname("Arnau");
		when(clientRepository.findByDocumentNumber(any())).thenReturn(null);
		productEntity.setCardStatus(Status.ACTIVO);
		productEntity.setCardNumber("6653747372443456");
		when(productRepository.findByActiveCardNumberWithOutClient(any())).thenReturn(null);
		assertThrows(NotFoundException.class, ()-> productServiceImpl.assignCardToClient("6653747372443456", "1234"));
		
	}

}
