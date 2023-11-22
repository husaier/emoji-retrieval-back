package org.bupt.hse.retrieval.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2023-11-19
 */
@Data
public class UserVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private long id;

    private String name;

    private String email;
}
