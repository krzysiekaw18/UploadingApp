package pl.krzysiekstuglik.uploadingApp.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "file")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "original_name")
    private String originalName;
    @Column(name = "upload_date")
    private LocalDateTime uploadDate;
    private String ip;
    private long size;
}
