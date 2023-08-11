package com.dutaduta.sketchme.chat.dao;


import com.dutaduta.sketchme.member.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateUserTestRepository extends JpaRepository<User, Long> {
    User save(User user);
}