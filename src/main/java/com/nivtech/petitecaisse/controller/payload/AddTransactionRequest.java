package com.nivtech.petitecaisse.controller.payload;

import javax.validation.constraints.Positive;

public class AddTransactionRequest
{
    @Positive
    private Long debtorId;

    @Positive
    private Long buyerId;

    @Positive
    private Long productId;

    @Positive
    private Integer quantity;

    private String code;

    public Long getDebtorId()
    {
        return debtorId;
    }

    public Long getBuyerId()
    {
        return buyerId;
    }

    public Long getProductId()
    {
        return productId;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public String getCode()
    {
        return code;
    }
}
