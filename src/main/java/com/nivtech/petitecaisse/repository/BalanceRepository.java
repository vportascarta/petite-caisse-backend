package com.nivtech.petitecaisse.repository;

import com.nivtech.petitecaisse.domain.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long>
{
}
