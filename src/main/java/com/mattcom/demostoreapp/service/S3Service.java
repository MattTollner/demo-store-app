package com.mattcom.demostoreapp.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.Random;

@Service
public class S3Service {

    private final String region = "eu-west-1";
    private final String bucketName = "demostorebucket";
    private final String accessKeyId = "AKIAW2LQDHRS6HERT6HA";
    private final String secretAccessKey = "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";

    //TODO put in environment vars


    public S3Service() {
    }

    //static AWSStaticCredentialsProvider cred
    public String generateUploadUrl() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        final AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion(region).build();

        //URL Expire Time
        Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);

        //Generate Random Name
        StringBuilder imageName = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 4; i++) {
            int n = r.nextInt();
            imageName.append(Integer.toHexString(n));
        }

        //Genereate URL
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, imageName.toString())
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        System.out.println(url.toString());
        return url.toString();
    }
}
