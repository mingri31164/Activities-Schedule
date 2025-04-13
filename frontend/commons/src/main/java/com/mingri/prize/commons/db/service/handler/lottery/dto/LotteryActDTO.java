package com.mingri.prize.commons.db.service.handler.lottery.dto;

import com.mingri.prize.commons.db.entity.CardUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.servlet.http.HttpServletRequest;

/**
 * 选择座位实体
 */
@Data
@AllArgsConstructor
public class LotteryActDTO {

    /**
     * 用户信息
     */
    private CardUser cardUser;

    /**
     * 活动id
     */
    private String gameid;

}
