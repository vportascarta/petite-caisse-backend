package com.nivtech.petitecaisse.repository;

import com.nivtech.petitecaisse.domain.entity.Role;
import com.nivtech.petitecaisse.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByRolesContains(Role role);

    List<User> findAllByRolesContains(Role role);

}
