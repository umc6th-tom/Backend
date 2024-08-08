package umc6.tom.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import umc6.tom.common.model.Uuid;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Util {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // MultipartFile 을 전달받아 File 로 전환한 후 S3에 업로드
    public String upload(MultipartFile multipartFile, String path, Uuid uuid) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        try {
            return upload(uploadFile, path, uuid);
        } finally {
            removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)
        }
    }

    private String upload(File uploadFile, String path, Uuid uuid) {
        String fileName = generateKeyName(path, uuid);
        return putS3(uploadFile, fileName);      // 업로드된 파일의 S3 URL 주소 반환
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.exists() && !targetFile.delete()) {
            log.error("파일이 삭제되지 못했습니다: {}", targetFile.getAbsolutePath());
            throw new RuntimeException("파일 삭제 실패: " + targetFile.getAbsolutePath());
        }
    }

    public void deleteFile(String targetFileName) {
        targetFileName = targetFileName.substring(46);
        log.info("targetFileUrl {} : ", targetFileName);
        amazonS3Client.deleteObject(bucket, targetFileName);
    }


    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("java.io.tmpdir") + "/" + Objects.requireNonNull(file.getOriginalFilename()));
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            } catch (IOException e) {
                log.error("파일 변환 중 오류 발생: {}", e.getMessage());
                throw e;
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    public String generateKeyName(String path, Uuid uuid) {
        return path + '/' + uuid.getUuid();
    }
}
