package com.iusie.campuscircle.model.request.team;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author iusie
 * @description
 * @date 2024/12/15
 */
@Data
public class TeamJoinRequest  implements Serializable {

    @Serial
    private static final long serialVersionUID = -8911934716308167050L;
    /**
     * 队伍id
     */
    private Long teamId;

    /**
     * 队伍密码
     */
    private String teamPassword;
}
