package com.dutaduta.sketchme.common.dto;

import com.dutaduta.sketchme.common.domain.Category;
import com.dutaduta.sketchme.product.dto.PictureResponse;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class CategoryResponse {

    @NotNull
    private Long categoryID;

    private String name;

    private String description;

    private Long price;

    @NotNull
    private boolean isOpen;

    private LocalDateTime createdDateTime;

    private LocalDateTime updatedDateTime;

    private List<PictureResponse> drawings;

    private List<HashtagResponse> hashtags;

    public static CategoryResponse of(Category category, List<PictureResponse> drawings, List<HashtagResponse> hashtags) {
        return CategoryResponse.builder()
                .categoryID(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .price(category.getApproximatePrice())
                .isOpen(category.isOpen())
                .createdDateTime(category.getCreatedDateTime())
                .updatedDateTime(category.getUpdatedDateTime())
                .drawings(drawings)
                .hashtags(hashtags).build();
    }
}
