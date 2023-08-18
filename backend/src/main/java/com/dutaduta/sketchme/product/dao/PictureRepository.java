package com.dutaduta.sketchme.product.dao;

import com.dutaduta.sketchme.common.domain.Category;
import com.dutaduta.sketchme.meeting.domain.Meeting;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.domain.User;
import com.dutaduta.sketchme.product.domain.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {


    List<Picture> findByArtistAndIsDeleted(Artist artist, Boolean isDeleted);

    List<Picture> findByIsDeletedAndIsOpenAndCategory(Boolean isDeleted, Boolean isOpen, Category category);

    List<Picture> findByUser(User user);

    List<Picture> findByIsDeletedAndCategory(Boolean isDeleted, Category category);

    // 검색어로 그림 검색 (정렬은 service 단에서 진행)
    @Query("SELECT p FROM Picture p " +
            "LEFT JOIN p.category c WHERE p.isDeleted=false AND p.isOpen=true AND c.isOpen = true " +
            "AND p.artist.isOpen = true AND p.artist.isDeactivated = false " +
            "AND c.name LIKE %:keyword% ORDER BY p.updatedDateTime DESC")
    List<Picture> searchPicturesByKeyword(@Param("keyword") String keyword);

    Picture findByMeeting(Meeting meeting);

}