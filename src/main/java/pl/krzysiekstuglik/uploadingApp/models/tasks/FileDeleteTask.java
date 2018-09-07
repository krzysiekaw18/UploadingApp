package pl.krzysiekstuglik.uploadingApp.models.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.krzysiekstuglik.uploadingApp.models.services.FileService;

@Service
public class FileDeleteTask {

    final FileService fileService;

    @Autowired
    public FileDeleteTask(FileService fileService) {
        this.fileService = fileService;
    }

    @Scheduled(fixedRate = 3600)
    public void runDeleteTask(){
        fileService.deleteUploadsOlderThenOneHour();
    }
}
