package com.hspan.hspan.data.repos;

import com.hspan.hspan.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String userName);

    void deleteByUsername(String username);

}
