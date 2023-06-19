package co.com.bankinc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.com.bankinc.entity.ProductEntity;

public interface IProductRepository extends JpaRepository<ProductEntity, Integer> {

	@Query(value="SELECT p FROM ProductEntity p WHERE p.cardNumber = :cardNumber AND p.cardStatus = 1")
	ProductEntity findProductByCardNumber(@Param("cardNumber") String cardNumber);
	
	ProductEntity findByCardNumber(String cardNumber);
	
	@Query(value="SELECT p FROM ProductEntity p WHERE p.cardNumber = :cardNumber AND p.cardStatus = 0")
	ProductEntity findByActiveCardNumber(String cardNumber);
	
	@Query(value="SELECT p FROM ProductEntity p WHERE p.cardNumber = :cardNumber AND p.cardStatus = 0 AND p.client IS NULL")
	ProductEntity findByActiveCardNumberWithOutClient(String cardNumber);


}
