package com.dutaduta.sketchme.member.dao;

import com.dutaduta.sketchme.member.domain.OAuthType;
import com.dutaduta.sketchme.member.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByOauthIdAndOauthType(String oauthId, OAuthType oAuthType);

    Boolean existsByNickname(String nickname);
}
