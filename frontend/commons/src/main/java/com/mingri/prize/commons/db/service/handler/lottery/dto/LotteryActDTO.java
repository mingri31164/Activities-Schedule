package com.mingri.prize.commons.db.service.handler.lottery.dto;

import com.mingri.prize.commons.db.entity.CardUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 选择座位实体

 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class LotteryActDTO {

    /**
     * 用户信息
     */
    private CardUser cardUser;

    /**
     * 活动id
     */
    private Integer gameid;

    /**
     * 请求体
     */
    private HttpServletRequest request;

}
