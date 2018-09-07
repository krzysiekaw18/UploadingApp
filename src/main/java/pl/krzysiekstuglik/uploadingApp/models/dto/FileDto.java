package pl.krzysiekstuglik.uploadingApp.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class FileDto {
    private MultipartFile multipartFile;
    private String ip;
}
