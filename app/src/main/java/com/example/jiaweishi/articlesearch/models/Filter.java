package com.example.jiaweishi.articlesearch.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jiaweishi on 2/13/16.
 */
public class Filter {
    private Date beginDate;
    private String sortOrder;
    private List<String> categories;

    public Filter(){
        categories = new ArrayList<>();
    }

    public void setBeginDate(String input){
        try {
            beginDate = new SimpleDateFormat("MM/dd/yyyy").parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
