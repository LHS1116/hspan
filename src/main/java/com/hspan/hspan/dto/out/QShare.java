package com.hspan.hspan.dto.out;

import com.hspan.hspan.data.models.Share;
import com.hspan.hspan.data.models.User;
import org.modelmapper.ModelMapper;

public class QShare {

    public Long fromID;

    public Long toID;

    public Long fileID;

    public boolean isPublic;

    public boolean isCanceled;

    public static QShare convert(Share share, ModelMapper mapper) {
        if (share == null) {
            return null;
        }
        return mapper.map(share, QShare.class);
    }


}
