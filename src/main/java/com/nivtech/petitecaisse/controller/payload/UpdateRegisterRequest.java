package com.nivtech.petitecaisse.controller.payload;

import javax.validation.constraints.Positive;

public class UpdateRegisterRequest
{
    @Positive
    private Long registerId;

    @Positive
    private Double newAmount;

    public Long getRegisterId()
    {
        return registerId;
    }

    public Double getNewAmount()
    {
        return newAmount;
    }
}
