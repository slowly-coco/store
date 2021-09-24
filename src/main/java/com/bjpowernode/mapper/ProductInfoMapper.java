package com.bjpowernode.mapper;

import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.ProductInfoExample;
import java.util.List;

import com.bjpowernode.pojo.vo.ProductInfoVo;
import org.apache.ibatis.annotations.Param;

public interface ProductInfoMapper {
    long countByExample(ProductInfoExample example);

    int deleteByExample(ProductInfoExample example);

    int deleteByPrimaryKey(Integer pId);

    int insert(ProductInfo record);

    int insertSelective(ProductInfo record);

    List<ProductInfo> selectByExample(ProductInfoExample example);

    ProductInfo selectByPrimaryKey(Integer pId);

    int updateByExampleSelective(@Param("record") ProductInfo record, @Param("example") ProductInfoExample example);

    int updateByExample(@Param("record") ProductInfo record, @Param("example") ProductInfoExample example);

    int updateByPrimaryKeySelective(ProductInfo record);

    int updateByPrimaryKey(ProductInfo record);

    int deleteBatch(String []ids);

//    多条件查询商品
    List<ProductInfo> selectCondition(ProductInfoVo vo);
}