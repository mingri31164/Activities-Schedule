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
 * 活动状态检查
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class LotteryActGameStatusChainHandler implements LotteryActChainFilter<LotteryActDTO> {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void handler(LotteryActDTO lotteryActDTO) {

        Integer gameid = lotteryActDTO.getGameid();
        // 获取活动信息 判断活动是否进行中
        CardGame game = (CardGame) redisUtil.get(RedisKeys.INFO + gameid);
        Date now = new Date();
        if(game == null || game.getStarttime().compareTo(now) > 0){
            log.info("活动当前时间：{}, 开始时间：{}", now, game.getStarttime());
            throw new ClientException("活动还未开始");
        }else if (game.getEndtime().compareTo(now) < 0){
            throw new ClientException("活动已结束");
        }

    }

    @Override
    public int getOrder() {
        return 10;
    }
}
