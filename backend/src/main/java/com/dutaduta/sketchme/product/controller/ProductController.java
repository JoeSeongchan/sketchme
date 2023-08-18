package com.dutaduta.sketchme.product.controller;

import com.dutaduta.sketchme.file.dto.ImgUrlResponse;
import com.dutaduta.sketchme.global.ResponseFormat;
import com.dutaduta.sketchme.oidc.dto.UserInfoInAccessTokenDTO;
import com.dutaduta.sketchme.oidc.jwt.JwtProvider;
import com.dutaduta.sketchme.oidc.jwt.JwtUtil;
import com.dutaduta.sketchme.oidc.jwt.JwtUtil;
import com.dutaduta.sketchme.oidc.jwt.JwtUtilImplForDevAndProd;
import com.dutaduta.sketchme.oidc.jwt.JwtUtilImplForLocal;
import com.dutaduta.sketchme.product.dto.MyPictureResponse;
import com.dutaduta.sketchme.product.dto.PictureDeleteRequest;
import com.dutaduta.sketchme.product.dto.PictureResponse;
import com.dutaduta.sketchme.product.dto.TimelapseDTO;
import com.dutaduta.sketchme.product.service.ProductService;
import com.dutaduta.sketchme.product.service.response.FinalPictureGetResponse;
import com.dutaduta.sketchme.product.service.response.TimelapseGetResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ProductController {

    private final ProductService productService;
    private final JwtUtil jwtUtil;

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
    public ResponseEntity<ResponseFormat<List<PictureResponse>>> seeDrawingsOfArtist(HttpServletRequest request) {
        Long artistID = JwtProvider.getArtistId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        List<PictureResponse> pictureResponses = productService.selectDrawingsOfArtist(artistID);
        return ResponseFormat.success(pictureResponses).toEntity();
    }

    @GetMapping("/search/drawing")
    public ResponseEntity<ResponseFormat<List<PictureResponse>>> searchPictures(@RequestParam String keyword, @RequestParam String orderBy) {
        log.info("Search Drawing");
        List<PictureResponse> pictureResponses = productService.searchPictures(keyword, orderBy);
        return ResponseFormat.success(pictureResponses).toEntity();
    }

    @GetMapping("/my-drawings")
    public ResponseEntity<ResponseFormat<List<MyPictureResponse>>> seePicturesIBought(HttpServletRequest request){
        UserInfoInAccessTokenDTO userInfo = jwtUtil.extractUserInfo(request);
        Long userId = userInfo.getUserId();
        List<MyPictureResponse> myPictures = productService.seePicturesIBought(userId);
        return ResponseFormat.success(myPictures).toEntity();
    }

    /**
     * 화상 미팅 중에 작가가 실시간으로 보내는 그림 파일을 API 서버의 로컬 스토리지에 저장한다.
     * @param meetingId 미팅 ID
     * @param multipartFiles 작가의 브라우저가 실시간으로 보내는 그림 파일
     * @param request JWT 토큰 안에 든 유저의 개인정보를 꺼내기 위해 필요한 Reqeust 객체
     * @return 성공, 실패 여부
     */
    @PostMapping("/live-picture")
    public ResponseEntity<ResponseFormat<Object>> postLivePicture(@RequestParam("meetingId") Long meetingId, MultipartFile[] multipartFiles, HttpServletRequest request){
        if(!isFileExisted(multipartFiles)){
            return ResponseFormat.fail(HttpStatus.BAD_REQUEST,"라이브 그림 파일이 존재하지 않습니다. (여기서 라이브 파일이란 작가의 컴퓨터가 화상 미팅 중에 실시간으로 보내는 그림 파일을 의미합니다.").toEntity();
        }
        productService.saveLivePicture(jwtUtil.extractUserInfo(request),meetingId, LocalDateTime.now(),multipartFiles[0]);
        return ResponseFormat.success().toEntity();
    }

    // 타임랩스를 만들어달라는 클라이언트의 요청을 받고, '동기'적으로 타임랩스를 만든 후,
    // 해당 타임랩스의 'fileserver'에서의 경로를 리턴한다.
    @PostMapping("/timelapse/new")
    public ResponseEntity<ResponseFormat<Object>> makeTimelapse(@RequestParam("meetingId") Long meetingId, HttpServletRequest request){
        UserInfoInAccessTokenDTO userInfo = jwtUtil.extractUserInfo(request);
        // Timelapse를 '동기'적으로 만든다.
        TimelapseDTO timelapseDTO = productService.makeTimelapse(jwtUtil.extractUserInfo(request),meetingId);
        // Timelapse 경로, Timelapse Thumbnail 경로를 DB에 저장한다.
        productService.saveTimelapse(timelapseDTO, meetingId);
        // 최종 그림 파일을 라이브 폴더에서 가져와 저장하고, 경로를 DB에 저장한다.
        productService.saveFinalPicture(userInfo,meetingId,LocalDateTime.now());
        // Timelapse 를 다 만들었다는 사실을 클라이언트에게 알린다.
        return ResponseFormat.success().toEntity();
    }

    /**
     * 타임랩스를 가져와 클라이언트에게 전송한다.
     * @param meetingId 미팅 ID
     * @param request 토큰을 가져오기 위해 필요한 Request
     * @return 타임랩스 파일
     */
    @GetMapping("/timelapse")
    public ResponseEntity<ResponseFormat<TimelapseGetResponse>> getTimelapse(@RequestParam("meetingId") Long meetingId, HttpServletRequest request){
        TimelapseGetResponse response = productService.getTimelapse(jwtUtil.extractUserInfo(request), meetingId);
        return ResponseFormat.success(response).toEntity();
    }

    @GetMapping("/final-picture")
    public ResponseEntity<ResponseFormat<FinalPictureGetResponse>> getFinalPicture(@RequestParam("meetingId") Long meetingId, HttpServletRequest request) {
        UserInfoInAccessTokenDTO userInfo = jwtUtil.extractUserInfo(request);
        FinalPictureGetResponse response = productService.getFinalPicture(meetingId, userInfo);
        return ResponseFormat.success(response).toEntity();
    }

    private static boolean isFileExisted(MultipartFile[] multipartFiles) {
        return multipartFiles != null && multipartFiles.length != 0;
    }

}
