package com.nivtech.petitecaisse.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "balances")
public class Balance
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @OneToOne(mappedBy = "balance")
    @JsonIgnore
    private User user;

    /**
     * A positive value indicate a credit / a negative value indicate a debt
     */
    private Double amount = 0.0;

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

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Double getAmount()
    {
        return amount;
    }

    public void addAmount(Double amount)
    {
        this.amount += amount;
    }

    public void setAmount(Double amount)
    {
        this.amount = amount;
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
