package com.ruoyi.lottery.domain.filter;


import com.itheima.prize.commons.db.entity.CardUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;


/**
 * 抽奖请求参数
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotteryActDTO {

    String gameid;

    CardUser user;
}
