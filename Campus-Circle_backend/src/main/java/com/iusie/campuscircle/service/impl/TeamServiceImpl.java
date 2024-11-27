package com.iusie.campuscircle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iusie.campuscircle.model.entity.Team;
import com.iusie.campuscircle.service.TeamService;
import com.iusie.campuscircle.mapper.TeamMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2024-11-27 11:04:57
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

}




