package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

// 업로드 결과 처리용 DTO
@Data
@AllArgsConstructor
public class UploadResultDTO implements Serializable {
    private String fileName;
    private String uuid;
    private String folderPath;

    // 실제 파일과 관련된 모든 정보를 가지는데 나중에 전체 경로가 필요한 경우를 대비
    public String getImageURL() {
        try {
            return URLEncoder.encode(folderPath + "/" + uuid + "_" + fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getThumbnailURL(){
        try {
            // encode는 파일 이름에 공백이 있거나 한글이 섞여 있는 경우를 대비하기 위함
            return URLEncoder.encode(folderPath + "/s_" +uuid + "_" +fileName,"UTF-8");
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }
}



