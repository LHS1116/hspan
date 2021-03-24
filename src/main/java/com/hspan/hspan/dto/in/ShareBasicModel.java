package com.hspan.hspan.dto.in;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareBasicModel {
    @NotNull
    public Long fromID;

    public Long toID;

    @NotNull
    public Long fileID;

    public boolean isPublic;
}
