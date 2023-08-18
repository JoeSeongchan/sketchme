package com.dutaduta.sketchme.member.dto;

import com.dutaduta.sketchme.common.dto.HashtagResponse;
import com.dutaduta.sketchme.file.dto.ImgUrlResponse;
import com.dutaduta.sketchme.member.domain.Artist;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class ArtistResponse {

    @NotNull
    private Long id;

    @NotNull
    private ImgUrlResponse imgUrlResponse;

    private String description;

    private String writer;

    @NotNull
    private boolean isOpen;

    private List<HashtagResponse> hashtags;

    private Long price;

    private BigDecimal rating;

    private Long userID;

    private boolean like;

    public static ArtistResponse of(Artist artist,ImgUrlResponse imgUrlResponse) {
        return ArtistResponse.builder()
                .id(artist.getId())
                .writer(artist.getNickname())
                .imgUrlResponse(imgUrlResponse)
                .description(artist.getDescription())
                .isOpen(artist.isOpen()).build();

    }

    public static ArtistResponse of(Artist artist, List<HashtagResponse> hashtags, Long minPrice, BigDecimal avgRating) {
        return ArtistResponse.builder()
                .id(artist.getId())
                .writer(artist.getNickname())
                .imgUrlResponse(ImgUrlResponse.builder().imgUrl(artist.getProfileImgUrl()).thumbnailUrl(artist.getProfileThumbnailImgUrl()).build())
                .description(artist.getDescription())
                .isOpen(artist.isOpen())
                .hashtags(hashtags)
                .price(minPrice)
                .rating(avgRating)
                .userID(artist.getUser().getId()).build();
    }

    public static ArtistResponse of(Artist artist, List<HashtagResponse> hashtags, Long minPrice, BigDecimal avgRating, boolean isLiked) {
        return ArtistResponse.builder()
                .id(artist.getId())
                .writer(artist.getNickname())
                .imgUrlResponse(ImgUrlResponse.builder().imgUrl(artist.getProfileImgUrl()).thumbnailUrl(artist.getProfileThumbnailImgUrl()).build())
                .description(artist.getDescription())
                .isOpen(artist.isOpen())
                .hashtags(hashtags)
                .price(minPrice)
                .rating(avgRating)
                .userID(artist.getUser().getId())
                .like(isLiked).build();
    }
}
