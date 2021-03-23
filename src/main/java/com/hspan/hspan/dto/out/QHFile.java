package com.hspan.hspan.dto.out;

import com.hspan.hspan.data.models.HFile;

public class QHFile extends QResponse{

    private String fileName;



    public QHFile(HFile data, String status, boolean success) {

        super(data, data.getId(), status, success);
    }
}
