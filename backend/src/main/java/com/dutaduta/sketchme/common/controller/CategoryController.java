package com.dutaduta.sketchme.common.controller;

import com.dutaduta.sketchme.common.dto.CategoryRequest;
import com.dutaduta.sketchme.common.dto.CategoryResponse;
import com.dutaduta.sketchme.common.service.CategoryService;
import com.dutaduta.sketchme.global.ResponseFormat;
import com.dutaduta.sketchme.oidc.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/category")
    public ResponseEntity<ResponseFormat<String>> modifyCategory(@RequestBody CategoryRequest categoryRequest, HttpServletRequest request) {
        Long artistID = JwtProvider.getArtistId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        categoryService.modifyCategory(categoryRequest, artistID);
        return ResponseFormat.success("카테고리 수정이 완료되었습니다.").toEntity();
    }

    @DeleteMapping("/category")
    public ResponseEntity<ResponseFormat<String>> deleteCategory(@RequestBody Map<String, Long> categoryMap, HttpServletRequest request) {
        Long artistID = JwtProvider.getArtistId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        categoryService.deleteCategory(categoryMap.get("categoryID"), artistID);
        return ResponseFormat.success("카테고리가 삭제되었습니다.").toEntity();
    }

    @PutMapping("/category/{categoryID}")
    public ResponseEntity<ResponseFormat<String>> changeCategoryIsOpen(@PathVariable Long categoryID, @RequestParam Boolean isOpen, HttpServletRequest request) {
        Long artistID = JwtProvider.getArtistId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        categoryService.changeCategoryIsOpen(categoryID, artistID, isOpen);
        return ResponseFormat.success("카테고리 공개 여부 전환 완료되었습니다.").toEntity();
    }

    @GetMapping("/category")
    public ResponseEntity<ResponseFormat<List<CategoryResponse>>> seeArtistCategories(@RequestBody Map<String, Long> artistMap, HttpServletRequest request) {
        Long loginArtistID = JwtProvider.getArtistId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        List<CategoryResponse> categoryResponseDTOs = categoryService.selectArtistCategories(artistMap.get("artistID"), loginArtistID);
        return ResponseFormat.success(categoryResponseDTOs).toEntity();
    }
}
