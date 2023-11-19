package org.bupt.hse.retrieval.params;

import lombok.Data;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2023-11-19
 */
@Data
public class RegisterParam {
    private String name;

    private String pwd;

    private String email;
}
