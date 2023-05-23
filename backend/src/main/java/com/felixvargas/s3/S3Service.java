package com.felixvargas.s3;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

@Service
public class S3Service {


    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }


    // this method allows us to upload a file to the bucket
    public void putObject(String bucketName, String key, byte[] file){
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.putObject(objectRequest, RequestBody.fromBytes(file));

    }

    // this implementation is for downloading a file from the bucket and returning it as a byte array
    public byte[] getObject(String bucketName, String key){
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(objectRequest);

        try {
            return responseInputStream.readAllBytes();
        } catch (IOException e) {
           throw new RuntimeException(e);
        }

    }


















}
