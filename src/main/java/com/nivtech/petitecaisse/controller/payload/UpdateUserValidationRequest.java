package com.nivtech.petitecaisse.controller.payload;

import javax.validation.constraints.Positive;

public class UpdateUserValidationRequest
{
    @Positive
    private Long userId;

    private Boolean isValidated;

    public Long getUserId()
    {
        return userId;
    }

    public Boolean getIsValidated()
    {
        return isValidated;
    }
}
