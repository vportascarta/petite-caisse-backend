package com.nivtech.petitecaisse.controller.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginRequest
{
    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @NotBlank
    private String password;

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }
}
