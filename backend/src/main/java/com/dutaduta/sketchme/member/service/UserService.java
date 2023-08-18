package com.dutaduta.sketchme.member.service;

import com.dutaduta.sketchme.common.dao.CategoryRepository;
import com.dutaduta.sketchme.common.domain.Category;
import com.dutaduta.sketchme.common.dto.HashtagResponse;
import com.dutaduta.sketchme.file.constant.FileType;
import com.dutaduta.sketchme.file.dto.ImgUrlResponse;
import com.dutaduta.sketchme.file.dto.UploadResponse;
import com.dutaduta.sketchme.file.service.FileService;
import com.dutaduta.sketchme.global.exception.BadRequestException;
import com.dutaduta.sketchme.global.exception.BusinessException;
import com.dutaduta.sketchme.global.exception.NotFoundException;
import com.dutaduta.sketchme.member.dao.ArtistRepository;
import com.dutaduta.sketchme.member.dao.FavoriteArtistRepository;
import com.dutaduta.sketchme.member.dao.UserRepository;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.domain.ArtistHashtag;
import com.dutaduta.sketchme.member.domain.FavoriteArtist;
import com.dutaduta.sketchme.member.domain.User;
import com.dutaduta.sketchme.member.dto.ArtistResponse;
import com.dutaduta.sketchme.member.dto.MemberInfoResponse;
import com.dutaduta.sketchme.review.dao.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class UserService {
    //@Autowired 사용 지양됨 -> @RequiredArgsConstructor 로 생성되는 생성자로 주입받기 위해 final 붙임.
    private final UserRepository userRepository;

    private final ArtistRepository artistRepository;

    private final FileService fileService;

    private final FavoriteArtistRepository favoriteArtistRepository;

    private final CategoryRepository categoryRepository;

    private final ReviewRepository reviewRepository;

    public MemberInfoResponse getUserInfo(String member, Long userId, Long artistId) throws BusinessException {
        // 일반 사용자인 경우
        if(member.equals("user")) {
            User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));
            log.info("user : " + user.toString());
            return new MemberInfoResponse(user,userId);
        }

        // 작가인 경우
        Artist artist = artistRepository.findById(artistId).orElseThrow(() -> new BadRequestException("존재하지 않는 작가입니다."));
        return new MemberInfoResponse(artist,artistId);
    }

    public boolean checkNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public void modifyUserInformation(String nickname, Long userId) {
        log.info(nickname);
        User user = userRepository.findById(userId).orElseThrow(()->new BadRequestException("존재하지 않는 사용자입니다."));
        user.updateNickname(nickname);
    }

    public ImgUrlResponse updateProfileImage(MultipartFile uploadFile, String member, Long userId, Long artistId) {
        Long ID;
        FileType fileType;

        String profileImgUrl;
        String profileThumbnailUrl;

        if(member.equals("user")) {
            ID = userId;
            fileType = FileType.PROFILEUSER;
            // 서버에 저장되어 있는 기존 이미지 삭제 (원본, 썸네일 둘 다 삭제)
            User user = userRepository.getReferenceById(ID);
            profileImgUrl = user.getProfileImgUrl();
            fileService.removeFile(profileImgUrl);
            // 새로운 이미지 저장
            UploadResponse dto = fileService.uploadFile(uploadFile, fileType, ID);
            profileImgUrl = dto.getImageURL();
            profileThumbnailUrl = dto.getThumbnailURL();
            // DB 정보도 갱신해주기 (파일 이름이 같아도, 날짜가 다르면 폴더 경로가 달라지면서 url이 달라짐)
            user.updateImgUrl(profileImgUrl, profileThumbnailUrl);
        } else {
            ID = artistId;
            fileType = FileType.PROFILEARTIST;
            // 서버에 저장되어 있는 기존 이미지 삭제
            Artist artist = artistRepository.getReferenceById(ID);
            profileImgUrl = artist.getProfileImgUrl();
            fileService.removeFile(profileImgUrl);
            // 새로운 이미지 저장
            UploadResponse dto = fileService.uploadFile(uploadFile, fileType, ID);
            profileImgUrl = dto.getImageURL();
            profileThumbnailUrl = dto.getThumbnailURL();
            // DB 정보도 갱신해주기
            artist.updateImgUrl(profileImgUrl, profileThumbnailUrl);
        }
        return new ImgUrlResponse(profileImgUrl, profileThumbnailUrl);
    }


    public void registFavoriteArtist(Long artistID, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("회원 정보가 존재하지 않습니다."));
        Artist artist = artistRepository.findById(artistID).orElseThrow(() -> new NotFoundException("작가 정보가 존재하지 않습니다."));

        // 기존에 관심작가로 등록되어 있는 작가
        if(favoriteArtistRepository.existsByUserIdAndArtist_Id(userId, artistID)) throw new BadRequestException("이미 관심 작가로 등록된 작가입니다.");

        // 새롭게 관심작가로 등록
        FavoriteArtist favoriteArtist = FavoriteArtist.builder()
                .user(user).artist(artist).build();
        favoriteArtistRepository.save(favoriteArtist);
    }

    public List<ArtistResponse> seeFavoriteArtist(Long userId) {
        List<ArtistResponse> result = new ArrayList<>();

        // 현재 로그인한 유저의(userId) 관심 작가 목록을 가져온다. (favorite_artist 테이블과 artist 테이블 조인)
        List<FavoriteArtist> favoriteArtists = favoriteArtistRepository.findByUser_Id(userId);

        // 반환을 위한 데이터 가공
        for(FavoriteArtist favoriteArtist : favoriteArtists) {
            // 해시태그 반환 형식 가공
            List<HashtagResponse> hashtags = new ArrayList<>();

            if(favoriteArtist.getArtist().getArtistHashtagList() != null) {
                for (ArtistHashtag hashtag : favoriteArtist.getArtist().getArtistHashtagList()) {
                    hashtags.add(HashtagResponse.of(hashtag.getHashtag()));
                }
            }

            // 해당 작가의 카테고리 최소 가격 구하기
            Long minPrice = null;
            Category category = categoryRepository.findTopByArtistAndIsDeletedAndIsOpenOrderByApproximatePrice(favoriteArtist.getArtist(), false, true);
            // 카테고리가 없는 경우
            if(category != null){
                minPrice = category.getApproximatePrice();
            }

            // 별점
            BigDecimal avgRating = reviewRepository.calculateAvgRating(favoriteArtist.getArtist());

            // 결과에 넣기
            result.add(ArtistResponse.of(favoriteArtist.getArtist(), hashtags, minPrice, avgRating, true));
        }

        return result;
    }


    public void deleteFavoriteArtist(Long artistID, Long userId) {
        FavoriteArtist favoriteArtist = favoriteArtistRepository.findByUser_IdAndArtist_Id(userId, artistID);
        if(favoriteArtist == null) throw new BadRequestException("관심작가로 등록되지 않은 작가입니다.");
        favoriteArtistRepository.delete(favoriteArtist);
    }

    public boolean toggleFavoriteArtist(Long artistId, Long userId) {
        // 기존에 관심작가로 등록되어 있는 경우 -> 삭제한다.
        if(favoriteArtistRepository.existsByUserIdAndArtist_Id(userId, artistId)){
            deleteFavoriteArtist(artistId, userId);
            return true;
        }

        // 기존에 관심작가로 등록되어 있지 않은 경우 -> 등록한다.
        registFavoriteArtist(artistId, userId);
        return false;
    }


}
