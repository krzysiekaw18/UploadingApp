package pl.krzysiekstuglik.uploadingApp.models.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.krzysiekstuglik.uploadingApp.models.FileEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends CrudRepository<FileEntity, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM file WHERE file_name = ?1")
    Optional<FileEntity> getFileByRandomName(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM file ORDER BY id DESC LIMIT 5")
    List<FileEntity> getListNeweastFile();

    @Query(nativeQuery = true, value = "SELECT * FROM file WHERE HOUR(TIMEDIFF(upload_date, NOW())) >= 1")
    List<FileEntity> getFilesOlderThanOneHour();
}
