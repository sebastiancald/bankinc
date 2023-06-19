package co.com.bankinc.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import co.com.bankinc.entity.ProductTypeEntity;
import co.com.bankinc.exception.BadRequestException;
import co.com.bankinc.exception.NotFoundException;
import co.com.bankinc.repository.IProductTypeRepository;

class ProductTypeServiceImplTest {

	@Mock
	private IProductTypeRepository productTypeRepository;

	@InjectMocks
	private ProductTypeServiceImpl productTypeServiceImpl;

	private ProductTypeEntity entity;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);

		entity = new ProductTypeEntity();
		entity.setProductTypeId(1);
		entity.setProductName("TC");
		entity.setProductTypeCode("123445");
		entity.setProductTypeStatus("ACTIVO");
	}

	@Test
	void testCreateProductType() {
		ResponseEntity<String> expected = ResponseEntity.ok("Se ha creado el nuevo tipo de producto con exito.");
		ResponseEntity<String> result = productTypeServiceImpl.createProductType(entity);
		assertEquals(expected, result);
	}
	
	@Test
	void testCreateProductTypeException() {
		 assertThrows(BadRequestException.class,()-> productTypeServiceImpl.createProductType(null));
		
	}

	@Test
	void testGetAllProductType() {
		List<ProductTypeEntity> productTypeList = List.of(entity);
		when(productTypeRepository.findAll()).thenReturn(productTypeList);
		var expected = productTypeList;
		var result = productTypeServiceImpl.getAllProductType();
		assertEquals(expected, result);
	}
	
	@Test
	void testGetAllProductTypeException() {
		List<ProductTypeEntity> productTypeList = List.of();
		when(productTypeRepository.findAll()).thenReturn(productTypeList);
		assertThrows(NotFoundException.class, ()-> productTypeServiceImpl.getAllProductType());
	}

}
