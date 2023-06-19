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
@Table(name="tbl_cliente")
public class ClientEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_cliente")
	private Integer clientId;
	
	@Column(name="primer_nombre")
	private String firstName;
	
	@Column(name="segundo_nombre")
	private String secondName;
	
	@Column(name="primer_apellido")
	private String firstSurname;
	
	@Column(name="segundo_apellido")
	private String secondSurname;
	
	@Column(name="numero_documento")
	private String documentNumber;

}
