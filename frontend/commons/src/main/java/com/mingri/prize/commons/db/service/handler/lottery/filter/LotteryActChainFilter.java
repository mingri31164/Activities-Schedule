package com.mingri.prize.commons.db.service.handler.lottery.filter;

import com.mingri.prize.commons.db.enums.LotteryChainMarkEnum;
import com.mingri.prize.commons.db.service.handler.lottery.dto.LotteryActDTO;
import com.mingri.prize.framework.starter.designpattern.chain.AbstractChainHandler;


/**
 * 抽奖过滤器
 */
public interface LotteryActChainFilter<T extends LotteryActDTO> extends AbstractChainHandler<LotteryActDTO> {

    @Override
    default String mark() {
        return LotteryChainMarkEnum.LOTTERY_ACT_FILTER.name();
    }
}
