package com.nivtech.petitecaisse.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transactions")
public class Transaction
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "debtor_id")
    @JsonIgnoreProperties({"email", "imageUrl", "emailVerified", "userVerified", "provider", "providerId", "roles",
            "balance", "debtTransactions", "buyerTransactions", "updateAt"})
    private User debtor;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties({"description", "imageUrl", "stock", "productTransactions", "updateAt"})
    private Product product;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    @JsonIgnoreProperties({"email", "imageUrl", "emailVerified", "userVerified", "provider", "providerId", "roles",
            "balance", "debtTransactions", "buyerTransactions", "updateAt"})
    private User buyer;

    @CreationTimestamp
    private Date purchaseAt;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public User getDebtor()
    {
        return debtor;
    }

    public void setDebtor(User debtor)
    {
        this.debtor = debtor;
    }

    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public User getBuyer()
    {
        return buyer;
    }

    public void setBuyer(User buyer)
    {
        this.buyer = buyer;
    }

    public Date getPurchaseAt()
    {
        return purchaseAt;
    }

    public void setPurchaseAt(Date purchaseAt)
    {
        this.purchaseAt = purchaseAt;
    }
}
