package com.itheima.prize.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.prize.commons.config.RedisKeys;
import com.itheima.prize.commons.db.entity.*;
import com.itheima.prize.commons.db.mapper.CardGameRulesMapper;
import com.itheima.prize.commons.db.service.CardGameProductService;
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
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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
    private CardGameProductService GameProductService;
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
        LocalDateTime end = begin.plusMinutes(1000);

        List<CardGame> cardGameList = cardGameMapper.selectAboutToStartGames(begin, end);
        for (CardGame game : cardGameList) {
            // 缓存活动基本信息
            redisUtil.set(RedisKeys.INFO + game.getId(), game, -1);

            // 缓存策略信息
            List<CardGameRules> cardGameRulesList = cardGameRulesMapper.listGameRulesByGameId(game.getId());
            for (CardGameRules r : cardGameRulesList) {
                redisUtil.hset(RedisKeys.MAXGOAL + game.getId(), r.getUserlevel() + "", r.getGoalTimes());
                redisUtil.hset(RedisKeys.MAXENTER + game.getId(), r.getUserlevel() + "", r.getEnterTimes());
            }
            // 抽奖令牌桶
            List<Map<Long, CardProductDto>> tokenList = new ArrayList<>();

            // 查询活动对应的奖品ID和数量
            List<CardProductDto> gameProductList = GameProductService.listGameProductsByGameId(game.getId());

            // 在活动时间段内生成随机时间戳做令牌
            for (CardProductDto product : gameProductList) {
                Date gameStartTime = game.getStarttime();
                Date gameEndTime = game.getEndtime();

                // 获取游戏开始和结束时间的时间戳（毫秒）
                long startMillis = gameStartTime.getTime();
                long endMillis = gameEndTime.getTime();

                // 生成随机中奖时间戳（毫秒级）
                long randomStartMillis = ThreadLocalRandom.current().nextLong(startMillis, endMillis);
                // 解决令牌重复问题：将（时间戳*1000+3位随机数）作为令牌（防止时间段短奖品多时重复）。
                // 抽奖时将抽中的令牌/1000，还原真实时间戳
                long duration = endMillis - randomStartMillis;
                long rnd = randomStartMillis + new Random().nextInt((int) duration);
                long token = rnd * 1000 + new Random().nextInt(999);

                // 将时间戳与对应的奖品信息关联
                Map<Long, CardProductDto> tokenMap = new HashMap<>();
                tokenMap.put(token, product);
                tokenList.add(tokenMap);
            }

            // 按时间戳从小到大排序
            tokenList.sort((m1, m2) -> Long.compare(m1.keySet().iterator().next(), m2.keySet().iterator().next()));

            // 将抽奖令牌桶存入 Redis，从右侧入队
            redisUtil.rightPushAll(RedisKeys.TOKENS + game.getId(), tokenList);
        }
    }
}




