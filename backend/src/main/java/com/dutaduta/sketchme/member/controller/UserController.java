package com.dutaduta.sketchme.member.controller;

import com.dutaduta.sketchme.file.dto.ImgUrlResponse;
import com.dutaduta.sketchme.global.ResponseFormat;
import com.dutaduta.sketchme.global.exception.BusinessException;
import com.dutaduta.sketchme.member.dto.MemberInfoResponse;
import com.dutaduta.sketchme.member.service.UserService;
import com.dutaduta.sketchme.oidc.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@CrossOrigin
public class UserController {

    private final UserService userService;

    @GetMapping("/user/profile")
    public ResponseEntity<?> getUserProfile(@RequestParam String member, HttpServletRequest request) {
        String secretKey = JwtProvider.getSecretKey();
        String token = JwtProvider.resolveToken(request);
        Long userId = JwtProvider.getUserId(token, secretKey);
        Long artistId = JwtProvider.getArtistId(token, secretKey);
        MemberInfoResponse memberInfoResponseDto = userService.getUserInfo(member, userId, artistId);
        log.info("member: {}",memberInfoResponseDto);
        return ResponseFormat.success(memberInfoResponseDto).toEntity();
    }

    @GetMapping("/user/check/{nickname}")
    public ResponseEntity<?> checkNickname(@PathVariable String nickname) {
        if(!userService.checkNickname(nickname)){
            return ResponseFormat.success("사용 가능한 닉네임입니다.").toEntity();
        } else{
            return ResponseFormat.fail(HttpStatus.BAD_REQUEST,"중복되는 닉네임입니다.").toEntity();
        }
    }

    @PutMapping("/user/info")
    public ResponseEntity<?> modifyUserInformation(@RequestBody Map<String, String> nicknameMap, HttpServletRequest request){
        Long userId = JwtProvider.getUserId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        try {
            userService.modifyUserInformation(nicknameMap.get("nickname"), userId);
            return ResponseFormat.success("닉네임 변경 완료").toEntity();
        } catch (BusinessException e){
            return ResponseFormat.fail(HttpStatus.BAD_REQUEST,"실패").toEntity();
        }
    }

    @PutMapping("/user/profile-image")
    public ResponseEntity<ResponseFormat<ImgUrlResponse>> updateProfileImage(@RequestParam String member, MultipartFile uploadFile, HttpServletRequest request){
        Long userId = JwtProvider.getUserId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        Long artistId = JwtProvider.getArtistId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        ImgUrlResponse imgUrlResponse = userService.updateProfileImage(uploadFile, member, userId, artistId);
        return ResponseFormat.success(imgUrlResponse).toEntity();
    }
}
