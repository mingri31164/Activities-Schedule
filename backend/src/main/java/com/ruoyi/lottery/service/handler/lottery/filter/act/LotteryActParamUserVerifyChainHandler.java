package com.ruoyi.lottery.service.handler.lottery.filter.act;

import com.itheima.prize.commons.config.RedisKeys;
import com.itheima.prize.commons.db.entity.CardUser;
import com.itheima.prize.commons.utils.ApiResult;
import com.itheima.prize.commons.utils.RedisUtil;
import com.ruoyi.lottery.domain.filter.LotteryActDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ruoyi.common.constant.LotteryConstants.defaultMaxEnterTime;
import static com.ruoyi.common.constant.LotteryConstants.defaultMaxGoalTime;

/**
 * 抽奖流程过滤器之用户数据验证
 */
@Component
@RequiredArgsConstructor
public class LotteryActParamUserVerifyChainHandler implements LotteryActChainFilter<LotteryActDTO>{

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void handler(LotteryActDTO requestParam) {
        String gameid = requestParam.getGameid();
        CardUser user = requestParam.getUser();
        // 判断抽奖次数
        Integer maxEnterTimes = (Integer) redisUtil.hget(RedisKeys.MAXENTER + gameid, user.getLevel().toString());
        if(maxEnterTimes == null)maxEnterTimes = defaultMaxEnterTime;

        Integer userEnter = (Integer) redisUtil.get(RedisKeys.USERENTER + gameid + "_" + user.getId());
        if(userEnter != null && userEnter >= maxEnterTimes)
//            return new ApiResult<>(-1,"您的抽奖次数已用完",null);
            throw new RuntimeException("您的抽奖次数已用完");

        // 判断中奖次数
        Integer maxGoalTimes = (Integer) redisUtil.hget(RedisKeys.MAXGOAL + gameid, user.getLevel().toString());
        Integer userGoalTimes = (Integer) redisUtil.get(RedisKeys.USERHIT + gameid + "_" + user.getId());
        if(maxGoalTimes == null)maxGoalTimes = defaultMaxGoalTime;
        if(userGoalTimes == null){
            redisUtil.set(RedisKeys.USERHIT + gameid + "_" + user.getId(), 0);
            userGoalTimes = 0;
        }
        if(userGoalTimes >= maxGoalTimes){
//            return new ApiResult<>(-1,"您已达到最大中奖数",null);
            throw new RuntimeException("您已达到最大中奖数");
        }

    }

    @Override
    public int getOrder() {
        return 20;
    }
}
