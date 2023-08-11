package com.dutaduta.sketchme.common.service;

import com.dutaduta.sketchme.common.dao.CategoryHashtagRepository;
import com.dutaduta.sketchme.common.dao.CategoryRepository;
import com.dutaduta.sketchme.common.dao.HashtagRepository;
import com.dutaduta.sketchme.common.domain.Category;
import com.dutaduta.sketchme.common.domain.CategoryHashtag;
import com.dutaduta.sketchme.common.domain.Hashtag;
import com.dutaduta.sketchme.common.dto.CategoryRequest;
import com.dutaduta.sketchme.common.dto.CategoryResponse;
import com.dutaduta.sketchme.common.dto.HashtagResponse;
import com.dutaduta.sketchme.file.dto.ImgUrlResponse;
import com.dutaduta.sketchme.global.exception.ForbiddenException;
import com.dutaduta.sketchme.global.exception.NotFoundException;
import com.dutaduta.sketchme.member.dao.ArtistRepository;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.product.dao.PictureRepository;
import com.dutaduta.sketchme.product.domain.Picture;
import com.dutaduta.sketchme.product.dto.PictureResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final ArtistRepository artistRepository;

    private final CategoryHashtagRepository categoryHashtagRepository;

    private final HashtagRepository hashtagRepository;

    private final PictureRepository pictureRepository;

    public void registCategory(CategoryRequest categoryRequest, Long artistID) {
        Artist artist = artistRepository.getReferenceById(artistID);
        Category category = Category.createCategory(categoryRequest, artist);
        categoryRepository.save(category);
        for (Long hashtagID : categoryRequest.getHashtags()) {
            Hashtag hashtag = hashtagRepository.findById(hashtagID).orElseThrow(() -> new NotFoundException("존재하지 않는 해시태그입니다."));
            categoryHashtagRepository.save(CategoryHashtag.of(category, hashtag));
        }
    }


    public void modifyCategory(CategoryRequest categoryRequest, Long artistID) {
        // 본인이 아니면 카테고리 수정할 수 없도록
        Category category = categoryRepository.findById(categoryRequest.getCategoryID()).orElseThrow(() -> new NotFoundException("해당 카테고리 정보가 없습니다."));
        if(category.isDeleted()) throw new NotFoundException("삭제된 카테고리입니다.");
        if(!Objects.equals(category.getArtist().getId(),artistID)) throw new ForbiddenException("본인만 카테고리를 수정할 수 있습니다.");

        category.updateCategory(categoryRequest);

        for (Long hashtagID : categoryRequest.getHashtags()) {
            Hashtag hashtag = hashtagRepository.findById(hashtagID).orElseThrow(() -> new NotFoundException("존재하지 않는 해시태그입니다."));
            // 중복되지 않는 해시태그들만 추가해주기
            if (categoryHashtagRepository.findByCategoryAndHashtag(category, hashtag) == null){
                categoryHashtagRepository.save(CategoryHashtag.of(category, hashtag));
            }
        }
    }

    public void deleteCategory(Long categoryID, Long artistID) {
        // 본인이 아니면 카테고리 삭제할 수 없도록
        Category category = categoryRepository.findById(categoryID).orElseThrow(() -> new NotFoundException("해당 카테고리 정보가 없습니다."));
        if(category.isDeleted()) throw new NotFoundException("삭제된 카테고리입니다.");
        if(!Objects.equals(category.getArtist().getId(),artistID)) throw new ForbiddenException("본인만 카테고리를 삭제할 수 있습니다.");

        category.deleteCategory();
    }

    public void changeCategoryIsOpen(Long categoryID, Long artistID, Boolean isOpen) {
        // 본인이 아니면 카테고리 공개여부 바꿀 수 없도록
        Category category= categoryRepository.findById(categoryID).orElseThrow(() -> new NotFoundException("해당 카테고리 정보가 없습니다."));
        if(category.isDeleted()) throw new NotFoundException("삭제된 카테고리입니다.");
        if(!Objects.equals(category.getArtist().getId(),artistID)) throw new ForbiddenException("본인만 카테고리 공개 여부를 수정할 수 있습니다.");

        category.updateIsOpen(isOpen);
    }

    public List<CategoryResponse> selectArtistCategories(Long categoryArtistID, Long loginArtistID) {

        Artist categoryArtist = artistRepository.findById(categoryArtistID).orElseThrow(() -> new NotFoundException("탈퇴한 작가입니다."));

        List<CategoryResponse> result = new ArrayList<>();
        List<Category> categories = null;

        // 현재 로그인한 사람이 카테고리들의 주인이라면 비공개 카테고리까지 전부 반환
        if(Objects.equals(categoryArtistID, loginArtistID)) {
            categories = categoryRepository.findByArtistAndIsDeleted(categoryArtist, false);
        }
        // 현재 로그인한 사람이 카테고리들의 주인이 아니라면 공개 카테고리만 반환
        if(!Objects.equals(categoryArtistID, loginArtistID)) {
            categories = categoryRepository.findByArtistAndIsDeletedAndIsOpen(categoryArtist, false, true);
        }

        for(Category category : categories) {
            // 삭제된거, 비공개인 그림 제외하고 반환
            List<PictureResponseDTO> drawings = new ArrayList<>();
            List<Picture> pictures = pictureRepository.findByIsDeletedAndIsOpenAndCategory(false, true, category);
            for(Picture picture : pictures) {
                drawings.add(PictureResponseDTO.of(picture, ImgUrlResponse.of(picture)));
            }

            // 해당 카테고리의 해시태그들
            List<HashtagResponse> hashtags = new ArrayList<>();
            for(CategoryHashtag categoryHashtag : categoryHashtagRepository.findByCategory(category)) {
                hashtags.add(HashtagResponse.of(categoryHashtag.getHashtag()));
            }

            result.add(CategoryResponse.of(category, drawings, hashtags));
        }

        return result;
    }
}
