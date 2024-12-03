package com.iusie.campuscircle.model.converter;

import cn.hutool.json.JSONUtil;
import com.iusie.campuscircle.model.dto.UserDO;
import com.iusie.campuscircle.model.entity.User;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author iusie
 * @description
 * @date 2024/12/2
 */
public class UserConverter {

    public static UserDO convertToUserDO(User user) {
        UserDO userDO = new UserDO();

        userDO.setId(user.getId());
        userDO.setUserAccount(user.getUserAccount());
        userDO.setUserPassword(user.getUserPassword());
        userDO.setUsername(user.getUsername());
        userDO.setAvatarUrl(user.getAvatarUrl());
        userDO.setUserProfile(user.getUserProfile());
        userDO.setGender(user.getGender());
        userDO.setPhone(user.getPhone());
        userDO.setEmail(user.getEmail());
        userDO.setUserRole(user.getUserRole());
        userDO.setUserStatus(user.getUserStatus());
        userDO.setCreateTime(user.getCreateTime());
        userDO.setUpdateTime(user.getUpdateTime());
        userDO.setIsDelete(user.getIsDelete());

        // 将 tags 字段从 JSON 字符串转换为 List<String>
        if (user.getTags() != null) {
            List<String> tagsList = JSONUtil.toList(user.getTags(), String.class);
            userDO.setTags(tagsList);
        }

        return userDO;
    }
}
