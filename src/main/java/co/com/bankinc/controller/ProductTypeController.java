package co.com.bankinc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.bankinc.entity.ProductTypeEntity;
import co.com.bankinc.service.IProductTypeService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/productType")
public class ProductTypeController {
	
	@Autowired
	private IProductTypeService productTypeService;

	
	@PostMapping
	public ResponseEntity<String> createProductType(@RequestBody Map<String, String> request){
		var productType = new ProductTypeEntity();
		productType.setProductName(request.get("productName"));
		productType.setProductTypeCode(request.get("productTypeCode"));
		productType.setProductTypeStatus(request.get("productTypeStatus"));
		return productTypeService.createProductType(productType);
	}
	
	@GetMapping
	public List<ProductTypeEntity> getAllProductType(){

		return productTypeService.getAllProductType();
	}
}
