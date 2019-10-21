package com.leyou.service;

import com.leyou.client.BrandClient;
import com.leyou.client.CategoryClient;
import com.leyou.client.GoodClient;
import com.leyou.client.SpecificationClient;
import com.leyou.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class PageService {
    @Resource
    private BrandClient brandClient;
    @Resource
    private CategoryClient categoryClient;
    @Resource
    private GoodClient goodClient;
    @Resource
    private SpecificationClient specificationClient;
    @Autowired
    private TemplateEngine templateEngine;
    public Map<String,Object> loadModel(Long id) {
        Map<String,Object> model = new HashMap<>();
        Spu spu = goodClient.queryById(id);
        List<Sku> skus = spu.getSkus();
        SpuDetail detail = spu.getSpuDetail();
        List<Category> categories = categoryClient.queryNameById(Arrays.asList(spu.getCid1(),
                spu.getCid2(), spu.getCid3()));
        Brands brand = brandClient.queryBrandById(spu.getBrandId());
        String title = spu.getTitle();
        String subTitle = spu.getSubTitle();
        List<SpecGroup> specs = specificationClient.queryListByCid(spu.getCid3());
        model.put("title", spu.getTitle());
        model.put("subTitle", spu.getSubTitle());
        model.put("skus", skus);
        model.put("detail", detail);
        model.put("brand", brand);
        model.put("categories", categories);
        model.put("specs", specs);

        return model;

    }
    public void createHtml(Long id){
        Context context = new Context();
        context.setVariables(loadModel(id));
        File dest = new File("F:\\html",id + ".html");
        if(dest.exists()){
            dest.delete();
        }
        try (PrintWriter writer = new PrintWriter(dest, "UTF-8")){
            // 生成html
            templateEngine.process("item", context, writer);
        }catch (Exception e){
            System.out.print("yichang");
        }
}}
