package com.nivtech.petitecaisse.config;

import com.nivtech.petitecaisse.domain.entity.AuthProvider;
import com.nivtech.petitecaisse.domain.entity.Role;
import com.nivtech.petitecaisse.domain.entity.User;
import com.nivtech.petitecaisse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class DataLoader implements ApplicationRunner
{

    private UserRepository userRepository;

    @Autowired
    public DataLoader(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public void run(ApplicationArguments args)
    {
        // Load first user to act as the cash register
        if (!userRepository.existsByRolesContains(Role.ROLE_CASH))
        {
            User cashRegister = new User();
            cashRegister.setName("Caisse Principale");
            cashRegister.setEmail("caisse001@exemple.com");
            cashRegister.setProvider(AuthProvider.none);
            cashRegister.setRoles(Collections.singletonList(Role.ROLE_CASH));

            userRepository.save(cashRegister);
        }

    }
}
