package com.findeyourbro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AWSConfig {
    public AWSCredentials credentials() {
        AWSCredentials credentials = new BasicAWSCredentials(
                "AKIAQTILGYDUVEPT2EH2",
                "Rg2HRLfe40+Zseyqs2PUbIAQpPD49ZsnDjr5m7KQ"
        );
        return credentials;
    }

    @Bean
    public AmazonS3 amazonS3() {
        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials()))
                .withRegion(Regions.SA_EAST_1)
                .build();
        return s3client;
    }
    
}
