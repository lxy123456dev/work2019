package com.leyou.service;

public class SearchRequest {
    private String key;  //关键字
    private Integer page;   //当前页码
    //todo 其他过滤条件
    private static final Integer DEFAULT_PAGE = 1;
    private static final Integer DEFAULT_SIZE = 20;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        //判断当前页码 不能为null,不能小于零
        if(page==null){
            return DEFAULT_PAGE;
        }
        return Math.max(page, DEFAULT_PAGE);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize(){
        return DEFAULT_SIZE;
    }
}
