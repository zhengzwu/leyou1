package com.search.client;

import com.leyou.pojo.Category;
import com.search.SearchApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApplication.class)
public class CategoryClientTest {
    @Resource
    private CategoryClient categoryClient;
    @Test
    public void testCate(){
       List<Category> listResponseEntity = categoryClient.queryByParentId(74L);
        for (Category category:listResponseEntity
             ) {
            System.out.print(category);
        }
    return;

    }

}