package com.iusie.campuscircle.model.request.friends;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

/**
 * @author iusie
 * @description
 * @date 2024/12/13
 */
@Data
public class FriendAddRequest {
    /**
     * 好友申请id
     */
    private Long id;

    /**
     * 接收申请的用户id
     */
    private Long receiveId;

    /**
     * 好友申请备注信息
     */
    private String remark;
}
