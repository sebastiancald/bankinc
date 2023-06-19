package co.com.bankinc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="tbl_tipo_producto")
public class ProductTypeEntity {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_tipo_producto")
	private Integer productTypeId;
	
	@Column(name="nombre_producto")
	private String productName;
	
	@Column(name="estado_tipo_producto")
	private String productTypeStatus;
	
	@Column(name="codigo_tipo_producto")
	private String productTypeCode;

}
