package com.nivtech.petitecaisse.domain;

import com.nivtech.petitecaisse.controller.payload.AddTransactionRequest;
import com.nivtech.petitecaisse.domain.entity.Balance;
import com.nivtech.petitecaisse.domain.entity.Stock;
import com.nivtech.petitecaisse.domain.entity.Transaction;
import com.nivtech.petitecaisse.exception.BadRequestException;
import com.nivtech.petitecaisse.repository.ProductRepository;
import com.nivtech.petitecaisse.repository.TransactionRepository;
import com.nivtech.petitecaisse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService
{
    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(ProductRepository productRepository, UserRepository userRepository,
            TransactionRepository transactionRepository)
    {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public Page<Transaction> getLastTransactions(int page, int size)
    {
        return transactionRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "purchaseAt"))
        );
    }

    public List<Transaction> getLastTransactions(LocalDateTime start, LocalDateTime end)
    {
        Date startDate = Date.from(start.atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(end.atZone(ZoneId.systemDefault()).toInstant());

        return transactionRepository.findAllByPurchaseAtBetween(startDate, endDate);
    }

    @Transactional
    public List<Transaction> addTransactions(List<AddTransactionRequest> transactionRequests)
    {
        return transactionRequests.stream()
                .map(addTransactionRequest -> addTransaction(addTransactionRequest.getDebtorId(),
                        addTransactionRequest.getBuyerId(), addTransactionRequest.getProductId(),
                        addTransactionRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Transaction addTransaction(Long debtorId, Long buyerId, Long productId, Integer quantity)
    {
        var optDebtor = userRepository.findById(debtorId);
        var optBuyer = userRepository.findById(buyerId);
        var optProduct = productRepository.findById(productId);

        if (optDebtor.isEmpty() || optBuyer.isEmpty())
        {
            throw new BadRequestException("L'utilisateur n'a pas été trouvé");
        }

        if (optProduct.isEmpty())
        {
            throw new BadRequestException("Le produit n'a pas été trouvé");
        }

        var debtor = optDebtor.get();
        var buyer = optBuyer.get();
        var product = optProduct.get();
        if (!buyer.getUserVerified())
        {
            throw new BadRequestException("Vous n'avez pas le droit de faire des achats, contacter l'administrateur");
        }

        if (quantity == 0)
        {
            return null;
        }

        // Update product stock
        var stock = Optional.ofNullable(product.getStock()).orElseGet(Stock::new);
        if (stock.getQuantity() < quantity)
        {
            throw new BadRequestException("Pas assez de stock disponible");
        }
        stock.removeQuantity(quantity);
        product.setStock(stock);

        // Update debtor balance
        var debtorBalance = Optional.ofNullable(debtor.getBalance()).orElseGet(Balance::new);
        debtorBalance.addAmount(-(product.getPrice() * quantity));
        debtor.setBalance(debtorBalance);

        // Create transaction
        var transaction = new Transaction();
        transaction.setDebtor(debtor);
        transaction.setBuyer(buyer);
        transaction.setProduct(product);
        transaction.setQuantity(quantity);

        // Save
        productRepository.save(product);
        userRepository.save(debtor);
        transactionRepository.save(transaction);

        return transaction;
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteTransaction(Long transactionId)
    {
        var optTransaction = transactionRepository.findById(transactionId);

        if (optTransaction.isEmpty())
        {
            throw new BadRequestException("La transaction n'a pas été trouvée");
        }

        var transaction = optTransaction.get();
        var product = transaction.getProduct();
        var debtor = transaction.getDebtor();
        var quantity = transaction.getQuantity();

        // Update product stock
        var stock = Optional.ofNullable(product.getStock()).orElseGet(Stock::new);
        stock.removeQuantity(-quantity);
        product.setStock(stock);

        // Update debtor balance
        var debtorBalance = Optional.ofNullable(debtor.getBalance()).orElseGet(Balance::new);
        debtorBalance.addAmount(product.getPrice() * quantity);
        debtor.setBalance(debtorBalance);

        // Save & delete
        productRepository.save(product);
        userRepository.save(debtor);
        transactionRepository.delete(transaction);
    }
}
