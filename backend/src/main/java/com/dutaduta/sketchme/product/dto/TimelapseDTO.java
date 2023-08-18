package com.dutaduta.sketchme.product.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TimelapseDTO {
    private String timelapsePath;
    private String thumbnailPath;
}
