package com.example.register.repository;

import com.example.register.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query("SELECT u FROM User u WHERE u.email = :username or u.mobile= :username")
    User findByEmailOrMobile(String username);

    //@Query("SELECT u FROM User u WHERE u.email = :username or u.mobile= :username")
    //Optional<User> findUser();
}
