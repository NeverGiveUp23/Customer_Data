package com.felixvargas.s3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {
    @Mock
    private S3Client s3Client;
    private S3Service s3ServiceUnderTest;

    @BeforeEach
    void setUp() {
        s3ServiceUnderTest = new S3Service(s3Client);
    }

    @Test
    void canPutObject() throws IOException {
        //Given
        String bucketName = "customer";
        String key = "profileImageId";
        byte[] file = "customer".getBytes();
        //when
        s3ServiceUnderTest.putObject(bucketName, key, file);
        //Then
        ArgumentCaptor<PutObjectRequest> putObjectRequestArgumentCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);

        ArgumentCaptor<RequestBody> requestBodyArgumentCaptor = ArgumentCaptor.forClass(RequestBody.class);


        verify(s3Client).putObject(
                putObjectRequestArgumentCaptor.capture(),
                requestBodyArgumentCaptor.capture()
        );


        PutObjectRequest putObjectRequestArgumentCaptorValue = putObjectRequestArgumentCaptor.getValue();

        assertThat(putObjectRequestArgumentCaptorValue.bucket()).isEqualTo(bucketName);
        assertThat(putObjectRequestArgumentCaptorValue.key()).isEqualTo(key);

        RequestBody requestBodyArgumentCaptorValue = requestBodyArgumentCaptor.getValue();

        assertThat(requestBodyArgumentCaptorValue.contentStreamProvider().newStream().readAllBytes()).isEqualTo(RequestBody.fromBytes(file).contentStreamProvider().newStream().readAllBytes());


    }

    @Test
    void canGetObject() throws IOException {
        
        //Given
        String bucketName = "customer";
        String key = "profileImageId";
        byte[] file = "customer".getBytes();


        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        //When

        ResponseInputStream<GetObjectResponse> responseInputStream = mock(ResponseInputStream.class);
        when(responseInputStream.readAllBytes()).thenReturn(file);

        when(s3Client.getObject(eq(objectRequest))).thenReturn(responseInputStream);


        //Then

        byte[] bytes = s3ServiceUnderTest.getObject(bucketName, key);


        assertThat(bytes).isEqualTo(file);
    }



    @Test
    void willThrowOnGetObject() throws IOException {

        //Given
        String bucketName = "customer";
        String key = "profileImageId";
        byte[] file = "customer".getBytes();


        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        //When

        ResponseInputStream<GetObjectResponse> responseInputStream = mock(ResponseInputStream.class);
        when(responseInputStream.readAllBytes()).thenThrow(new IOException("Cannot read your bytes, Sorry!"));

        when(s3Client.getObject(eq(objectRequest))).thenReturn(responseInputStream);


        //Then

        assertThatThrownBy(() -> s3ServiceUnderTest.getObject(bucketName, key))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Cannot read your bytes, Sorry!")
                .hasRootCauseInstanceOf(IOException.class);


    }
}