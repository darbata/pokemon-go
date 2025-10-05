package poke.consumer.components;

import java.nio.file.Path;

import org.springframework.stereotype.Component;

import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
public class S3 {

    private final S3Client s3 = S3Client.builder()
        .region(Region.AP_SOUTHEAST_2)
        .credentialsProvider(AnonymousCredentialsProvider.create()) // public bucket
        .build();


    public void uploadFile(Path path) {
        PutObjectRequest put = PutObjectRequest.builder()
            .bucket("sit313-heatmap") // system uses single bucket
            .key("heatmaps/" + System.currentTimeMillis() + ".csv") 
            .build();

        s3.putObject(put, path);
    }
    
}
