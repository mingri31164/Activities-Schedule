package com.mingri.prize.api.action;

import com.alibaba.fastjson.JSON;
import com.mingri.prize.api.config.LuaScript;
import com.mingri.prize.commons.annotition.SlideLimiter;
import com.mingri.prize.commons.config.RabbitKeys;
import com.mingri.prize.commons.config.RedisKeys;
import com.mingri.prize.commons.db.entity.*;
import com.mingri.prize.commons.db.enums.LotteryChainMarkEnum;
import com.mingri.prize.commons.db.service.CardGameRulesService;
import com.mingri.prize.commons.db.service.CardGameService;
import com.mingri.prize.commons.db.service.GameLoadService;
import com.mingri.prize.commons.db.service.handler.lottery.dto.LotteryActDTO;
import com.mingri.prize.commons.utils.ApiResult;
import com.mingri.prize.commons.utils.RedisUtil;
import com.mingri.prize.framework.starter.designpattern.chain.AbstractChainContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import javafx.util.Pair;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/api/act")
@Api(tags = {"抽奖模块"})
@RequiredArgsConstructor
public class ActController {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private LuaScript luaScript;
    @Autowired
    private CardGameRulesService gameRulesService;
    @Autowired
    private GameLoadService gameLoadService;
    @Autowired
    private CardGameService gameService;

    private final AbstractChainContext<LotteryActDTO> lotteryActDTOAbstractChainContext;


    @SlideLimiter(
            key = "lottery:ip:",
            window = 60,
            limit = 100,
            dimension = "#request.remoteAddr"
    )
    @GetMapping("/go/{gameid}")
    @ApiOperation(value = "抽奖")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameid",value = "活动id",example = "1",required = true)
    })
    public ApiResult<Object> act(@PathVariable int gameid, HttpServletRequest request){

        CardUser user = (CardUser) request.getSession().getAttribute("user");
        lotteryActDTOAbstractChainContext.handler
                (LotteryChainMarkEnum.LOTTERY_ACT_FILTER.name(), new LotteryActDTO(user, gameid, request));

        // 增加用户参与抽奖次数
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
            log.info("恭喜中奖！,key = {}, gameId = {}", productKey, product.getId());

            //mq保存中奖信息
            CardUserHit cardUserHit = CardUserHit.builder()
                    .gameid(gameid)
                    .hittime(new Date())
                    .userid(user.getId())
                    .productid(product.getId())
                    .build();
            String jsonString = JSON.toJSONString(cardUserHit);
            rabbitTemplate.convertAndSend(RabbitKeys.QUEUE_HIT, jsonString);

            return new ApiResult<>(1, "恭喜中奖", product);
        }

    }


    @GetMapping("/limits/{gameid}")
    @ApiOperation(value = "剩余次数")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameid",value = "活动id",example = "1",required = true)
    })
    public ApiResult<Object> limits(@PathVariable int gameid, HttpServletRequest request){
        //获取活动基本信息
        CardGame game = (CardGame) redisUtil.get(RedisKeys.INFO+gameid);
        if (game == null){
            return new ApiResult<>(-1,"活动未加载",null);
        }
        //获取当前用户
        HttpSession session = request.getSession();
        CardUser user = (CardUser) session.getAttribute("user");
        if (user == null){
            return new ApiResult(-1,"未登陆",null);
        }
        //用户可抽奖次数
        Integer enter = (Integer) redisUtil.get(RedisKeys.USERENTER+gameid+"_"+user.getId());
        if (enter == null){
            enter = 0;
        }
        //根据会员等级，获取本活动允许的最大抽奖次数
        Integer maxenter = (Integer) redisUtil.hget(RedisKeys.MAXENTER+gameid,user.getLevel()+"");
        //如果没设置，默认为0，即：不限制次数
        maxenter = maxenter==null ? 0 : maxenter;

        //用户已中奖次数
        Integer count = (Integer) redisUtil.get(RedisKeys.USERHIT+gameid+"_"+user.getId());
        if (count == null){
            count = 0;
        }
        //根据会员等级，获取本活动允许的最大中奖数
        Integer maxcount = (Integer) redisUtil.hget(RedisKeys.MAXGOAL+gameid,user.getLevel()+"");
        //如果没设置，默认为0，即：不限制次数
        maxcount = maxcount==null ? 0 : maxcount;

        //幸运转盘类，先给用户随机剔除，再获取令牌，有就中，没有就说明抢光了
        //一般这种情况会设置足够的商品，卡在随机上
        Integer randomRate = (Integer) redisUtil.hget(RedisKeys.RANDOMRATE+gameid,user.getLevel()+"");
        if (randomRate == null){
            randomRate = 100;
        }

        Map map = new HashMap();
        map.put("maxenter",maxenter);
        map.put("enter",enter);
        map.put("maxcount",maxcount);
        map.put("count",count);
        map.put("randomRate",randomRate);

        return new ApiResult<>(1,"成功",map);
    }



    @GetMapping("/info/{gameid}")
    @ApiOperation(value = "查询缓存信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameid",value = "活动id",example = "1",required = true)
    })
    public ApiResult info(@PathVariable int gameid){
        Map resMap = new LinkedHashMap();
        Map tokenMap = new LinkedHashMap();

        Object gameInfo = redisUtil.get(RedisKeys.INFO + gameid);
        Map<Object, Object> maxGoalMap = redisUtil.hmget(RedisKeys.MAXGOAL + gameid);
        Map<Object, Object> maxEnterMap = redisUtil.hmget(RedisKeys.MAXENTER + gameid);
        List<Object> tokenList = redisUtil.lrange(RedisKeys.TOKENS + gameid, 0, -1);


        resMap.put(RedisKeys.INFO + gameid, gameInfo);
        resMap.put(RedisKeys.MAXGOAL + gameid, maxGoalMap);
        resMap.put(RedisKeys.MAXENTER + gameid, maxEnterMap);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm: ss.SSS");
        for (Object item : tokenList) {
            Object o = redisUtil.get(RedisKeys.TOKEN + gameid + "_" + item.toString());
            Long key = Long.valueOf(item.toString());
            Date date = new Date(key / 1000);
            String formattedDate = dateFormat.format(date);
            tokenMap.put(formattedDate, o);
        }
        resMap.put(RedisKeys.TOKENS + gameid, tokenMap);
        return new ApiResult(200, "缓存信息", resMap);
    }





    @PostMapping("/info/{gameid}")
    @ApiOperation(value = "手动缓存信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameid",value = "活动id",example = "1",required = true)
    })
    public ApiResult cache(@PathVariable int gameid){
        Map resMap = new LinkedHashMap();

        System.out.printf("scheduled!"+new Date());
        List<CardGame> gameList = gameService.list();

        // 将当前时间加一分钟
        long oneMinuteInMillis = 1 * 60 * 1000; // 一分钟的毫秒数
        Date now = new Date();
        Date newDate = new Date(now.getTime() + oneMinuteInMillis);

        List<CardGameRules> ruleList = gameRulesService.list();
        Map<Integer, List<CardGameRules>> map = new HashMap<>();
        for (CardGameRules rule : ruleList) {
            List list = map.get(rule.getGameid());
            if(list == null){
                list = new ArrayList();
            }
            list.add(rule);
            map.put(rule.getGameid(), list);
        }


        for (CardGame game : gameList) {
            if(game.getId() == gameid){
                // 缓存活动信息
                redisUtil.set(RedisKeys.INFO + game.getId(), game, -1);
                resMap.put(RedisKeys.INFO + game.getId(), game);


                // 缓存活动策略信息
                List<CardGameRules> rulesList = map.get(game.getId());
                Map maxGoalMap = new HashMap();
                Map maxEnterMap = new HashMap();
                for (CardGameRules r : rulesList) {
                    redisUtil.hset(RedisKeys.MAXGOAL + game.getId(), r.getUserlevel() + "", r.getGoalTimes());
                    redisUtil.hset(RedisKeys.MAXENTER + game.getId(), r.getUserlevel() + "", r.getEnterTimes());
                    maxGoalMap.put(r.getUserlevel(), r.getGoalTimes());
                    maxEnterMap.put(r.getUserlevel(), r.getEnterTimes());
                }
                resMap.put(RedisKeys.MAXGOAL + game.getId(), maxGoalMap);
                resMap.put(RedisKeys.MAXENTER + game.getId(), maxEnterMap);


                // 缓存令牌桶
                List<CardProductDto> productList = gameLoadService.getByGameId(game.getId());
                long sum = 0;
                for (CardProductDto item : productList) {
                    sum += item.getAmount();
                }
                List<Long> tokenList = generateToken(sum, game.getStarttime().getTime(), game.getEndtime().getTime());
                List<Pair<Long, CardProductDto>> pairList = new ArrayList<>();
                int idx = 0;
                for (CardProductDto item : productList) {
                    for (Integer j = 0; j < item.getAmount(); j++) {
                        pairList.add(new Pair(tokenList.get(idx) , item));
                        redisUtil.set(RedisKeys.TOKEN + game.getId() + "_" + tokenList.get(idx), item, 240);//单位:秒
                        idx ++;
                    }
                }
                tokenList.sort(Comparator.comparing(Long::valueOf));
                redisUtil.rightPushAll(RedisKeys.TOKENS + game.getId(), tokenList);
                for (Pair<Long, CardProductDto> pair : pairList) {
                    Date date = new Date(pair.getKey() / 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    String formattedDate = dateFormat.format(date);
                    resMap.put(formattedDate, pair.getValue());
                }


            }
        }
        return new ApiResult(200, "缓存信息", resMap);
    }




    public static List<Long> generateToken(long length, long start, long end){
        long duration = end - start;
        List<Long> list = new ArrayList<>();
        for (long i = 0; i < length; i++) {
            //活动持续时间（ms）
            long rnd = start + new Random().nextInt((int)duration);
            //为什么乘1000，再额外加一个随机数呢？ - 防止时间段奖品多时重复
            long token = rnd * 1000 + new Random().nextInt(999);
            list.add(token);
        }
        return list;
    }

}
