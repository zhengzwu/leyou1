package com.search.pojo;

import java.util.Map;

public class SearchRequest {
    private Integer page;
    private String key;
    private static final Integer DEFAULT_PAGE= 1;
    private static final Integer DEFAULT_SIZE= 22;
    private Map<String,String> filter;
    public Integer getPage() {
        if (page==null) {
            return DEFAULT_PAGE;
        }
        return Math.max(page,DEFAULT_PAGE) ;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public Integer getSize() {
        return DEFAULT_SIZE;
    }

    public Map<String, String> getFilter() {
        return filter;
    }
}
