package com.dutaduta.sketchme.file.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.http.HttpHeaders;

import java.io.File;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class FileResponse {
    @NotNull
    private File file;

    @NotNull
    private HttpHeaders header;
}
