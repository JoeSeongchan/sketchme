package com.dutaduta.sketchme.product.service;

import com.dutaduta.sketchme.common.dao.CategoryHashtagRepository;
import com.dutaduta.sketchme.common.dao.CategoryRepository;
import com.dutaduta.sketchme.common.domain.Category;
import com.dutaduta.sketchme.common.domain.CategoryHashtag;
import com.dutaduta.sketchme.common.dto.HashtagResponse;
import com.dutaduta.sketchme.file.constant.FileType;
import com.dutaduta.sketchme.file.dto.ImgUrlResponse;
import com.dutaduta.sketchme.file.dto.UploadResponse;
import com.dutaduta.sketchme.file.service.FileService;
import com.dutaduta.sketchme.global.exception.BadRequestException;
import com.dutaduta.sketchme.global.exception.BusinessException;
import com.dutaduta.sketchme.global.exception.NotFoundException;
import com.dutaduta.sketchme.global.exception.UnauthorizedException;
import com.dutaduta.sketchme.member.dao.ArtistRepository;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.oidc.dto.UserInfoInAccessTokenDTO;
import com.dutaduta.sketchme.product.dao.PictureRepository;
import com.dutaduta.sketchme.product.domain.Picture;
import com.dutaduta.sketchme.product.dto.PictureResponseDTO;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class ProductService {

    private final FileService fileService;

    private final PictureRepository pictureRepository;

    private final ArtistRepository artistRepository;

    private final CategoryRepository categoryRepository;

    private final CategoryHashtagRepository categoryHashtagRepository;

    public List<ImgUrlResponse> registDrawingsOfArtist(MultipartFile[] uploadFiles, Long artistID) {

        Artist artist = artistRepository.getReferenceById(artistID);
        if(artist.isDeactivated()) throw new BadRequestException("탈퇴한 작가입니다.");

        List<ImgUrlResponse> result = new ArrayList<>();

        for(MultipartFile uploadFile : uploadFiles) {
            // 일단 DB에 picture 저장(이미지 url 없는 상태로)
            // 외부그림, 비공개, 삭제안됨 여부는 전부 default값으로 하면 되니까 별도 설정 필요없음
            Picture picture = Picture.builder().artist(artist).build();
            Long pictureID = pictureRepository.save(picture).getId();

            // picture id 받아와서 서버에 실제 이미지 파일 저장
            UploadResponse uploadResponse = fileService.uploadFile(uploadFile, FileType.PICTURE, pictureID);
            result.add(ImgUrlResponse.of(uploadResponse));

            // DB에 저장했던 picture에 이미지 url 추가
            picture.updateImgUrl(uploadResponse.getImageURL(), uploadResponse.getThumbnailURL());
        } // for

        return result;
    }

    public void deleteDrawingOfArtist(Long pictureID, Long artistID) {
        Artist artist = artistRepository.getReferenceById(artistID);
        if(artist.isDeactivated()) throw new BadRequestException("탈퇴한 작가입니다.");

        Picture picture = pictureRepository.findById(pictureID).orElseThrow(() -> new BusinessException());
        if(!Objects.equals(picture.getArtist().getId(), artistID)) throw new BadRequestException("잘못된 요청입니다");

        if(picture.isDeleted()) throw new BusinessException();

        // 실제 이미지 파일 삭제
        fileService.removeFile(picture.getUrl());

        // DB에서 정보 삭제
        picture.updateIsDeleted(true);
    }

    public List<PictureResponseDTO> selectDrawingsOfArtist(Long artistID) {

        Artist artist = artistRepository.getReferenceById(artistID);
        if(artist.isDeactivated()) throw new BadRequestException("탈퇴한 작가입니다.");

        List<PictureResponseDTO> result = new ArrayList<>();

        // 작가가 내가 소유한 그림들을 본다.
        // 공개, 비공개 여부도 함께 반환해야 할 듯! 작가가 자신의 그림을 확인할 수는 있어도, 그걸 카테고리에 추가하는건 안됨
        List<Picture> pictures = pictureRepository.findByArtistAndIsDeleted(artist, false);
        for(Picture picture : pictures) {
            ImgUrlResponse imgUrlResponse = ImgUrlResponse.of(picture);
            PictureResponseDTO pictureResponseDTO = PictureResponseDTO.of(picture, imgUrlResponse);
            result.add(pictureResponseDTO);
        }

        return result;
    }

    public String saveLivePicture(UserInfoInAccessTokenDTO userInfo, Long id, LocalDateTime of, MultipartFile multipartFile) {
        return null;
    }

    public List<PictureResponseDTO> searchPictures() {
        List<PictureResponseDTO> result = new ArrayList<>();

        // 비공개인 그림들을 제외하고 모든 그림을 반환한다.
        List<Picture> pictures = pictureRepository.findByIsDeletedAndIsOpen(false, true);

        // 반환을 위한 데이터 가공
        for(Picture picture : pictures) {
            // 그림이 속해 있는 카테고리의 해시태그들을 반환해줘야 함
            Category category = picture.getCategory();
            // 해당 카테고리의 해시태그들
            List<HashtagResponse> hashtags = new ArrayList<>();
            for(CategoryHashtag categoryHashtag : categoryHashtagRepository.findByCategory(category)) {
                hashtags.add(HashtagResponse.of(categoryHashtag.getHashtag()));
            }
            result.add(PictureResponseDTO.of(picture, ImgUrlResponse.of(picture),hashtags));
        }

        return result;
    }

    public List<ImgUrlResponse> registDrawingsOfCategory(MultipartFile[] uploadFiles, Long categoryID, Long artistID) {
        Artist artist = artistRepository.getReferenceById(artistID);
        if(artist.isDeactivated()) throw new BadRequestException("탈퇴한 작가입니다.");

        Category category = categoryRepository.findById(categoryID).orElseThrow(() -> new NotFoundException("카테고리 정보가 없습니다."));
        log.info(artistID);
        log.info(category.getArtist().getId());
        if(category.getArtist().getId() != artistID) throw new UnauthorizedException("카테고리 주인만 그림을 등록할 수 있습니다.");

        List<ImgUrlResponse> result = new ArrayList<>();

        for(MultipartFile uploadFile : uploadFiles) {
            // 일단 DB에 picture 저장(이미지 url 없는 상태로)
            // 외부그림, 비공개, 삭제안됨 여부는 전부 default값으로 하면 되니까 별도 설정 필요없음
            Picture picture = Picture.builder().artist(artist).category(category).build();
            picture.updateIsOpen(true);
            Long pictureID = pictureRepository.save(picture).getId();

            // picture id 받아와서 서버에 실제 이미지 파일 저장
            UploadResponse uploadResponse = fileService.uploadFile(uploadFile, FileType.PICTURE, pictureID);
            result.add(ImgUrlResponse.of(uploadResponse));

            // DB에 저장했던 picture에 이미지 url 추가
            picture.updateImgUrl(uploadResponse.getImageURL(), uploadResponse.getThumbnailURL());
        } // for

        return result;
    }
}
