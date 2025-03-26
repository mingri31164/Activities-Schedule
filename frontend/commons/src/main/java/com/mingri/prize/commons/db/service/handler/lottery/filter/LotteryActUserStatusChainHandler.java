package com.mingri.prize.commons.db.service.handler.lottery.filter;

import com.alibaba.fastjson.JSON;
import com.mingri.prize.commons.config.RabbitKeys;
import com.mingri.prize.commons.config.RedisKeys;
import com.mingri.prize.commons.db.entity.CardGame;
import com.mingri.prize.commons.db.entity.CardUser;
import com.mingri.prize.commons.db.entity.CardUserGame;
import com.mingri.prize.commons.db.service.handler.lottery.dto.LotteryActDTO;
import com.mingri.prize.commons.utils.RedisUtil;
import com.mingri.prize.framework.starter.web.convention.exception.ClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * 用户抽奖状态检查
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class LotteryActUserStatusChainHandler implements LotteryActChainFilter<LotteryActDTO> {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    static int defaultMaxEnterTime = 10;
    static int defaultMaxGoalTime = 20;


    @Override
    public void handler(LotteryActDTO lotteryActDTO) {
        CardUser user = lotteryActDTO.getCardUser();
        Integer gameid = lotteryActDTO.getGameid();

        // 判断抽奖次数
        Integer userEnter = (Integer) redisUtil.get(RedisKeys.USERENTER + gameid + "_" + user.getId());
        Integer maxEnterTimes = (Integer) redisUtil.hget(RedisKeys.MAXENTER + gameid, user.getLevel().toString());
        if(maxEnterTimes == null)maxEnterTimes = defaultMaxEnterTime;
        if(userEnter == null){
            redisUtil.set(RedisKeys.USERENTER + gameid + "_" + user.getId(), 1);

            // mq保存参加活动记录
            CardUserGame cardUserGame = CardUserGame.builder()
                    .gameid(gameid)
                    .userid(user.getId())
                    .createtime(new Date())
                    .build();
            String jsonString = JSON.toJSONString(cardUserGame);
            rabbitTemplate.convertAndSend(RabbitKeys.QUEUE_PLAY, jsonString);
        } else if(userEnter >= maxEnterTimes){
            throw new ClientException("您的抽奖次数已用完");
        }
        // 判断中奖次数
        Integer maxGoalTimes = (Integer) redisUtil.hget(RedisKeys.MAXGOAL + gameid, user.getLevel().toString());
        Integer userGoalTimes = (Integer) redisUtil.get(RedisKeys.USERHIT + gameid + "_" + user.getId());
        if(maxGoalTimes == null)maxGoalTimes = defaultMaxGoalTime;
        if(userGoalTimes == null){
            redisUtil.set(RedisKeys.USERHIT + gameid + "_" + user.getId(), 0);
            userGoalTimes = 0;
        }
        if(userGoalTimes >= maxGoalTimes){
            throw new ClientException("您已达到最大中奖数");
        }

    }

    @Override
    public int getOrder() {
        return 20;
    }
}
