package com.hspan.hspan.dto.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBasicModel {
//    @NotNull
//    @Size(min = 3, max = 16)
    public String username;

//    @NotNull
//    @Size(min = 6, max = 32)
    public String password;

}
