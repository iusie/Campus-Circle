package com.iusie.campuscircle;

import com.iusie.campuscircle.controller.UserController;
import com.iusie.campuscircle.model.converter.UserConverter;
import com.iusie.campuscircle.model.dto.UserDO;
import com.iusie.campuscircle.model.entity.User;
import com.iusie.campuscircle.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author iusie
 * @description
 * @date 2024/12/2
 */
@SpringBootTest
public class UserDOTest {

    @Autowired
    UserService userService;

    @Test
    public void userT()
    {
        User user = userService.getById("1860233492181352449");
        UserDO userDO = UserConverter.convertToUserDO(user);
        System.out.println("源数据:"+user);
        System.out.println("变化数据:"+userDO);
    }
}
