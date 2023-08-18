package com.dutaduta.sketchme.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class CategoryRequest {

    private Long categoryID;

    @NotNull
    private String name;

    @NotNull
    private String description;

    private Long approximatePrice;

    private Long[] hashtags;

    private List<Long> remainingPictureIDs;

}
