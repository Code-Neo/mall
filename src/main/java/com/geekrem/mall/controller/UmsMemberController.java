package com.geekrem.mall.controller;

import com.geekrem.mall.common.CommonResult;
import com.geekrem.mall.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(tags = "电话验证码验证管理（Redis）")
@Controller
@RequestMapping("/sso")
@Slf4j
public class UmsMemberController {

      @Autowired
      private UmsMemberService umsMemberService;

      @ApiOperation ("获取验证码")
      @RequestMapping(value = "/getAuthCode",method = RequestMethod.GET)
      @ResponseBody
      public CommonResult getAuthCode(@RequestParam String phoneNum){
            return umsMemberService.generateAuthCode(phoneNum);
      }

      @ApiOperation ("判断验证码是否正确")
      @RequestMapping(value = "/verifyAuthCode",method = RequestMethod.POST)
      @ResponseBody
      public CommonResult updatePassword(@RequestParam String phoneNum,
                                         @RequestParam String authCode){
            log.debug ("电话为：{}，验证码为：{}",phoneNum,authCode);
            return umsMemberService.verifyAuthCode(phoneNum,authCode);
      }
}
