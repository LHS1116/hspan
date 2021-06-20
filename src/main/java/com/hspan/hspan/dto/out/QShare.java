package com.hspan.hspan.dto.out;

import com.hspan.hspan.data.models.Share;
import com.hspan.hspan.data.models.User;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Setter
@Getter
public class QShare {

    public Long fromID;

    public Long toID;

    public Long fileID;

    public String code;

    public boolean isPublic;

    public boolean isCanceled;

    public static QShare convert(Share share, ModelMapper mapper) {
        if (share == null) {
            return null;
        }

        QShare qs = mapper.map(share, QShare.class);
        qs.code = "";
        System.out.println(qs);
        return qs;
    }

    @Override
    public String toString() {
        return "QShare{" +
                "fromID=" + fromID +
                ", toID=" + toID +
                ", fileID=" + fileID +
                ", isPublic=" + isPublic +
                ", isCanceled=" + isCanceled +
                '}';
    }
}
