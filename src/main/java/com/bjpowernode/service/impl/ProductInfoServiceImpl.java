package com.bjpowernode.service.impl;

import com.bjpowernode.mapper.ProductInfoMapper;
import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.ProductInfoExample;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import com.bjpowernode.service.ProductInfoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    private ProductInfoMapper productInfoMapper;
    @Override
    public List<ProductInfo> getAll() {

        return productInfoMapper.selectByExample(new ProductInfoExample());
    }

    @Override
    public PageInfo splitPage(int pageNum, int pageSize) {
//        分业务插件使用PageHelper工具类文成分页设置
//        pageNum表示当前的页数，pageSize表示每页显示几行
        PageHelper.startPage(pageNum,pageSize);

        ProductInfoExample example = new ProductInfoExample();
//        设置排序，按照主键降序排序
        example.setOrderByClause("p_id desc");
//        设置完排序后，取集合，切记一定在取集合之前，设置PageHelper.startPage(pageNum,PageSize)
        List<ProductInfo> list = productInfoMapper.selectByExample(example);
//        将查到的集合封装到PageInfo对象中
        PageInfo<ProductInfo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public int save(ProductInfo info) {
        return productInfoMapper.insert(info);
    }

    @Override
    public ProductInfo getByID(int pid) {

        return  productInfoMapper.selectByPrimaryKey(pid);
    }

    @Override
    public int update(ProductInfo info) {

        return productInfoMapper.updateByPrimaryKey(info);
    }

    @Override
    public int delete(int pid) {
        return productInfoMapper.deleteByPrimaryKey(pid);
    }

    @Override
    public int deleteBatch(String[] ids) {

        return productInfoMapper.deleteBatch(ids);
    }

    @Override
    public List<ProductInfo> selectCondition(ProductInfoVo vo) {
        return productInfoMapper.selectCondition(vo);
    }

    @Override
    public PageInfo<ProductInfo> splitPageVo(ProductInfoVo vo, int pageSize) {
//        取出集合之前按，先要设置PageHelper.startPage()属性
        PageHelper.startPage(vo.getPage(),pageSize);
        List<ProductInfo> list = productInfoMapper.selectCondition(vo);
        return new PageInfo<>(list);
    }
}
