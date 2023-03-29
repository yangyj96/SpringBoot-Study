package org.zerock.mreview.controller;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mreview.dto.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {

    @Value("${org.zerock.upload.path}")             // application.properties의 변수
    private String uploadPath;

    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles){
    // MultipartFile은 단건만 배열로 설정하면 다수의 파일을 받을 수 있음
    // 배열을 활용하면 동시에 여러개의 파일 정보를 처리할 수 있으므로 화면에서 여러개의 파일을 동시에 업로드 할 수 있음

        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for (MultipartFile uploadFile: uploadFiles) {
            //브라우저에 따라 업로드하는 파일의 이름은 전체경로일 수도 있고(Internet Explorer)
            //단순히 파일의 이름만을 의미할 수도 있습니다.(chrome browser)

            // 이미지 파일만 업로드 가능
            if(uploadFile.getContentType().startsWith("image") == false) {
                log.warn("this file is not image type");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            //실제 파일 이름 IE나 Edge는 전체 경로가 들어오므로
            String originalName = uploadFile.getOriginalFilename();

            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            log.info("fileName: " + fileName);

            //날짜 폴더 생성
            String folderPath = makeFolder();

            //UUID
            String uuid = UUID.randomUUID().toString();

            //저장할 파일 이름 중간에 "_"를 이용해서 구분
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid +"_" + fileName;

            Path savePath = Paths.get(saveName);

            try {
                //원본 파일 저장
                uploadFile.transferTo(savePath);

                // 썸네일 생성(썸네일 파일 이름은 중간에 s_로 시작하도록)
                String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator + "s_" + uuid + "_" + fileName;

                File thumbnailFile = new File(thumbnailSaveName);

                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile,100,100);

                resultDTOList.add(new UploadResultDTO(fileName,uuid,folderPath));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }//end for
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    private String makeFolder() {

        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath =  str.replace("/", File.separator);

        File uploadPathFolder = new File(uploadPath, folderPath);

        if (uploadPathFolder.exists() == false) {
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }

    // 업로드 이미지 출력
    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName) {

        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName =  URLDecoder.decode(fileName,"UTF-8");
            log.info("fileName: " + srcFileName);
            File file = new File(uploadPath +File.separator+ srcFileName);
            log.info("file: " + file);
            HttpHeaders header = new HttpHeaders();

            //MIME타입 처리
            header.add("Content-Type", Files.probeContentType(file.toPath()));

            //파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName){
        String srcFileName = null;
        try{
            srcFileName = URLDecoder.decode(fileName,"UTF-8");
            File file = new File(uploadPath +File.separator+ srcFileName);
            boolean result = file.delete();

            File thumbnail = new File(file.getParent(), "s_" + file.getName());

            result = thumbnail.delete();

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
            return new ResponseEntity<>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
