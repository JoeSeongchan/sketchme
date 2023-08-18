package com.dutaduta.sketchme.member.dao;

import com.dutaduta.sketchme.member.domain.OAuthType;
import com.dutaduta.sketchme.member.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByOauthIdAndOauthTypeAndIsDeleted(String oauthId, OAuthType oAuthType, Boolean isDeleted);
    Boolean existsByNickname(String nickname);
    Optional<User> findByIdAndIsDeleted(Long userId, Boolean isDeleted);
}
