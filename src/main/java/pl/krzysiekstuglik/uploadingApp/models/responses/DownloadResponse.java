package pl.krzysiekstuglik.uploadingApp.models.responses;

import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class DownloadResponse {
    private Resource resource;
    private String realName;
    private boolean isFileExist;
}
