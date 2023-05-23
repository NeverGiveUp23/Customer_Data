package com.felixvargas.s3;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.s3.mock}")
    private boolean mock;

    @Bean // <- this is a bean, so it will be available in the application context, you want this because you want to inject it in your service
    public S3Client s3Client(){
        if(mock){
            return new FakeS3();
        }
        return S3Client.builder()
                .region(Region.of(awsRegion)) // Region.US_WEST_2 is the region where the bucket was created
                .build();
    }
}
