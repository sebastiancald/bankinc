package co.com.bankinc.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import co.com.bankinc.entity.ClientEntity;
import co.com.bankinc.exception.BadRequestException;
import co.com.bankinc.exception.NotFoundException;
import co.com.bankinc.repository.IClientRepository;

class ClientServiceImplTest {
	
	@Mock
	private IClientRepository clientRepository;
	
	@InjectMocks
	private ClientServiceImpl clientServiceImpl;
	
	private ClientEntity entity;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		
		entity = new ClientEntity();
		entity.setClientId(1);
				
				
	}

	@Test
	void testCreateClient() {
		ResponseEntity<String> expected = ResponseEntity.ok("Usuario creado ecitosamente");
		ResponseEntity<String> result = clientServiceImpl.createClient(entity);
		assertEquals(expected, result);
	}
	
	@Test
	void testCreateClientExceeption() {
		assertThrows(BadRequestException.class, ()-> clientServiceImpl.createClient(null));
		
	}

	@Test
	void testGetAllClients() {
		List<ClientEntity> clientList = List.of(entity);
		when(clientRepository.findAll()).thenReturn(clientList);
		var expected = clientList;
		var result = clientServiceImpl.getAllClients();
		assertEquals(expected, result);
	}
	
	@Test
	void testGetAllClientsException() {
		List<ClientEntity> clientList = List.of();
		when(clientRepository.findAll()).thenReturn(clientList);
		assertThrows(NotFoundException.class, ()-> clientServiceImpl.getAllClients());
	}

}
