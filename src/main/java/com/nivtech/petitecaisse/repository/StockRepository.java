package com.nivtech.petitecaisse.repository;

import com.nivtech.petitecaisse.domain.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long>
{
}
