package co.com.bankinc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.com.bankinc.entity.TransactionEntity;

public interface ITransactionRepository extends JpaRepository<TransactionEntity, Integer>{
	
	@Query("SELECT t FROM TransactionEntity t WHERE t.transactionId = :transactionId AND t.product.cardNumber = :cardNumber")
	TransactionEntity findByTransactionIdAndCardNumber( @Param("cardNumber") String cardNumber, @Param("transactionId") Integer transactionId);

}
