package com.dutaduta.sketchme.product.controller;

import com.dutaduta.sketchme.file.dto.ImgUrlResponse;
import com.dutaduta.sketchme.global.ResponseFormat;
import com.dutaduta.sketchme.oidc.jwt.JwtProvider;
import com.dutaduta.sketchme.product.dto.PictureResponseDTO;
import com.dutaduta.sketchme.product.service.ProductService;
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
public class ProductController {

    private final ProductService productService;

    @PostMapping("/drawing/artist")
    public ResponseEntity<ResponseFormat<List<ImgUrlResponse>>> registDrawingsOfArtist(MultipartFile[] uploadFiles, HttpServletRequest request) {
        Long artistID = JwtProvider.getArtistId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        List<ImgUrlResponse> imgUrlResponses = productService.registDrawingsOfArtist(uploadFiles, artistID);
        return ResponseFormat.success(imgUrlResponses).toEntity();
    }

    @DeleteMapping("/drawing/artist")
    public ResponseEntity<ResponseFormat<String>> deleteDrawingOfArtist(@RequestBody Map<String, Long> pictureMap, HttpServletRequest request) {
        Long artistID = JwtProvider.getArtistId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        productService.deleteDrawingOfArtist(pictureMap.get("pictureID"), artistID);
        return ResponseFormat.success("그림 삭제 완료").toEntity();
    }

    @GetMapping("/drawing/artist")
    public ResponseEntity<ResponseFormat<List<PictureResponseDTO>>> seeDrawingsOfArtist(HttpServletRequest request) {
        Long artistID = JwtProvider.getArtistId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        List<PictureResponseDTO> pictureResponseDTOs = productService.selectDrawingsOfArtist(artistID);
        return ResponseFormat.success(pictureResponseDTOs).toEntity();
    }

    @PostMapping("/drawing/category")
    public ResponseEntity<ResponseFormat<List<ImgUrlResponse>>> addDrawingToCategory(@RequestPart(value = "categoryID") Map<String, Long> categoryMap, @RequestPart(value = "uploadFiles") MultipartFile[] uploadFiles, HttpServletRequest request) {
        Long artistID = JwtProvider.getArtistId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        Long categoryID = categoryMap.get("categoryID");
        List<ImgUrlResponse> imgUrlResponses = productService.registDrawingsOfCategory(uploadFiles, categoryID, artistID);
        return ResponseFormat.success(imgUrlResponses).toEntity();
    }

    @GetMapping("/search/drawing")
    public ResponseEntity<ResponseFormat<List<PictureResponseDTO>>> searchPictures() {
        List<PictureResponseDTO> pictureResponseDTOs = productService.searchPictures();
        return ResponseFormat.success(pictureResponseDTOs).toEntity();
    }

}
