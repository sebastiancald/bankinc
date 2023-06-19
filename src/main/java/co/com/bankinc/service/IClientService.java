package co.com.bankinc.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import co.com.bankinc.entity.ClientEntity;

public interface IClientService {
	
	public ResponseEntity<String> createClient(ClientEntity client);
	
	public List<ClientEntity> getAllClients();

}
