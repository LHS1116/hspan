package com.hspan.hspan.data.repos;

import com.hspan.hspan.data.models.HFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HFileRepository extends JpaRepository<HFile,  Long>{
    HFile findByFilePath(String filePath);
}
