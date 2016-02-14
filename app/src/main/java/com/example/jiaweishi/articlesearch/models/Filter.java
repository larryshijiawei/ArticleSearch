package com.example.jiaweishi.articlesearch.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jiaweishi on 2/13/16.
 */
public class Filter {
    private Date beginDate;
    private String sortOrder = "newest";
    private List<String> categories;

    public Filter(){
        categories = new ArrayList<>();
    }

    public void setBeginDate(Date date){
        beginDate = date;
    }

    public void setSortOrder(String order){
        //if(order != null)
            sortOrder = order;
    }

    public void addCategory(String category){
        categories.add(category);
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public List<String> getCategories() {
        return categories;
    }
}
