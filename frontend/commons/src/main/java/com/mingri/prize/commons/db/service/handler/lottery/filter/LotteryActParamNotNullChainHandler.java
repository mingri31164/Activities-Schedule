package com.mingri.prize.commons.db.service.handler.lottery.filter;

import cn.hutool.core.util.StrUtil;
import com.mingri.prize.commons.db.service.handler.lottery.dto.LotteryActDTO;
import com.mingri.prize.framework.starter.web.convention.exception.ClientException;
import org.springframework.stereotype.Component;


/**
 * 抽奖参数不能为空
 */
@Component
public class LotteryActParamNotNullChainHandler implements LotteryActChainFilter<LotteryActDTO> {

    @Override
    public void handler(LotteryActDTO lotteryActDTO) {
        if (StrUtil.isBlank((CharSequence) lotteryActDTO.getCardUser())) {
            throw new ClientException("用户信息为空");
        }
        if (StrUtil.isBlank(String.valueOf(lotteryActDTO.getGameid()))) {
            throw new ClientException("活动标识不能为空");
        }
        if (StrUtil.isBlank((CharSequence) lotteryActDTO.getRequest())) {
            throw new ClientException("网络请求不能为空");
        }

    }


    @Override
    public int getOrder() {
        //数字越小优先级越高
        return 0;
    }
}
