package com.dutaduta.sketchme.file.service;

import com.dutaduta.sketchme.file.constant.FileType;
import com.dutaduta.sketchme.file.dto.FileResponse;
import com.dutaduta.sketchme.file.dto.UploadResponse;
import com.dutaduta.sketchme.global.exception.BadRequestException;
import com.dutaduta.sketchme.global.exception.BusinessException;
import com.dutaduta.sketchme.global.exception.InternalServerErrorException;
import com.dutaduta.sketchme.product.domain.Picture;
import com.dutaduta.sketchme.product.service.ProductService;
import com.sun.jdi.InvalidTypeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
@Log4j2
public class FileService {

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    public static String removePrefixPath(String timelapsePath, String prefixPath) {
        return timelapsePath.replaceAll(prefixPath, "");
    }


    /**
     * 파일 업로드. 처음에 업로드할 때는 파일 타입이 무엇인지 지정해줘야 함
     *
     * @param uploadFiles
     * @param fileType    프로필, 타임랩스, 그림
     * @return
     */
    public UploadResponse uploadFile(MultipartFile uploadFile, FileType fileType, Long ID) {

        // 업로드할 파일이 없는 경우
        if (uploadFile.isEmpty()) {
            throw new BadRequestException("파일이 없습니다.");
        }

        // 확장자 검사 -> 이미지 파일만 업로드 가능하도록
        if (!uploadFile.getContentType().startsWith("image")) {
//            log.warn("이 파일은 image 타입이 아닙니다 ㅡ.ㅡ");
            throw new BadRequestException("이미지 파일이 아닙니다.");
        }

        String originalName = uploadFile.getOriginalFilename();
        String extension = originalName.substring(originalName.indexOf(".") + 1);

        // 파일타입 + 날짜 폴더 생성
        String folderPath = makeFolder(fileType);

        // UUID 적용해서 파일 이름 만들기 (고유한 파일 이름, 추후에 우리 서비스의 이름 지정 형식에 맞게 수정 필요)
        String saveName = uploadPath + File.separator + folderPath + File.separator + "o_" + ID + "." + extension;
        String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator + "s_" + ID + "." + extension;

        UploadResponse dto = null;

        // 파일 저장
        try {
            // 원본 이미지 저장
            Path savePath = Paths.get(saveName);
//            log.info("savePath : " + savePath);
            uploadFile.transferTo(savePath);

            // 썸네일 생성 및 저장
            File thumbnailfile = new File(thumbnailSaveName);

            // TODO: 썸네일 라이브러리 활용 코드 GIF 부분에 적용
            Thumbnailator.createThumbnail(savePath.toFile(), thumbnailfile, 100, 100);

            // 결과 반환할 리스트에도 담기
            dto = new UploadResponse(ID + "." + extension, folderPath, fileType);
//            log.info("dto imgURL : " + dto.getImageURL());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dto;
    } // uploadFile

    private String makeFolder(FileType fileType) {
        // 파일이 저장되는 시점의 시각을 가져와서 폴더 저장 경로 설정
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("/", File.separator);
        folderPath = fileType + File.separator + folderPath;  // folderPath가 파일 타입별로 달라짐

        // 폴더 만들기
        File uploadPathFolder = new File(uploadPath, folderPath);

        if (!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }

    public Boolean removeFile(String imgURL) {
        try {
            String srcFileName = URLDecoder.decode(imgURL, "UTF-8");
            String extension = imgURL.substring(imgURL.lastIndexOf(".") + 1);

            // 원본 파일 삭제
            File file = new File(uploadPath + File.separator + srcFileName);
            boolean result = file.delete();

            // 썸네일 삭제
            File thumbnail = new File(file.getParent() + File.separator + "s_" + file.getName().substring(2));
            result = thumbnail.delete();

            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 회원가입 시, 카카오에서 받은 프로필 이미지 url을 이용해서 우리 서버에 이미지 파일로 저장하는 과정
     *
     * @param imageUrl
     * @throws IOException
     */
    public UploadResponse saveImageUrl(String imageUrl, Long userID) {

        // 파일타입 + 날짜 폴더 생성
        String folderPath = makeFolder(FileType.PROFILEUSER);

        try {
            String extension = imageUrl.substring(imageUrl.lastIndexOf(".") + 1);
            String saveName = uploadPath + File.separator + folderPath + File.separator + "o_" + userID + "." + extension;

            // 카카오에서 준 url로 원본 프로필 이미지 저장하기
            URL url = new URL(imageUrl);
            InputStream in = url.openStream();
            OutputStream out = new FileOutputStream(saveName); //저장경로

            while (true) {
                //이미지를 읽어온다.
                int data = in.read();
                if (data == -1) {
                    break;
                }
                //이미지를 쓴다.
                out.write(data);

            }

            in.close();
            out.close();

            // 썸네일 생성 및 저장
            Path savePath = Paths.get(saveName);
            String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator + "s_" + userID + "." + extension;
            File thumbnailfile = new File(thumbnailSaveName);
            Thumbnailator.createThumbnail(savePath.toFile(), thumbnailfile, 100, 100);

            // 반환값 준비
            UploadResponse dto = new UploadResponse(userID + "." + extension, folderPath, FileType.PROFILEUSER);
//            log.info("ImageURL  :  " + dto.getImageURL());

            return dto;

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException();
        }
    }

    public FileResponse getFile(String imgURL) throws IOException {
        String srcFileName = URLDecoder.decode(imgURL, UTF_8);
        String fileName = String.format("%s/%s",uploadPath,srcFileName);
        File file = new File(fileName);
        HttpHeaders header = new HttpHeaders();

        // MIME 타입 처리 (파일 확장자에 따라 브라우저에 전송하는 MIME 타입이 달라져야 함)
        header.add("Content-Type", Files.probeContentType(file.toPath()));
        return new FileResponse(file, header);
    }

    public FileResponse downloadFile(String userAgent, String imgURL) throws UnsupportedEncodingException {
        // srcFileName은 파일타입 + 폴더경로(=날짜) + 파일 이름 으로 구성됨
        String srcFileName = URLDecoder.decode(imgURL, "UTF-8");
        String fileName = String.format("%s%s%s",uploadPath,File.separator,srcFileName);
        File file = new File(fileName);

        // 찾는 파일이 없는 경우
        if (!file.exists()) {
            throw new BadRequestException("파일이 없습니다.");
        }

        // 다운로드 할 때 저장되는 이미지 이름 커스텀
        // (예시 : 🎨SketchMe🎨_작품🖼_[작품생성날짜].[확장자])
        String downloadName = "🎨SketchMe🎨_";
        if (srcFileName.contains(FileType.PICTURE.toString())) {
            downloadName += "작품🖼_";
        } else if (srcFileName.contains(FileType.TIMELAPSE.toString())) {
            downloadName += "타임랩스🎞_";
        } else if (srcFileName.contains(FileType.PROFILEARTIST.toString())) {
            downloadName += "작가프로필✍_";
        } else if (srcFileName.contains(FileType.PROFILEUSER.toString())) {
            downloadName += "사용자프로필😊_";
        }
        String[] filenameArr = srcFileName.split(getSeparatorRegex());
        downloadName += (filenameArr[1] + filenameArr[2] + filenameArr[3]); // 생성날짜 (폴더구조에서 가져옴)
        downloadName += ("." + srcFileName.substring(srcFileName.lastIndexOf(".") + 1)); // 확장자

        // IE 브라우저에서 제목에 한글이 들어간 파일이 제대로 다운로드되지 않는 문제를 해결하기 위해 IE인 경우 별도의 처리를 해줌
        if (userAgent.contains("Trident")) {
//            log.info("IE browser");
            downloadName = URLEncoder.encode(downloadName, "UTF-8").replaceAll("\\+", " ");
        } else if (userAgent.contains("Edge")) {
//            log.info("Edge browser");
            downloadName = URLEncoder.encode(downloadName, "UTF-8");
        } else {
//            log.info("Chrome browser");
            downloadName = new String(downloadName.getBytes("UTF-8"), "ISO-8859-1");
        }

        // 다운로드 할 때 저장되는 이름 지정
        // 파일 이름이 한글인 경우 저장할 때 깨지는 문제를 막기 위해 파일 이름에 대해 문자열 처리를 해줌
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Disposition", "attachment; filename=" + downloadName);

        return new FileResponse(file, header);
    }

    private static String getSeparatorRegex() {
        String separator = "";
        if(File.separatorChar=='\\'){
            separator = "\\\\";
        } else{
            separator = "/";
        }
        return separator;
    }

    public void checkImageIsPNG(MultipartFile multipartFile) {
        if(!"image/png".equals(multipartFile.getContentType())){
            throw new BadRequestException("PNG 파일 형식의 이미지를 보내주세요. 다른 파일 형식은 허용하지 않습니다.");
        }
    }

    public File getDir(String path) {
        File dir =  new File(path);
        if(!dir.exists()){
            dir.mkdirs();
        }
        return dir;
    }

    public File getDir(LocalDateTime now, String prefix) {
        int year= now.getYear();
        int month= now.getMonthValue();
        int day= now.getDayOfMonth();
        File dir =  new File(String.format("%s/%d/%d/%d", prefix, year, month, day));
        if(!dir.exists()){
            dir.mkdirs();
        }
        return dir;
    }

    public File getOrigImagePath(Picture picture, File pictureDir) {
        File picturePath = new File(String.format("%s/o_%d.png", pictureDir.getPath(), picture.getId()));
        return picturePath;
    }

    public void saveMultipartFile(MultipartFile multipartFile, File outputPath, String whatImageIs) {
        try {
            multipartFile.transferTo(outputPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerErrorException(String.format("'%s' 를 저장할 수 없습니다.",whatImageIs));
        }
    }

    public File getThumbnailPath(Picture picture, File pictureDir) {
        return new File(String.format("%s/s_%d.png", pictureDir.getPath(), picture.getId()));
    }

    public void makeThumbnail(File picturePath, File thumbnailPath) {
        try {
            Thumbnailator.createThumbnail(picturePath, thumbnailPath, ProductService.THUMBNAIL_WIDTH, ProductService.THUMBNAIL_HEGITH);
        } catch (IOException e) {
            throw new InternalServerErrorException("썸네일을 저장할 수 없습니다.");
        }
    }

    public File getFile(String path, String whatIsFile) {
        File file = new File(path);
        if(file.exists()){
            throw new BadRequestException(String.format("'%s' 파일이 존재하지 않습니다.",whatIsFile));
        }
        return file;
    }
}
