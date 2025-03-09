package com.ruoyi.lottery.service.handler.lottery.filter.act;


import com.ruoyi.common.enums.LotteryChainMarkEnum;
import com.ruoyi.framework.designpattern.chain.AbstractChainHandler;
import com.ruoyi.lottery.domain.filter.LotteryActDTO;

/**
 * 抽奖过滤器
 */
public interface LotteryActChainFilter <T extends LotteryActDTO> extends AbstractChainHandler<LotteryActDTO> {

    @Override
    default String mark() {
        return LotteryChainMarkEnum.LOTTERY_ACT_MARK_ENUM.name();
    }

}
