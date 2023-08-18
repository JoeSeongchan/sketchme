package com.dutaduta.sketchme.member.controller;

import com.dutaduta.sketchme.global.ResponseFormat;
import com.dutaduta.sketchme.global.exception.BusinessException;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.dto.ArtistInfoRequest;
import com.dutaduta.sketchme.member.dto.ArtistResponse;
import com.dutaduta.sketchme.member.service.ArtistService;
import com.dutaduta.sketchme.oidc.jwt.JwtProvider;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@CrossOrigin
public class ArtistController {

    private final ArtistService artistService;

    @GetMapping("/artist/desc/{id}")
    public ResponseEntity<ResponseFormat<String>> getArtistDescription(@PathVariable(name = "id") Long id) {
        log.info("id " + id);
        String description = artistService.getDescription(id);
        description = description == null ? "" : description;
        return ResponseFormat.success(description).toEntity();
    }

    @PostMapping("/artist/regist")
    public ResponseEntity<?> registArtist(HttpServletRequest request) {
        // 현재 사용자 id
        Long userId = JwtProvider.getUserId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        return artistService.registArtist(userId).toEntity();
    }

    /**
     * 작가 정보 수정
     * @param artistInfoRequest
     * @param uploadFile
     * @param request
     * @return
     */
    @PutMapping("/artist/info")
    public ResponseEntity<ResponseFormat<String>> modifyArtistInformation(@RequestPart(value = "dto") ArtistInfoRequest artistInfoRequest, @Nullable @RequestPart(value="uploadFile") MultipartFile uploadFile, HttpServletRequest request){
        Long artistId = JwtProvider.getArtistId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        artistService.modifyArtistInformation(artistInfoRequest, uploadFile, artistId);
        return ResponseFormat.success("작가 정보 수정 완료").toEntity();
    }

    @PutMapping("/artist")
    public ResponseEntity<?> changeArtistIsOpen(@RequestParam Boolean isOpen, HttpServletRequest request) {
        Long artistId = JwtProvider.getArtistId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        artistService.changeArtistIsOpen(isOpen, artistId);
        return ResponseFormat.success("작가 공개 여부 전환 완료").toEntity();
    }

    @DeleteMapping("/artist/deactivate")
    public ResponseEntity<?> deactivateArtist(HttpServletRequest request){
        Long artistId = JwtProvider.getArtistId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        Long userId = JwtProvider.getUserId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        artistService.deactivateArtist(artistId, userId);
        return ResponseFormat.success("작가 비활성화 완료").toEntity();
    }

    @PutMapping("/artist/desc")
    public ResponseEntity<ResponseFormat<String>> modifyArtistDescription(@RequestBody Map<String, String> descriptionMap, HttpServletRequest request) {
        Long artistId = JwtProvider.getArtistId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        artistService.modifyArtistDescription(descriptionMap.get("description"), artistId);
        return ResponseFormat.success("작가 소개 수정 완료").toEntity();
    }

    /**
     * 비활성화한 작가 계정을 다시 활성화한다.
     * (테스트 목적으로만 사용된다. 실제 배포할 때는 해당 API는 쓰지 않는다.
     * 추후에 삭제 예정)
     */
    @PutMapping("/artist/test/activate")
    public ResponseEntity<?> reactivateArtist(HttpServletRequest request) {
        Long artistId = JwtProvider.getArtistId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        artistService.reactivateArtist(artistId);
        return ResponseFormat.success("작가 비활성화 취소 완료 (프론트 테스트용!!)").toEntity();
    }

    @GetMapping("/search/artist")
    public ResponseEntity<ResponseFormat<List<ArtistResponse>>> searchArtists(@RequestParam String keyword, @RequestParam String orderBy) {
        log.info("Search Artist");
        List<ArtistResponse> artistResponses = artistService.searchArtists(keyword, orderBy);
        log.info("Search Artist Finish - total : " + artistResponses.size());
        return ResponseFormat.success(artistResponses).toEntity();
    }

    @GetMapping("/artist/info/{id}")
    public ResponseEntity<ResponseFormat<ArtistResponse>> getArtistInfo(@PathVariable Long id, HttpServletRequest request) {
        Long userId = 0L;
        // 로그인을 한 사용자인 경우
        if(request.getHeader("Authorization") != null) {
            userId = JwtProvider.getUserId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        }
        ArtistResponse artistResponse = artistService.getArtistInfo(id, userId);
        return ResponseFormat.success(artistResponse).toEntity();
    }


}
