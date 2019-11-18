package com.nivtech.petitecaisse.domain;

import com.nivtech.petitecaisse.domain.entity.Balance;
import com.nivtech.petitecaisse.domain.entity.Role;
import com.nivtech.petitecaisse.domain.entity.User;
import com.nivtech.petitecaisse.exception.BadRequestException;
import com.nivtech.petitecaisse.exception.ResourceNotFoundException;
import com.nivtech.petitecaisse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService
{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUser(Long id)
    {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
    }

    public List<User> getAllUsers()
    {
        return userRepository.findAllByRolesContains(Role.ROLE_USER);
    }

    public List<User> getAllCashRegisters()
    {
        return userRepository.findAllByRolesContains(Role.ROLE_CASH);
    }

    @Transactional
    public User updateUser(Long id, String name, String imageUrl, String code)
    {
        var user = getUser(id);
        user.setName(name);
        user.setImageUrl(imageUrl);

        if (code != null)
        {
            user.setUserPin(passwordEncoder.encode(code));
            user.setNbBadPinCode(0);
        }

        return userRepository.save(user);
    }

    public boolean isUserCodeValid(Long id, String code)
    {
        var user = getUser(id);
        if (user.getNbBadPinCode() >= 3 || user.getUserPin().isEmpty())
        {
            return false;
        }

        var isValid = passwordEncoder.matches(code, user.getUserPin());

        if (isValid)
        {
            user.setNbBadPinCode(0);
        } else
        {
            user.setNbBadPinCode(user.getNbBadPinCode() + 1);
        }
        userRepository.save(user);

        return isValid;
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Boolean updateUserValidation(Long userId, Boolean isValidated)
    {
        var optUser = userRepository.findById(userId);

        if (optUser.isEmpty())
        {
            throw new BadRequestException("L'utilisateur n'a pas été trouvé");
        }

        var user = optUser.get();

        if (!user.getRoles().contains(Role.ROLE_USER))
        {
            throw new BadRequestException("L'utilisateur n'est pas valide");
        }

        if (isValidated == null) { isValidated = false; }
        user.setUserVerified(isValidated);
        userRepository.save(user);

        return isValidated;
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Pair<Double, Double> updateBalanceWithCashRegister(Long registerId, Long userId,
            Double amountToBeTransferred)
    {

        var optCashRegister = userRepository.findById(registerId);
        var optUser = userRepository.findById(userId);

        if (optCashRegister.isEmpty() || optUser.isEmpty())
        {
            throw new BadRequestException("L'utilisateur n'a pas été trouvé");
        }

        var cashRegister = optCashRegister.get();
        var user = optUser.get();

        if (!cashRegister.getRoles().contains(Role.ROLE_CASH))
        {
            throw new BadRequestException("La caisse n'est pas valide");
        }

        var cashRegisterBalance = Optional.ofNullable(cashRegister.getBalance()).orElseGet(Balance::new);
        var userBalance = Optional.ofNullable(user.getBalance()).orElseGet(Balance::new);

        cashRegisterBalance.addAmount(amountToBeTransferred);
        cashRegister.setBalance(cashRegisterBalance);

        userBalance.addAmount(amountToBeTransferred);
        user.setBalance(userBalance);

        userRepository.save(cashRegister);
        userRepository.save(user);

        return Pair.of(cashRegisterBalance.getAmount(), userBalance.getAmount());
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Double updateCashRegister(Long registerId, Double newAmount)
    {

        if (newAmount == null)
        {
            throw new BadRequestException("Le solde n'est pas correct");
        }

        var optCashRegister = userRepository.findById(registerId);

        if (optCashRegister.isEmpty())
        {
            throw new BadRequestException("L'utilisateur n'a pas été trouvé");
        }

        var cashRegister = optCashRegister.get();

        if (!cashRegister.getRoles().contains(Role.ROLE_CASH))
        {
            throw new BadRequestException("La caisse n'est pas valide");
        }

        var cashRegisterBalance = Optional.ofNullable(cashRegister.getBalance()).orElseGet(Balance::new);

        cashRegisterBalance.setAmount(newAmount);
        cashRegister.setBalance(cashRegisterBalance);

        userRepository.save(cashRegister);

        return cashRegisterBalance.getAmount();
    }

}
