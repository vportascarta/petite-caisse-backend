package com.nivtech.petitecaisse.repository;

import com.nivtech.petitecaisse.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>
{

    List<Transaction> findAllByPurchaseAtBetween(Date start, Date end);

}
