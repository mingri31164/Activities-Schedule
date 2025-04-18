package com.mingri.prize.api.action;

import com.mingri.prize.commons.db.entity.*;
import com.mingri.prize.commons.db.entity.CardProductDto;
import com.mingri.prize.commons.db.entity.ViewCardUserHit;
import com.mingri.prize.commons.db.service.*;
import com.mingri.prize.commons.db.service.GameLoadService;
import com.mingri.prize.commons.utils.ApiResult;
import com.mingri.prize.commons.utils.PageBean;
import com.mingri.prize.commons.db.entity.CardGame;
import com.mingri.prize.commons.db.service.CardGameProductService;
import com.mingri.prize.commons.db.service.CardGameService;
import com.mingri.prize.commons.db.service.ViewCardUserHitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/game")
@Api(tags = {"活动模块"})
public class GameController {
    @Autowired
    private GameLoadService loadService;
    @Autowired
    private CardGameService gameService;
    @Autowired
    private ViewCardUserHitService viewCardUserHitService;


    @GetMapping("/list/{status}/{curpage}/{limit}")
    @ApiOperation(value = "活动列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="status",value = "活动状态（-1=全部，0=未开始，1=进行中，2=已结束）",example = "-1",required = true),
            @ApiImplicitParam(name = "curpage",value = "第几页",defaultValue = "1",dataType = "int", example = "1",required = true),
            @ApiImplicitParam(name = "limit",value = "每页条数",defaultValue = "10",dataType = "int",example = "3",required = true)
    })
    public ApiResult list(@PathVariable int status, @PathVariable int curpage, @PathVariable int limit) {
        PageBean<CardGame> pageBean = gameService.pageGame(status,curpage,limit);
        return new ApiResult(1,"成功",pageBean);
    }

    @GetMapping("/info/{gameid}")
    @ApiOperation(value = "活动信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameid",value = "活动id",example = "1",required = true)
    })
    public ApiResult<CardGame> info(@PathVariable int gameid) {
        CardGame cardGame = gameService.getById(gameid);
        return new ApiResult(1,"成功",cardGame);
    }

    @GetMapping("/products/{gameid}")
    @ApiOperation(value = "奖品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameid",value = "活动id",example = "1",required = true)
    })
    public ApiResult<List<CardProductDto>> products(@PathVariable int gameid) {
//      List<CardProductDto> gameProductList = GameProductService.listGameProductsByGameId(gameid);
        List<CardProductDto> gameProductList = loadService.getByGameId(gameid);
        if (gameProductList.isEmpty()) {
            return new ApiResult<>(0, "该活动没有相关奖品", null);
        }
        return new ApiResult<>(1, "成功", gameProductList);
    }

    @GetMapping("/hit/{gameid}/{curpage}/{limit}")
    @ApiOperation(value = "中奖列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameid",value = "活动id",dataType = "int",example = "1",required = true),
            @ApiImplicitParam(name = "curpage",value = "第几页",defaultValue = "1",dataType = "int", example = "1",required = true),
            @ApiImplicitParam(name = "limit",value = "每页条数",defaultValue = "10",dataType = "int",example = "3",required = true)
    })
    public ApiResult<PageBean<ViewCardUserHit>> hit(@PathVariable int gameid, @PathVariable int curpage, @PathVariable int limit) {
        PageBean<ViewCardUserHit> pageBean = viewCardUserHitService.pageUserHit(gameid,curpage,limit);
        return new ApiResult<>(1,"成功",pageBean);
    }
}