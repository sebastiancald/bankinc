package co.com.bankinc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.bankinc.entity.ClientEntity;
@Repository
public interface IClientRepository extends JpaRepository<ClientEntity, Integer> {

	ClientEntity findByDocumentNumber(String documentNumber);
}
