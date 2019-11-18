package com.nivtech.petitecaisse.repository;

import com.nivtech.petitecaisse.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>
{

    List<Product> findAllByName(String name);

}
