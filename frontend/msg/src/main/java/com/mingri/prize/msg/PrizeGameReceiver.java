package com.mingri.prize.msg;

import com.alibaba.fastjson.JSON;
import com.mingri.prize.commons.config.RabbitKeys;
import com.mingri.prize.commons.db.entity.CardUserGame;
import com.mingri.prize.commons.db.service.CardUserGameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitKeys.QUEUE_PLAY)
public class PrizeGameReceiver {

    private final static Logger logger = LoggerFactory.getLogger(PrizeGameReceiver.class);

    @Autowired
    private CardUserGameService cardUserGameService;

    @RabbitHandler
    public void processMessage(String message) {
        //从消息队列中获取用户活动信息
        logger.info("user play : msg={}" , message);
        //反序列化为CardUserGame对象
        CardUserGame cardUserGame = JSON.parseObject(message, CardUserGame.class);
        //将用户活动信息保存到数据库中
        boolean save = cardUserGameService.save(cardUserGame);
        if(save){
            logger.warn(RabbitKeys.QUEUE_PLAY, " 用户参与活动信息保存失败，", cardUserGame.toString());
        }
    }

}
