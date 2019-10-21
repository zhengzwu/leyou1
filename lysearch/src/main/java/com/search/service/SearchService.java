package com.search.service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.utils.JsonUtils;
import com.leyou.pojo.*;
import com.search.client.CategoryClient;
import com.search.client.GoodsClient;
import com.search.client.SkuClient;
import com.search.client.SpecificationClient;
import com.search.pojo.Goods;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {
    @Resource
    private CategoryClient categoryClient;
    @Resource
    private GoodsClient goodsClient;
    @Resource
    private SkuClient skuClient;
    @Resource
    private SpecificationClient specificationClient;

    public Goods buildGoods(Spu spu){
        Goods goods = new Goods();
        //商品分类名称
        List<Category> categories = categoryClient.queryNameById(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //查询详情
        List<String> names = categories.stream().map(Category::getName).collect(Collectors.toList());
        SpuDetail spuDetail = goodsClient.querySpuDetailBySpuId(spu.getId());
        List<Sku> skus = skuClient.querySkuBySpuId(spu.getId());
        System.out.print(skus);
        List<Long> prices = new ArrayList<>();
        List<Map<String,Object>> skulist = new ArrayList<>();
        for (Sku sku: skus
             ) {
            prices.add(sku.getPrice());
            Map<String,Object> map = new HashMap<>();
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            map.put("image", StringUtils.substringBefore(sku.getImages(),","));
            map.put("price",sku.getPrice());
            skulist.add(map);
        }
        //解析规格参数模板，分为通用参数generic和特殊specific
        Map<Long,String> genericSpec = JsonUtils.toMap(spuDetail.getGenericSpec(),Long.class,String.class);
        Map<Long,List<String>> specialSpec= JsonUtils.nativeRead(spuDetail.getSpecialSpec(),
                new TypeReference<Map<Long, List<String>>>(){});
        //key 规格参数名字 value  规格参数值
        Map<String,Object> specs = new HashMap<>();
        List<SpecParams> specParams = specificationClient.quertSpecParamBygid(null, spu.getCid3(),true);
        for (SpecParams s:specParams
             ) {
            String key = s.getName();
            Object value ;
            List<String> a;
            if(s.getGeneric()){
                System.out.print(s.getId());
                System.out.print(genericSpec);
                value = genericSpec.get(s.getId());
                String ca = value.toString();
                if(s.getNumberic()){
                   value=chooseSegment(value.toString(),s);
                }

            }else {
                value= specialSpec.get(s.getId());


            }
            specs.put(key,value);
        }
        goods.setId(spu.getId());
        goods.setAll(spu.getTitle()+""+StringUtils.join(names,""));
        goods.setSubTitle(spu.getSubTitle());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setPrice(prices);
        goods.setSkus(JsonUtils.toString(skulist));
        goods.setSpecs(specs);

        return goods;
    }
    private String chooseSegment(String value, SpecParams p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }
}
