package com.mingri.prize.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingri.prize.commons.db.entity.*;
import com.mingri.prize.commons.db.entity.CardGame;
import com.mingri.prize.commons.db.mapper.CardGameRulesMapper;
import com.mingri.prize.commons.db.service.CardGameService;
import com.mingri.prize.commons.db.mapper.CardGameMapper;
import com.mingri.prize.commons.db.service.GameLoadService;
import com.mingri.prize.commons.utils.PageBean;
import com.mingri.prize.commons.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author shawn
* @description 针对表【card_game】的数据库操作Service实现
* @createDate 2023-12-26 11:58:48
*/
@Slf4j
@Service
public class CardGameServiceImpl extends ServiceImpl<CardGameMapper, CardGame>
    implements CardGameService{

    @Autowired
    private CardGameMapper cardGameMapper;
    @Autowired
    private CardGameRulesMapper cardGameRulesMapper;
    @Autowired
    private GameLoadService gameLoadService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 查询活动
     * @param status
     * @param curpage
     * @param limit
     * @return
     */
    public PageBean<CardGame> pageGame(int status, int curpage, int limit) {
        Page<CardGame> page = new Page();

        log.info(String.valueOf(status));

        QueryWrapper<CardGame> gameQueryWrapper = new QueryWrapper<>();
        if (status != -1){
            gameQueryWrapper.eq("status", status);
        }
        //查询全部
        else if (status == -1){
            gameQueryWrapper.like("status", "");
        }
        IPage result = page(page, gameQueryWrapper);
        IPage result1 = page(page, gameQueryWrapper);

        PageBean<CardGame> pageBean = new PageBean<>
                (curpage, limit, result.getTotal(), result.getRecords());

        return pageBean;
    }

}




