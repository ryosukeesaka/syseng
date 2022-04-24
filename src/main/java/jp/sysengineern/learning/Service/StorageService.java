package jp.sysengineern.learning.Service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void init();

    void store(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    //リソースとして定義された画像を参照。
    Resource loadAsResource(String filename);

    void deleteAll();

}
