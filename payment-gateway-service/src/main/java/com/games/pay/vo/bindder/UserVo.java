package com.games.pay.vo.bindder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserVo {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
}
