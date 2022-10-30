package com.findeyourbro.service.file;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.apache.tika.Tika;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.findeyourbro.config.AWSConfig;

@Service
public class FileService {
    
    private final static String BUCKET_NAME = "rallyimagestore";
    private final static String JPEG_IMAGE_TYPE = "JPEG";
    private final static String PNG_IMAGE_TYPE = "PNG";
    private final static String BMP_IMAGE_TYPE = "BMP";
    private final static String GIF_IMAGE_TYPE = "GIF";
    private final static String IMAGE_HEADER_TYPE = "IMAGE";
   
    public String getImageAsUrl(String imageKey){        
        ApplicationContext context = new AnnotationConfigApplicationContext(AWSConfig.class);
        AmazonS3 amazonS3Client = context.getBean("amazonS3", AmazonS3.class);
        URL url = amazonS3Client.getUrl(BUCKET_NAME, imageKey);
        return url.toString();
    }
    
    public void storeImage(String imageName, String imageBase64, String key) {
        byte[] bI = Base64.decodeBase64((imageBase64.substring(imageBase64.indexOf(",")+1)).getBytes());

        InputStream fis = new ByteArrayInputStream(bI);
        
        ApplicationContext context = new AnnotationConfigApplicationContext(AWSConfig.class);
        AmazonS3 amazonS3Client = context.getBean("amazonS3", AmazonS3.class);
        
        ObjectMetadata metadata = new ObjectMetadata();     
        metadata.setContentLength(bI.length);
        metadata.setContentType(new Tika().detect(bI));
        
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, key, fis, metadata);    
        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read); //all users or authenticated
        putObjectRequest.setAccessControlList(acl);

        
        if(validateImageType(metadata.getContentType())) {
            amazonS3Client.putObject(putObjectRequest);
        }        
    }
    
    public void deleteImageByKey(String key) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AWSConfig.class);
        AmazonS3 amazonS3Client = context.getBean("amazonS3", AmazonS3.class);
        amazonS3Client.deleteObject(BUCKET_NAME, key);
    }
    
    public boolean validateImageType(String contentType) {
        String format = contentType.split("/")[1];     
        if(contentType.split("/")[0].equalsIgnoreCase(IMAGE_HEADER_TYPE) 
                && (format.equalsIgnoreCase(BMP_IMAGE_TYPE)
                || format.equalsIgnoreCase(PNG_IMAGE_TYPE)
                || format.equalsIgnoreCase(GIF_IMAGE_TYPE)
                || format.equalsIgnoreCase(GIF_IMAGE_TYPE)
                || format.equalsIgnoreCase(JPEG_IMAGE_TYPE))) {
            return true;
        }
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Tipo de imagem inválida", null);            
    }
}
