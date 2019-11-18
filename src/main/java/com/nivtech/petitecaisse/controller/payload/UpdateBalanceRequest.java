package com.nivtech.petitecaisse.controller.payload;

import javax.validation.constraints.Positive;

public class UpdateBalanceRequest
{
    @Positive
    private Long registerId;

    @Positive
    private Long userId;

    @Positive
    private Double amountToBeTransferred;

    public Long getRegisterId()
    {
        return registerId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public Double getAmountToBeTransferred()
    {
        return amountToBeTransferred;
    }
}
