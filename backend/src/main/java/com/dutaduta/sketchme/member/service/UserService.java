package com.dutaduta.sketchme.member.service;

import com.dutaduta.sketchme.file.constant.FileType;
import com.dutaduta.sketchme.file.dto.ImgUrlResponse;
import com.dutaduta.sketchme.file.dto.UploadResponse;
import com.dutaduta.sketchme.file.service.FileService;
import com.dutaduta.sketchme.global.exception.BadRequestException;
import com.dutaduta.sketchme.global.exception.BusinessException;
import com.dutaduta.sketchme.member.dao.ArtistRepository;
import com.dutaduta.sketchme.member.dao.UserRepository;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.domain.User;
import com.dutaduta.sketchme.member.dto.MemberInfoResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {
    //@Autowired 사용 지양됨 -> @RequiredArgsConstructor 로 생성되는 생성자로 주입받기 위해 final 붙임.
    private final UserRepository userRepository;

    private final ArtistRepository artistRepository;

    private final FileService fileService;

    @Transactional // db 트랜잭션 자동으로 commit
    public MemberInfoResponse getUserInfo(String member, Long userId, Long artistId) throws BusinessException {
        // 일반 사용자인 경우
        if(member.equals("user")) {
            User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));
            log.info("user : " + user.toString());
            // TODO: 수정 필요
            return new MemberInfoResponse(user,0L);
        }

        // 작가인 경우
        Artist artist = artistRepository.findById(artistId).orElseThrow(() -> new BadRequestException("존재하지 않는 작가입니다."));
        // TODO: 수정 필요
        return new MemberInfoResponse(artist,0L);
    }

    @Transactional
    public boolean checkNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Transactional
    public void modifyUserInformation(String nickname, Long userId) {
        log.info(nickname);
        User user = userRepository.findById(userId).orElseThrow(()->new BadRequestException("존재하지 않는 사용자입니다."));
        user.updateNickname(nickname);
    }

    @Transactional
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
}
