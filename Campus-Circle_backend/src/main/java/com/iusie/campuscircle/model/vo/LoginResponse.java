package com.iusie.campuscircle.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author iusie
 * @description
 * @date 2024/11/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginResponse extends UserVO{
    private String token;
}
