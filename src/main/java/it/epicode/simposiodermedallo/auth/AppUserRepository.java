package it.epicode.simposiodermedallo.auth;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByRolesContaining(Role role);
    Optional<AppUser> findByIdAndRolesContaining(Long id, Role role);
}
