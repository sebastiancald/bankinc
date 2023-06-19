package co.com.bankinc.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import co.com.bankinc.util.TransactionStatus;
import lombok.Data;

@Data
@Entity
@Table(name="tbl_transaccion")
public class TransactionEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_transaccion")
	private Integer transactionId;
	
	@Column(name="fecha_compra")
	private LocalDateTime purchaseDate;
	
	@Column(name="monto_compra")
	private BigDecimal purchaseAmount;
	
	@Column(name="estado_transaccion")
	private TransactionStatus transactionStatus;

	@ManyToOne
	@JoinColumn(name = "id_producto")
	private ProductEntity product;

}
