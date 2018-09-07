package pl.krzysiekstuglik.uploadingApp.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.krzysiekstuglik.uploadingApp.models.FileEntity;
import pl.krzysiekstuglik.uploadingApp.models.dto.FileDto;
import pl.krzysiekstuglik.uploadingApp.models.repositories.FileRepository;
import pl.krzysiekstuglik.uploadingApp.models.responses.DownloadResponse;
import pl.krzysiekstuglik.uploadingApp.models.responses.UploadResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {

    final FileRepository fileRepository;
    final DiscService discService;
    final PromotionService promotionService;

    @Autowired
    public FileService(FileRepository fileRepository, DiscService discService, PromotionService promotionService) {
        this.fileRepository = fileRepository;
        this.discService = discService;
        this.promotionService = promotionService;
    }

    public UploadResponse uploadFile(FileDto fileDto) {
        String randomName = getRandomString();
        UploadResponse uploadResponse = new UploadResponse();
        checkMondayPromotion(fileDto);
        checkDatabaseSave(fileDto, randomName);
        checkDiscSave(fileDto, randomName);
        uploadResponse.setUrlToFile(randomName);
        uploadResponse.setResponseType(UploadResponse.ResponseType.UPLOADED);
        return uploadResponse;
    }

    private void checkMondayPromotion(FileDto fileDto) {
        UploadResponse uploadResponse = new UploadResponse();
        if (!promotionService.isMondeyToday()) {
            if (fileDto.getMultipartFile().getSize() >= promotionService.getMondayPromotionMaxFileSize())
                uploadResponse.setResponseType(UploadResponse.ResponseType.FILE_TO_LARGE);
        }
    }

    private UploadResponse checkDiscSave(FileDto fileDto, String randomName) {
        UploadResponse uploadResponse = new UploadResponse();
        if (!discService.save(fileDto, randomName)) {
            uploadResponse.setResponseType(UploadResponse.ResponseType.DISC_ERROR);
        }
        return uploadResponse;
    }

    private UploadResponse checkDatabaseSave(FileDto fileDto, String randomName) {
        UploadResponse uploadResponse = new UploadResponse();
        if (!createFileInDatabase(fileDto, randomName)) {
            uploadResponse.setResponseType(UploadResponse.ResponseType.DATABASE_ERROR);
        }
        return uploadResponse;
    }

    public DownloadResponse downloadFile(String generatedName) {
        Optional<FileEntity> fileEntity = fileRepository.getFileByRandomName(generatedName);
        DownloadResponse downloadResponse = new DownloadResponse();

        if (!fileEntity.isPresent()) {
            downloadResponse.setFileExist(false);
            return downloadResponse;
        }
        downloadResponse.setRealName(fileEntity.get().getOriginalName());
        downloadResponse.setResource(discService.read(generatedName));
        return downloadResponse;
    }

    public boolean createFileInDatabase(FileDto fileDto, String fileName) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setOriginalName(fileDto.getMultipartFile().getOriginalFilename());
        fileEntity.setSize(fileDto.getMultipartFile().getSize());
        fileEntity.setIp(fileDto.getIp());
        fileEntity.setFileName(fileName);

        fileRepository.save(fileEntity);
        return true;
    }

    public boolean deleteUploadsOlderThenOneHour() {
        List<FileEntity> fileToDeleteList = fileRepository.getFilesOlderThanOneHour();
        fileToDeleteList.forEach(s -> {
            discService.delete(s.getFileName());
            fileRepository.deleteById(s.getId());
        });
        return true;
    }

    private String getRandomString() {
        return UUID.randomUUID().toString();
    }

    public Optional<FileEntity> getFileByRandomName(String randomName) {
        return fileRepository.getFileByRandomName(randomName);
    }

    public List<FileEntity> getListNeweastFile() {
        return fileRepository.getListNeweastFile();
    }

}
