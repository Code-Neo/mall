package com.geekrem.mall.controller;

import com.geekrem.mall.bean.PmsBrand;
import com.geekrem.mall.common.CommonResult;
import com.geekrem.mall.service.PmsBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 品牌管理的controller
 */

@Api(tags = "PmsBrandController",value = "商品品牌管理")
@Controller
@Slf4j
@RequestMapping("/brand")
public class PmsBrandController {


    @Autowired
    private PmsBrandService pmsBrandService;

    @ApiOperation("查询所有商品品牌")
    @RequestMapping(value = "/listAll",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getBrandList(){
        List<PmsBrand> brands = pmsBrandService.listAllBrand();
        if ( brands!= null) {
            log.debug("getBrandList查询成功");
            return CommonResult.success("查询成功",brands);
        }else{
            log.error("getBrandList查询失败");
            return CommonResult.faile("查询失败",null);
        }
    }

    @ApiOperation("创建品牌")
    @RequestMapping(value="/create",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult createBrand(@RequestBody PmsBrand brand){
        int flag = pmsBrandService.createBrand(brand);
        if ( flag!= 0) {
            log.debug("createBrand创建成功");
            return CommonResult.success("创建成功",flag);
        }else{
            log.error("createBrand创建失败");
            return CommonResult.faile("创建失败",null);
        }

    }

    @ApiOperation("更新品牌")
    @RequestMapping(value = "/update/{id}",method = RequestMethod.PUT)
    @ResponseBody
    public CommonResult updateBrand(@PathVariable(value = "id") Long id, @RequestBody PmsBrand brand){
        int i = pmsBrandService.updateBrand(id, brand);
        if ( i!= 0) {
            log.debug("updateBrand更新成功");
            return CommonResult.success("更新成功",i);
        }else{
            log.error("updateBrand更新失败");
            return CommonResult.faile("更新失败",null);
        }
    }


    @ApiOperation("删除品牌")
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult deleteBrand(@PathVariable(value = "id") Long id){
        int i = pmsBrandService.deleteBrand(id);
        if ( i!= 0) {
            log.debug("deleteBrand删除成功");
            return CommonResult.success("删除成功",i);
        }else{
            log.error("deleteBrand删除失败");
            return CommonResult.faile("删除失败",null);
        }
    }

    @ApiOperation("分页查询品牌")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult listBrand(@RequestParam(value = "PageNum",defaultValue = "1") Integer PageNum,
                                  @RequestParam(value = "PageSize",defaultValue = "3") Integer PageSize){
        List<PmsBrand> brands = pmsBrandService.listBrand(PageNum, PageSize);
        if ( brands!= null) {
            log.debug("listBrand查询成功");
            return CommonResult.success("查询成功",brands);
        }else{
            log.error("listBrand查询失败");
            return CommonResult.faile("查询失败",null);
        }
    }

    @ApiOperation("根据Id查询品牌")
    @RequestMapping(value = "/brand/{id}",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getBrand(@PathVariable(value = "id") Long id){
        PmsBrand brand = pmsBrandService.getBrand(id);
        if (brand != null) {
            log.debug("getBrand查询成功");
            return CommonResult.success("查询成功",brand);
        }else{
            log.error("getBrand查询失败");
            return CommonResult.faile("查询失败",null);
        }
    }

}
