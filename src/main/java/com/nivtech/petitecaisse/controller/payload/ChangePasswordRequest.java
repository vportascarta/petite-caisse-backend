package com.nivtech.petitecaisse.controller.payload;

import javax.validation.constraints.NotBlank;

public class ChangePasswordRequest
{
    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;

    public String getOldPassword()
    {
        return oldPassword;
    }

    public String getNewPassword()
    {
        return newPassword;
    }
}
