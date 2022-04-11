package com.bytedance.summer.dao;

import com.bytedance.summer.entity.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author pjl
 * @date 2019/8/12 14:28
 * 订单相关mapper
 */
@Mapper
public interface OrderMapper {
    /*
    返回全部订单
     */
    @Select("select * from `order`")
    List<Order> getAllOrders();

    /*
    根据order_id查询订单
     */
    @Select("select * from `order` where order_id=#{order_id}")
    Order getOrderById(String order_id);

    @Delete("delete from `order` where order_id=#{order_id}")
    int deleteOrderById(String order_id);

    /*
    新增订单
     */
    @Insert("insert into `order` (order_id,pid,uid,price,detail) values (#{order_id},#{pid},#{uid},#{price},#{detail})")
    int insertOrder(Order order);

    /*
    更新支付信息
     */
    @Update("update `order` set status=#{status},token=#{token} where order_id=#{order_id}")
    int pay(String order_id,Integer status,String token);
}
