package com.mingri.prize.api.action;

import com.mingri.prize.commons.constant.MessageConstant;
import com.mingri.prize.commons.db.entity.CardUser;
import com.mingri.prize.commons.db.entity.CardUserDto;
import com.mingri.prize.commons.db.entity.ViewCardUserHit;
import com.mingri.prize.commons.db.service.GameLoadService;
import com.mingri.prize.commons.db.service.ViewCardUserHitService;
import com.mingri.prize.commons.utils.ApiResult;
import com.mingri.prize.commons.utils.PageBean;
import com.mingri.prize.commons.utils.RedisUtil;
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

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping(value = "/api/user")
@Api(tags = {"用户模块"})
public class UserController {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ViewCardUserHitService hitService;
    @Autowired
    private GameLoadService loadService;

    @GetMapping("/info")
    @ApiOperation(value = "用户信息")
    public ApiResult info(HttpServletRequest request) {

        CardUser user = (CardUser) request.getSession().getAttribute("user");

        CardUserDto cardUserDto = new CardUserDto(user);
        if (user == null) {
            return new ApiResult(0,MessageConstant.USER_NOT_LOGIN,null);
        }

        //获取当前用户参与的活动数和中奖数
        cardUserDto.setGames(loadService.getGamesNumByUserId(user.getId()));
        cardUserDto.setProducts(loadService.getPrizesNumByUserId(user.getId()));

        return new ApiResult(1,"成功",cardUserDto);
    }

    @GetMapping("/hit/{gameid}/{curpage}/{limit}")
    @ApiOperation(value = "我的奖品")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameid",value = "活动id（-1=全部）",dataType = "int",example = "1",required = true),
            @ApiImplicitParam(name = "curpage",value = "第几页",defaultValue = "1",dataType = "int", example = "1"),
            @ApiImplicitParam(name = "limit",value = "每页条数",defaultValue = "10",dataType = "int",example = "3")
    })
    public ApiResult hit(@PathVariable int gameid,@PathVariable int curpage,@PathVariable int limit,HttpServletRequest request) {
        CardUser cardUser = (CardUser) request.getSession().getAttribute("user");
        if (cardUser == null) {
            return new ApiResult(0,MessageConstant.USER_NOT_LOGIN,null);
        }
        PageBean<ViewCardUserHit> pageBean = hitService.pageUserPrize(gameid,curpage,limit,cardUser);
       return new ApiResult(1,"成功",pageBean);
    }
}