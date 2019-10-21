package com.search.searching.web;

import com.leyou.common.vo.PageResult;
import com.search.pojo.Goods;
import com.search.pojo.SearchRequest;
import com.search.pojo.SearchResult;
import com.search.searching.service.SearchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class RequestSearch {
    @Autowired
    private SearchingService searchingService;
    @PostMapping("page")
    public ResponseEntity<SearchResult> search(@RequestBody SearchRequest request){
        System.out.print(request.getKey());
        SearchResult search = searchingService.search(request);

        return ResponseEntity.ok(search);
    }
}
