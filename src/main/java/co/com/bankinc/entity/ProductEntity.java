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

import co.com.bankinc.util.Currency;
import co.com.bankinc.util.Status;
import lombok.Data;

@Data
@Entity
@Table(name="tbl_producto")
public class ProductEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_producto")
	private Integer productId;
	
	@Column(name="numero_tarjeta")
	private String cardNumber;
	
	@Column(name="nombre_tarjetahabiente")
	private String cardHoldername;
	
	@Column(name="fecha_expiracion")
	private String expirationDate;
	
	@Column(name="estado")
	private Status cardStatus;

	@Column(name="balance")
	private BigDecimal balance;
	
	@Column(name="fecha_activacion")
	private LocalDateTime activationDate;
	
	@Column(name="tipo_moneda")
	private Currency currency;
	
	@ManyToOne
	@JoinColumn(name = "id_cliente")
	private ClientEntity client;
	
	@ManyToOne
	@JoinColumn(name = "id_tipo_producto")
	private ProductTypeEntity productType;
}
