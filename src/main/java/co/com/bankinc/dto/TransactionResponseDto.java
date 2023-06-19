package co.com.bankinc.dto;

import java.math.BigDecimal;

import co.com.bankinc.util.TransactionStatus;
import lombok.Data;
@Data
public class TransactionResponseDto {
	
	private Integer transactionId;
	private BigDecimal purchaseAmount;
	private TransactionStatus transactionEstatus;
	private String cardNumber;

}
