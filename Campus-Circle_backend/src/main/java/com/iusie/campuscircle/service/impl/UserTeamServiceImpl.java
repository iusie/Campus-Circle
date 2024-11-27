package com.iusie.campuscircle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iusie.campuscircle.model.entity.UserTeam;
import com.iusie.campuscircle.service.UserTeamService;
import com.iusie.campuscircle.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
* @createDate 2024-11-27 11:04:57
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}




