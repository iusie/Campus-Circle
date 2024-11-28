package com.iusie.campuscircle.model.enums;

import lombok.Getter;

/**
 * @author iusie
 * @description 队伍状态枚举
 * @date 2024/11/28
 */
@Getter
public enum TeamStatusEnums {

    PUBLIC(0,"公开"),
    PRIVATE(1,"私有"),
    SECRET(2,"加密");


    private final int value;
    private final String text;

    public static TeamStatusEnums getEnumByValues(Integer value){
        if(value == null){
            return null;
        }
        TeamStatusEnums[] values = TeamStatusEnums.values();
        for (TeamStatusEnums teamStatusEnum:values
        ) {
            if(teamStatusEnum.getValue() == value){
                return teamStatusEnum;
            }
        }
        return null;
    }

    TeamStatusEnums(int value, String text) {
        this.value = value;
        this.text = text;
    }

}