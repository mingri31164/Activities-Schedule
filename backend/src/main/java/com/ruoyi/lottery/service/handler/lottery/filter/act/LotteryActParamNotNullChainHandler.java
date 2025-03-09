package com.ruoyi.lottery.service.handler.lottery.filter.act;

import com.itheima.prize.commons.config.RedisKeys;
import com.itheima.prize.commons.db.entity.CardUser;
import com.itheima.prize.commons.utils.ApiResult;
import com.itheima.prize.commons.utils.RedisUtil;
import com.ruoyi.lottery.domain.CardGame;
import com.ruoyi.lottery.domain.filter.LotteryActDTO;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 抽奖流程过滤器之验证参数必填
 */
public class LotteryActParamNotNullChainHandler implements LotteryActChainFilter<LotteryActDTO>{

    @Override
    public void handler(LotteryActDTO requestParam) {
        String gameid = requestParam.getGameid();
        if (StrUtil.isBlank(gameid)) {
//            return new ApiResult(-1,"活动标识不能为空",null);
            throw new RuntimeException("活动标识不能为空");
        }
        CardUser user = requestParam.getUser();
        if (user == null) {
//            return new ApiResult(-1,"用户未登录",null);
            throw new RuntimeException("用户未登录");
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
