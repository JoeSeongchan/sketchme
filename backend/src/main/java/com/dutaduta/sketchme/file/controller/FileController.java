package com.dutaduta.sketchme.file.controller;

import com.dutaduta.sketchme.file.dto.FileResponse;
import com.dutaduta.sketchme.file.service.FileService;
import com.dutaduta.sketchme.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Log4j2
public class FileController {

    private final FileService fileService;

    /**
     * 이미지 파일을 화면에 출력하기 위해 사용하는 API
     * @param imgURL
     * @return
     */
    @GetMapping("/display")
    public ResponseEntity<?> getFile(@RequestParam String imgURL) {
        try {
            System.out.println("imgURL = " + imgURL);
            FileResponse fileResponse = fileService.getFile(imgURL);
            // 파일 데이터 처리
            return new ResponseEntity<>(FileCopyUtils.copyToByteArray(fileResponse.getFile()), fileResponse.getHeader(), HttpStatus.OK);
//            return ResponseFormat.success(FileCopyUtils.copyToByteArray(file)).toEntity(header);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BadRequestException("파일을 찾을 수 없습니다.");
        }
    }


    // MIME 타입은 다운로드가 가능한 application/octet-stream으로 지정
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> downloadFile(@RequestHeader("User-Agent") String userAgent, String imgURL) {

        try {
            FileResponse fileResponse = fileService.downloadFile(userAgent, imgURL);
            // 파일 데이터 처리
            return new ResponseEntity<>(FileCopyUtils.copyToByteArray(fileResponse.getFile()), fileResponse.getHeader(), HttpStatus.OK);
//            return ResponseFormat.success(FileCopyUtils.copyToByteArray(file)).toEntity(header); // 이렇게 하면 될 것 같은데.. 안됨..
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BadRequestException("파일을 찾을 수 없습니다.");
        }
    }

}
