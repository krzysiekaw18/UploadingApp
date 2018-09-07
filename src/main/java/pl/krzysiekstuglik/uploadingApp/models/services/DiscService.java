package pl.krzysiekstuglik.uploadingApp.models.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import pl.krzysiekstuglik.uploadingApp.models.dto.FileDto;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

@Service
public class DiscService {

    @Value("${path.to.resources}")
    String pathToResourcesCatalog;

    public boolean save(FileDto fileDto, String fileName){
        File file = new File(pathToResourcesCatalog + File.separator + fileName);
        try {
            Files.write(file.toPath(), fileDto.getMultipartFile().getBytes(), StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public Resource read(String fileName){
        File file = new File(pathToResourcesCatalog + File.separator + fileName);
        return new FileSystemResource(file);
    }

    public boolean delete(String fileName) {
        File file = new File(pathToResourcesCatalog + File.separator + fileName);
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
