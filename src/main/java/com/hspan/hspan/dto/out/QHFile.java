package com.hspan.hspan.dto.out;

import com.hspan.hspan.data.models.HFile;

public class QHFile {

    private String fileName;
    private Long size;
    private String uploadDate;



    public QHFile(HFile data, String status, boolean success) {

        this.size = data.getSize();
        this.fileName = data.getFilePath();
    }
}
