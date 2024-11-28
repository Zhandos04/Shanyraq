package by.project.turamyzba.services.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws IOException {
        // Генерация уникального имени для файла
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // Создание метаданных для файла
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        // Загрузка файла в S3
        s3client.putObject(bucketName, fileName, file.getInputStream(), metadata);

        // Возвращаем публичный URL файла в S3
        return s3client.getUrl(bucketName, fileName).toString();
    }
}
