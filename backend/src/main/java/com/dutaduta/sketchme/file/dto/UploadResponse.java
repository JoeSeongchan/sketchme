package com.dutaduta.sketchme.file.dto;


import com.dutaduta.sketchme.file.constant.FileType;
import lombok.*;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class UploadResponse implements Serializable {

    private String fileName;
    private String folderPath;
    private FileType fileType;

    /**
     * 전체 경로가 필요한 경우 사용
     * @return
     */
    public String getImageURL() {
        try {
            return URLEncoder.encode(folderPath + File.separator + "o_"+fileName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 썸네일의 전체 경로가 필요한 경우 사용
     * @return
     */
    public String getThumbnailURL() {
        try {
            return URLEncoder.encode(folderPath + File.separator + "s_" + fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
