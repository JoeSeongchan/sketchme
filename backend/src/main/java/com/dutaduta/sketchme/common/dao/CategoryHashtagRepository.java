package com.dutaduta.sketchme.common.dao;

import com.dutaduta.sketchme.common.domain.Category;
import com.dutaduta.sketchme.common.domain.CategoryHashtag;
import com.dutaduta.sketchme.common.domain.Hashtag;
import com.dutaduta.sketchme.member.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryHashtagRepository extends JpaRepository<CategoryHashtag, Long> {

    CategoryHashtag findByCategoryAndHashtag(Category category, Hashtag hashtag);

    List<CategoryHashtag> findByCategory(Category category);
}
