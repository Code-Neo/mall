package com.geekrem.mall.service.impl;

import com.geekrem.mall.bean.PmsBrand;
import com.geekrem.mall.bean.PmsBrandExample;
import com.geekrem.mall.mbg.mapper.PmsBrandMapper;
import com.geekrem.mall.service.PmsBrandService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PmsBrandServiceImpl implements PmsBrandService {

    @Autowired
    private PmsBrandMapper pmsBrandMapper;

    @Override
    public List<PmsBrand> listAllBrand() {
        List<PmsBrand> brands = pmsBrandMapper.selectByExample(new PmsBrandExample ());
        return brands;
    }

    @Override
    public int createBrand(PmsBrand brand) {
        int i = pmsBrandMapper.insertSelective(brand);
        return i;
    }

    @Override
    public int updateBrand(Long id, PmsBrand brand) {
        PmsBrandExample pmsBrandExample = new PmsBrandExample();
        pmsBrandExample.createCriteria().andIdEqualTo(id);

        int i = pmsBrandMapper.updateByExampleSelective(brand, pmsBrandExample);
        return i;
    }

    @Override
    public int deleteBrand(Long id) {
        int i = pmsBrandMapper.deleteByPrimaryKey(id);
        return i;
    }

    @Override
    public List<PmsBrand> listBrand(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<PmsBrand> pmsBrands = pmsBrandMapper.selectByExample(new PmsBrandExample());
        return pmsBrands;
    }

    @Override
    public PmsBrand getBrand(Long id) {
        PmsBrand brand = pmsBrandMapper.selectByPrimaryKey(id);
        return brand;
    }
}
