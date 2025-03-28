package com.mingri.prize.commons.db.mapper;

import com.mingri.prize.commons.db.entity.CardGameRules;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author shawn
* @description 针对表【card_game_rules(活动策略)】的数据库操作Mapper
* @createDate 2023-12-26 11:58:48
* @Entity entity.db.commons.com.mingri.prize.CardGameRules
*/
public interface CardGameRulesMapper extends BaseMapper<CardGameRules> {

    /**
     * 根据gameid查询活动策略信息
     * @param gameid
     * @return
     */
    @Select("select * from card_game_rules where gameid=#{gameid}")
    List<CardGameRules> listGameRulesByGameId(Integer gameid);
}




