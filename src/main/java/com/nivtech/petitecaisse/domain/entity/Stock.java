package com.nivtech.petitecaisse.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "stocks")
public class Stock
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "stock")
    @JsonIgnore
    private Product product;

    private Integer quantity = 0;

    @UpdateTimestamp
    private Date updateAt;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
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

    public void removeQuantity(Integer quantity)
    {
        this.quantity -= quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public Date getUpdateAt()
    {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt)
    {
        this.updateAt = updateAt;
    }
}
