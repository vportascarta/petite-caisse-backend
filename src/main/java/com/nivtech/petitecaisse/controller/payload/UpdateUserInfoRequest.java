package com.nivtech.petitecaisse.controller.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UpdateUserInfoRequest
{

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String imageUrl;

    @Pattern(regexp = "^(\\d{5})?$")
    private String code;

    public String getName()
    {
        return name;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public String getCode()
    {
        return code;
    }
}
