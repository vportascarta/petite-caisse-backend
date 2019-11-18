package com.nivtech.petitecaisse.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    private String imageUrl;

    @Column(nullable = false)
    private Boolean userVerified = false;

    @Column(nullable = false)
    private Integer nbBadPinCode = Integer.MAX_VALUE;

    @JsonIgnore
    private String userPin;

    @JsonIgnore
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    @Column
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER, targetClass = Role.class)
    private List<Role> roles;

    @UpdateTimestamp
    private Date updateAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "balance_id", referencedColumnName = "id")
    private Balance balance;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "debtor")
    @JsonIgnoreProperties({"debtor"})
    private List<Transaction> debtTransactions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "buyer")
    @JsonIgnoreProperties({"buyer"})
    private List<Transaction> buyerTransactions;

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

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public Boolean getUserVerified()
    {
        return userVerified;
    }

    public void setUserVerified(Boolean userVerified)
    {
        this.userVerified = userVerified;
    }

    public Integer getNbBadPinCode()
    {
        return nbBadPinCode;
    }

    public void setNbBadPinCode(Integer nbBadPinCode)
    {
        this.nbBadPinCode = nbBadPinCode;
    }

    public String getUserPin()
    {
        return userPin;
    }

    public void setUserPin(String userPin)
    {
        this.userPin = userPin;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public AuthProvider getProvider()
    {
        return provider;
    }

    public void setProvider(AuthProvider provider)
    {
        this.provider = provider;
    }

    public String getProviderId()
    {
        return providerId;
    }

    public void setProviderId(String providerId)
    {
        this.providerId = providerId;
    }

    public List<Role> getRoles()
    {
        return roles;
    }

    public void setRoles(List<Role> roles)
    {
        this.roles = roles;
    }

    public Balance getBalance()
    {
        return balance;
    }

    public void setBalance(Balance balance)
    {
        this.balance = balance;
    }

    public Date getUpdateAt()
    {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt)
    {
        this.updateAt = updateAt;
    }

    public List<Transaction> getDebtTransactions()
    {
        return debtTransactions;
    }

    public void setDebtTransactions(List<Transaction> debtTransactions)
    {
        this.debtTransactions = debtTransactions;
    }

    public List<Transaction> getBuyerTransactions()
    {
        return buyerTransactions;
    }

    public void setBuyerTransactions(List<Transaction> buyerTransactions)
    {
        this.buyerTransactions = buyerTransactions;
    }
}
