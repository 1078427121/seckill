package com.bytedance.summer.dao;

import com.bytedance.summer.entity.ProductMysql;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProductMapper {
    /*
    根据pid查询商品
     */
    @Select("select * from `product` where pid=#{pid}")
    ProductMysql getProduct(Long pid);
}
