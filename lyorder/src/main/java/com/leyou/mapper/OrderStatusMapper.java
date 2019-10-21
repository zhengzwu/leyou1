package com.leyou.mapper;

import com.leyou.pojo.OrderStatus;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface OrderStatusMapper  extends Mapper<OrderStatus>,IdListMapper<OrderStatus,Long>,InsertListMapper<OrderStatus> {
}