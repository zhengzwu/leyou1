package com.leyou.service;

import com.leyou.auth.entity.UserInfo;
import com.leyou.client.AddressClient;
import com.leyou.client.GoodsClient;
import com.leyou.client.SkuClient;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.IdWorker;
import com.leyou.dto.AddressDTO;
import com.leyou.dto.CartDTO;
import com.leyou.dto.OrderDTO;
import com.leyou.interceptor.UserInterceptor;
import com.leyou.mapper.OrderDetailMapper;
import com.leyou.mapper.OrderMapper;
import com.leyou.mapper.OrderStatusMapper;
import com.leyou.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Resource
    private GoodsClient goodsClient;
    @Autowired
    private IdWorker idWorker;
    @Resource
    private SkuClient skuClient;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderDetailMapper orderDetailMapper;
    @Resource
    private OrderStatusMapper orderStatusMapper;
    @Transactional
    public Long createOrder(OrderDTO orderDTO) {
        //1.1新增订单
        Order order = new Order();
        long orderId = idWorker.nextId();
        order.setOrderId(orderId);
        order.setCreateTime(new Date());
        order.setPaymentType(orderDTO.getPaymentType());
        //1.2用户信息
        UserInfo user = UserInterceptor.getLoginUser();
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());
        order.setBuyerRate(false);
        // 1.3 收货人地址信息 -- orderDTO中只有地址ID（addressID），要根据地址ID去数据库中查询(假数据)
        AddressDTO addr = AddressClient.findById(orderDTO.getAddressId());
        order.setReceiver(addr.getName());//收货人
        order.setReceiverMobile(addr.getPhone());//收货人手机号码
        order.setReceiverAddress(addr.getAddress());//收货所在街道
        order.setReceiverState(addr.getState());//收货人所在省
        order.setReceiverCity(addr.getCity());//收货人所在城市
        order.setReceiverDistrict(addr.getDistrict());//收货人所在区
        order.setReceiverZip(addr.getZipCode());//收货人邮编
        //金额
        Map<Long,Integer> numMap= orderDTO.getCarts()
                .stream().collect(Collectors.toMap(CartDTO::getSkuId,CartDTO::getNum));
        Set<Long> ids = numMap.keySet();

        List<Sku> skus = skuClient.querySkuBySkuId(new ArrayList<>(ids));
        System.out.println(skus);
        List<OrderDetail> details = new ArrayList<>();
        Long totalPrice = 0L;
        for (Sku sku:skus
             ) {
            totalPrice += sku.getPrice() * numMap.get(sku.getId());

            //封装orderDetail
            OrderDetail detail = new OrderDetail();
            detail.setImage(StringUtils.substringBefore(sku.getImages(),","));
            detail.setNum(numMap.get(sku.getId()));
            detail.setOrderId(orderId);
            detail.setOwnSpec(sku.getOwnSpec());
            detail.setPrice(sku.getPrice());
            detail.setSkuId(sku.getId());
            detail.setTitle(sku.getTitle());
            details.add(detail);
        }
        order.setTotalPay(totalPrice);
        order.setActualPay(totalPrice + order.getPostFee() - 0 );// 实付金额= 总金额 + 邮费 - 优惠金额
        int count = orderMapper.insertSelective(order);
        if(count != 1){

            throw new LyException(ExceptionEnum.CREATE_ORDER_ERROR);
        }
        count = orderDetailMapper.insertList(details);
        if(count != details.size()){

            throw new LyException(ExceptionEnum.CREATE_ORDER_ERROR);
        }
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCreateTime(order.getCreateTime());
        orderStatus.setStatus(OrderStatusEnum.UN_PAY.value());
        count = orderStatusMapper.insertSelective(orderStatus);
        if(count != 1){
            throw new LyException(ExceptionEnum.CREATE_ORDER_ERROR);
        }

        // 4 减库存 -- 需要调用商品微服务，传递商品id和数量两个参数
        List<CartDTO> cartDTOS = orderDTO.getCarts();
        goodsClient.decreaseStock(cartDTOS);

        return orderId;
    }
}
