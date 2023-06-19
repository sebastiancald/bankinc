package co.com.bankinc.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import co.com.bankinc.entity.ProductTypeEntity;

public interface IProductTypeService {

	ResponseEntity<String> createProductType(ProductTypeEntity productType);

	List<ProductTypeEntity> getAllProductType();

}
