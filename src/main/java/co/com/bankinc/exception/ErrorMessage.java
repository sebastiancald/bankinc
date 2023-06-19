package co.com.bankinc.exception;

import lombok.Data;

@Data
public class ErrorMessage {

	private String message;

	private Integer status;

	public ErrorMessage(String message, Integer status) {
		this.message = message;
		this.status = status;
	}

}
