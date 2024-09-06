package com.itheima.prize.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.prize.commons.config.RedisKeys;
import com.itheima.prize.commons.db.entity.CardGame;
import com.itheima.prize.commons.db.entity.CardGameRules;
import com.itheima.prize.commons.db.entity.CardUser;
import com.itheima.prize.commons.db.entity.ViewCardUserHit;
import com.itheima.prize.commons.db.mapper.CardGameRulesMapper;
import com.itheima.prize.commons.db.service.CardGameRulesService;
import com.itheima.prize.commons.db.service.CardGameService;
import com.itheima.prize.commons.db.mapper.CardGameMapper;
import com.itheima.prize.commons.utils.PageBean;
import com.itheima.prize.commons.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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

        PageBean<CardGame> pageBean = new PageBean<>
                (curpage, limit, result.getTotal(), result.getRecords());

        return pageBean;
    }

    /**
     * 缓存预热
     */
    public void listAndSaveAboutToStartGames() {
        log.info("缓存预热");
        LocalDateTime begin = LocalDateTime.now();
        LocalDateTime end = begin.plusMinutes(100);

        List<CardGame> cardGameList =  cardGameMapper.selectAboutToStartGames(begin, end);
        for (CardGame game : cardGameList) {
            //缓存活动基本信息
            redisUtil.set(RedisKeys.INFO+game.getId(),game,-1);
            //缓存策略信息
            List<CardGameRules> cardGameRulesList = cardGameRulesMapper.listGameRulesByGameId(game.getId());
            for (CardGameRules r : cardGameRulesList){
                redisUtil.hset(RedisKeys.MAXGOAL+game.getId(),r.getUserlevel()+"",r.getGoalTimes());
                redisUtil.hset(RedisKeys.MAXENTER+game.getId(),r.getUserlevel()+"",r.getEnterTimes());
            }
        }
    }
}




