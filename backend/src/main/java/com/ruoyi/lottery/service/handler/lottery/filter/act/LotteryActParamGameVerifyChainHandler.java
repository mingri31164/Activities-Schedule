package com.ruoyi.lottery.service.handler.lottery.filter.act;

import com.itheima.prize.commons.config.RedisKeys;
import com.itheima.prize.commons.db.entity.CardGame;
import com.itheima.prize.commons.db.entity.CardUser;
import com.itheima.prize.commons.utils.ApiResult;
import com.itheima.prize.commons.utils.RedisUtil;
import com.ruoyi.lottery.domain.filter.LotteryActDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.ruoyi.common.constant.LotteryConstants.defaultMaxEnterTime;
import static com.ruoyi.common.constant.LotteryConstants.defaultMaxGoalTime;

/**
 * 抽奖流程过滤器之活动数据验证
 */
@Component
@RequiredArgsConstructor
public class LotteryActParamGameVerifyChainHandler implements LotteryActChainFilter<LotteryActDTO>{

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void handler(LotteryActDTO requestParam) {
        String gameid = String.valueOf(requestParam.getGameid());

        // 获取活动信息 判断活动是否进行中
        CardGame game = (CardGame) redisUtil.get(RedisKeys.INFO + gameid);
        Date now = new Date();
        if(game == null || game.getStarttime().compareTo(now) > 0){
//            return new ApiResult(-1,"比赛还未开始",null);
            throw new RuntimeException("比赛还未开始");
        }else if (game.getEndtime().compareTo(now) < 0){
//            return new ApiResult(-1,"比赛已结束",null);
            throw new RuntimeException("比赛已结束");
        }
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
