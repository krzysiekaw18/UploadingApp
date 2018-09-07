package pl.krzysiekstuglik.uploadingApp.models.responses;

import lombok.Data;

@Data
public class UploadResponse {
    public enum ResponseType{
        UPLOADED, DATABASE_ERROR, DISC_ERROR, FILE_TO_LARGE;
    }
    private ResponseType responseType;
    private String urlToFile;
}
