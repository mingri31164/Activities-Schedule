package com.mingri.prize.commons.db.service;

import com.mingri.prize.commons.db.entity.CardGame;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mingri.prize.commons.utils.PageBean;

/**
* @author shawn
* @description 针对表【card_game】的数据库操作Service
* @createDate 2023-12-26 11:58:48
*/
public interface CardGameService extends IService<CardGame> {

    /**
     * 查询活动列表
     * @param status
     * @param curpage
     * @param limit
     * @return
     */
    PageBean<CardGame> pageGame(int status, int curpage, int limit);

}
