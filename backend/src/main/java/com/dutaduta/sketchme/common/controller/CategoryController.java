package com.dutaduta.sketchme.common.controller;

import com.dutaduta.sketchme.common.dto.CategoryRequest;
import com.dutaduta.sketchme.common.dto.CategoryResponse;
import com.dutaduta.sketchme.common.service.CategoryService;
import com.dutaduta.sketchme.file.dto.ImgUrlResponse;
import com.dutaduta.sketchme.global.ResponseFormat;
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
public class CategoryController {

    private final CategoryService categoryService;
    @PostMapping("/category")
    public ResponseEntity<ResponseFormat<String>> registCategory(@RequestBody CategoryRequest categoryRequest, HttpServletRequest request){
        Long artistID = JwtProvider.getArtistId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        categoryService.registCategory(categoryRequest, artistID);
        return ResponseFormat.success("카테고리가 성공적으로 등록되었습니다.").toEntity();
    }

    /**
     * 카테고리 수정하기
     * 카테고리 내의 그림의 추가 등록/삭제도 여기서 처리한다.
     * @param categoryRequest
     * @param request
     * @return
     */
    @PutMapping("/category")
    public ResponseEntity<ResponseFormat<List<ImgUrlResponse>>> modifyCategory(@RequestPart(value = "categoryInfo") CategoryRequest categoryRequest, @Nullable @RequestPart(value = "uploadFiles") MultipartFile[] uploadFiles, HttpServletRequest request) {
        Long artistID = JwtProvider.getArtistId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        List<ImgUrlResponse> updatedImages = categoryService.modifyCategory(categoryRequest, uploadFiles, artistID);
        return ResponseFormat.success(updatedImages).toEntity();
    }

    @DeleteMapping("/category")
    public ResponseEntity<ResponseFormat<String>> deleteCategory(@RequestBody Map<String, Long> categoryMap, HttpServletRequest request) {
        Long artistID = JwtProvider.getArtistId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        categoryService.deleteCategory(categoryMap.get("categoryID"), artistID);
        return ResponseFormat.success("카테고리가 삭제되었습니다.").toEntity();
    }

    @PutMapping("/category/list/{categoryID}")
    public ResponseEntity<ResponseFormat<String>> changeCategoryIsOpen(@PathVariable Long categoryID, @RequestParam Boolean isOpen, HttpServletRequest request) {
        Long artistID = JwtProvider.getArtistId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        categoryService.changeCategoryIsOpen(categoryID, artistID, isOpen);
        return ResponseFormat.success("카테고리 공개 여부 전환 완료되었습니다.").toEntity();
    }

    @GetMapping("/category/list/{artistID}")
    public ResponseEntity<ResponseFormat<List<CategoryResponse>>> seeArtistCategories(@PathVariable Long artistID, HttpServletRequest request) {
        Long loginArtistID = null;
        // 로그인을 한 사용자인 경우
        if(request.getHeader("Authorization") != null) {
            loginArtistID = JwtProvider.getArtistId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        }
        List<CategoryResponse> categoryResponseDTOs = categoryService.selectArtistCategories(artistID, loginArtistID);
        return ResponseFormat.success(categoryResponseDTOs).toEntity();
    }
}
