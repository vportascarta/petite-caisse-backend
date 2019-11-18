package com.nivtech.petitecaisse.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "products")
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 2047)
    private String description;

    private Double price;

    private String imageUrl;

    @UpdateTimestamp
    private Date updateAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_id", referencedColumnName = "id")
    private Stock stock;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnoreProperties({"product"})
    private List<Transaction> productTransactions;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Double getPrice()
    {
        return price;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public Date getUpdateAt()
    {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt)
    {
        this.updateAt = updateAt;
    }

    public Stock getStock()
    {
        return stock;
    }

    public void setStock(Stock stock)
    {
        this.stock = stock;
    }

    public List<Transaction> getProductTransactions()
    {
        return productTransactions;
    }

    public void setProductTransactions(List<Transaction> productTransactions)
    {
        this.productTransactions = productTransactions;
    }
}
