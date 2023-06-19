package co.com.bankinc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.com.bankinc.entity.ClientEntity;
import co.com.bankinc.exception.BadRequestException;
import co.com.bankinc.exception.NotFoundException;
import co.com.bankinc.repository.IClientRepository;
import co.com.bankinc.service.IClientService;

@Service
public class ClientServiceImpl implements IClientService {

	@Autowired
	private IClientRepository clientRepository;

	@Override
	public ResponseEntity<String> createClient(ClientEntity client) {
		if (client == null) {
			throw new BadRequestException("ERROR_CREATING");
		}
		clientRepository.save(client);

		return ResponseEntity.ok("Usuario creado ecitosamente");
	}

	@Override
	public List<ClientEntity> getAllClients() {
		List<ClientEntity> clientList = clientRepository.findAll();
		if (clientList.isEmpty()) {
			throw new NotFoundException("NOT_FOUND_INFO");
		}
		return clientList;
	}

}
