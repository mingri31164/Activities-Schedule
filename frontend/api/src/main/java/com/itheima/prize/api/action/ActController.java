package com.itheima.prize.api.action;

import com.alibaba.fastjson.JSON;
import com.itheima.prize.api.config.LuaScript;
import com.itheima.prize.commons.config.RabbitKeys;
import com.itheima.prize.commons.config.RedisKeys;
import com.itheima.prize.commons.db.entity.*;
import com.itheima.prize.commons.db.service.CardGameRulesService;
import com.itheima.prize.commons.utils.ApiResult;
import com.itheima.prize.commons.utils.RedisUtil;
import com.ruoyi.common.enums.LotteryChainMarkEnum;
import com.ruoyi.framework.designpattern.chain.AbstractChainContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.lottery.domain.filter.LotteryActDTO;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/api/act")
@Api(tags = {"抽奖模块"})
@RequiredArgsConstructor
public class ActController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CardGameRulesService cardGameRulesService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private LuaScript luaScript;
    private final AbstractChainContext<LotteryActDTO> lotteryActAbstractChainContext;


    @GetMapping("/go/{gameid}")
    @ApiOperation(value = "抽奖")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameid",value = "活动id",example = "1",required = true)
    })
    public ApiResult<Object> act(@PathVariable int gameid, HttpServletRequest request){

        CardUser user = (CardUser) request.getSession().getAttribute("user");
        // 执行抽奖过滤器
        lotteryActAbstractChainContext.handler(LotteryChainMarkEnum.LOTTERY_ACT_MARK_ENUM.name(),
                new LotteryActDTO(String.valueOf(gameid), user));

        Date now = new Date();
        Integer userEnter = (Integer) redisUtil.get(RedisKeys.USERENTER + gameid + "_" + user.getId());
        if(userEnter == null){
            redisUtil.set(RedisKeys.USERENTER + gameid + "_" + user.getId(), 1);
            // mq保存参加活动记录
            CardUserGame cardUserGame = new CardUserGame();
            cardUserGame.setGameid(gameid);
            cardUserGame.setUserid(user.getId());
            cardUserGame.setCreatetime(now);
            String jsonString = JSON.toJSONString(cardUserGame);
            rabbitTemplate.convertAndSend(RabbitKeys.QUEUE_PLAY, jsonString);
        }
        redisUtil.incr(RedisKeys.USERENTER + gameid + "_" + user.getId(), 1);

        // 获取令牌
        Long token = luaScript.tokenCheck(RedisKeys.TOKENS + gameid ,String.valueOf(new Date().getTime()));
        if(token == 0){
            return new ApiResult(-1,"奖品已抽光",null);
        }else if(token == 1){
            return new ApiResult(0,"未中奖",null);
        }else{
            //token有效，中奖！
            String productKey = RedisKeys.TOKEN + gameid + "_" + token;
            CardProductDto product = (CardProductDto) redisUtil.get(productKey);
            redisUtil.incr(RedisKeys.USERHIT + gameid + "_" + user.getId(), 1);
            //mq保存中奖信息
            CardUserHit cardUserHit = new CardUserHit();
            cardUserHit.setGameid(gameid);
            cardUserHit.setHittime(now);
            cardUserHit.setUserid(user.getId());
            cardUserHit.setProductid(product.getId());
            String jsonString = JSON.toJSONString(cardUserHit);
            rabbitTemplate.convertAndSend(RabbitKeys.QUEUE_HIT, jsonString);

            return new ApiResult<>(1, "恭喜中奖", product);
        }
    }


    @GetMapping("/info/{gameid}")
    @ApiOperation(value = "查询缓存信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameid",value = "活动id",example = "1",required = true)
    })
    public ApiResult info(@PathVariable int gameid){
        String tokenKey = RedisKeys.TOKENS+gameid;
        Map map = new HashMap();
        Map maxGoals = new HashMap();
        Map maxEnters = new HashMap<>();
        // 创建一个 LinkedHashMap 以保持插入顺序
        Map<String, CardProductDto> productMap = new LinkedHashMap<>();

        //获取活动基本信息
        CardGame game = (CardGame) redisUtil.get(RedisKeys.INFO+gameid);
        if (game == null){
            return new ApiResult<>(-1,"活动未加载",null);
        }
        //获取活动策略
        List<CardGameRules> cardGameRulesList = cardGameRulesService.lambdaQuery()
                .eq(CardGameRules::getGameid,gameid).list();

        for (CardGameRules r : cardGameRulesList) {
            Integer maxgoal = (Integer) redisUtil.hget(RedisKeys.MAXGOAL + game.getId(), r.getUserlevel() + "");
            Integer maxenter = (Integer) redisUtil.hget(RedisKeys.MAXENTER + game.getId(), r.getUserlevel() + "");

            maxGoals.put(r.getUserlevel()+"",maxgoal);
            maxEnters.put(r.getUserlevel()+"",maxenter);
        }

        List<Long> tokenList = redisTemplate.opsForList().range(RedisKeys.TOKENS+gameid,0,-1);

            for (Long token : tokenList) { //遍历所有时间戳
                //还原真实时间戳
                long realTimeStamp = token/1000;
                // 将时间戳转换为可读的日期时间格式
                Date randomDate = new Date(realTimeStamp);

                // 创建 SimpleDateFormat 对象，设置日期格式（精确到毫秒）
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

               // 将 Date 对象格式化为可读的日期时间字符串
                String formattedDate = sdf.format(randomDate);

                CardProductDto productDto = (CardProductDto) redisUtil.get(RedisKeys.TOKEN+gameid+"_"+token);

                // 如果需要将 product 的所有内容放入 productMap
                productMap.put(formattedDate, productDto);
            }

        map.put(RedisKeys.INFO+gameid,game);
        map.put(RedisKeys.TOKENS+gameid,productMap);
        map.put(RedisKeys.MAXGOAL+gameid,maxGoals);
        map.put(RedisKeys.MAXENTER+gameid,maxEnters);

        return new ApiResult(200,"成功",map);
    }
}
