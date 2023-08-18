package com.dutaduta.sketchme.product.service.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FinalPictureGetResponse {
    String pictureUri;
    String thumbnailUri;
}
