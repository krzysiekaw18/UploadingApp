package pl.krzysiekstuglik.uploadingApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.krzysiekstuglik.uploadingApp.models.FileEntity;
import pl.krzysiekstuglik.uploadingApp.models.dto.FileDto;
import pl.krzysiekstuglik.uploadingApp.models.responses.DownloadResponse;
import pl.krzysiekstuglik.uploadingApp.models.responses.UploadResponse;
import pl.krzysiekstuglik.uploadingApp.models.services.FileService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@Controller
public class MainController {

    final FileService fileService;

    @Autowired
    public MainController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("listFiles", fileService.getListNeweastFile());
        return "index";
    }

    @PostMapping("/")
    public String index(@RequestParam("file")MultipartFile file,
                        HttpServletRequest servletRequest,
                        Model model){
        UploadResponse uploadResponse = fileService.uploadFile(new FileDto(file, servletRequest.getRemoteAddr()));
        if (uploadResponse.getResponseType() != UploadResponse.ResponseType.UPLOADED){
            model.addAttribute("info", uploadResponse.getResponseType());
            return "index";
        }
        return "redirect:/" + uploadResponse.getUrlToFile();
    }

    @GetMapping("/{fileName}")
    public String showFile(@PathVariable("fileName") String fileName,
                           Model model){
        Optional<FileEntity> fileEntityOptional = fileService.getFileByRandomName(fileName);
        if (!fileEntityOptional.isPresent()){
            return "redirect:/";
        }
        model.addAttribute("fileOptional", fileEntityOptional.get());
        return "file_details";
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable("fileName") String filename){
        DownloadResponse downloadResponse = fileService.downloadFile(filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadResponse.getRealName() + "\"")
                .body(downloadResponse.getResource());
    }
}
