package co.com.bankinc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.bankinc.entity.ClientEntity;
import co.com.bankinc.service.IClientService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/client")
public class ClientController {
	
	@Autowired
	private IClientService clientService;
	
	@PostMapping
	public ResponseEntity<String> createClient(@RequestBody Map<String, String> request){
		var client = new ClientEntity();
		client.setFirstName(request.get("firstName"));
		client.setSecondName(request.get("secondName"));
		client.setFirstSurname(request.get("firstSurname"));
		client.setSecondSurname(request.get("secondSurname"));
		client.setDocumentNumber(request.get("documentNumber"));
		return clientService.createClient(client);
	}
	
	
	@GetMapping
	public List<ClientEntity> getAllClient(){
		
		return clientService.getAllClients();
	}
}
