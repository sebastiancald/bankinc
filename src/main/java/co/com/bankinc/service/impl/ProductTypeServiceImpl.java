package co.com.bankinc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.com.bankinc.entity.ProductTypeEntity;
import co.com.bankinc.exception.BadRequestException;
import co.com.bankinc.exception.NotFoundException;
import co.com.bankinc.repository.IProductTypeRepository;
import co.com.bankinc.service.IProductTypeService;

@Service
public class ProductTypeServiceImpl implements IProductTypeService {

	@Autowired
	private IProductTypeRepository productTypeRepository;

	@Override
	public ResponseEntity<String> createProductType(ProductTypeEntity productType) {
		if (productType == null) {
			throw new BadRequestException("ERROR_CREATING");
		}
		productTypeRepository.save(productType);

		return ResponseEntity.ok("Se ha creado el nuevo tipo de producto con exito.");

	}

	@Override
	public List<ProductTypeEntity> getAllProductType() {
		List<ProductTypeEntity> productTypeList = productTypeRepository.findAll();
		if (productTypeList.isEmpty()) {
			throw new NotFoundException("NOT_FOUND_INFO");
		}
		return productTypeList;
	}

}
