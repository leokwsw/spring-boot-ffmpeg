package dev.leonardpark.ffmpeg.respository;

import dev.leonardpark.ffmpeg.entity.FileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends MongoRepository<FileEntity, String> {

}
