package com.dutaduta.sketchme.common.dao;

import com.dutaduta.sketchme.common.domain.Category;
import com.dutaduta.sketchme.member.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByArtistAndIsDeleted(Artist artist, Boolean isDeleted);

    List<Category> findByArtistAndIsDeletedAndIsOpen(Artist artist, Boolean isDeleted, Boolean isOpen);

    Category findTopByArtistAndIsDeletedAndIsOpenOrderByApproximatePrice(Artist artist, Boolean isDeleted, Boolean isOpen);
}
