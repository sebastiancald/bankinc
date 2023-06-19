package co.com.bankinc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.bankinc.entity.ProductTypeEntity;
@Repository
public interface IProductTypeRepository extends JpaRepository<ProductTypeEntity, Integer>{

}
