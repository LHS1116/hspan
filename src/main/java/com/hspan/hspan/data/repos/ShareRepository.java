package com.hspan.hspan.data.repos;

import com.hspan.hspan.data.models.Share;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShareRepository extends JpaRepository<Share, Long> {

    List<Share> findAllByFromID(Long fromID);

    List<Share> findByFromIDAndToID(Long fromID, Long toID);

    Share findByFromIDAndIsPublicAndFileID(Long fromID, Boolean isPublic, Long fileID);

}
