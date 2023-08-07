package com.mysite.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {			//실제 업로드된 파일을 물리적인 경로에 저장 

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{
       
    	
    	UUID uuid = UUID.randomUUID();		//고유한 번호를 자동으로 생성 시켜 준다. 
    	//원본 파일의 확장자만 저장 
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        // 서버에 저장할 파일이름 
        String savedFileName = uuid.toString() + extension;
        // c:/shop/item/UUID.jpg
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        
        // 반드시 예외 처리를 해야 한다. : 자신이 직접 처리 (Try Catch ) , Throws : 예외를 호출하는 쪽에서 처리하도록
        // 미문다. 
        // c:/shop/item/UUID.jpg 
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();
        return savedFileName;
    }

    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath);
        if(deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }

}