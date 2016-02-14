package com.example.jiaweishi.articlesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by jiaweishi on 2/10/16.
 */
public class Article implements Serializable{
    private final String URL_PREFIX = "http://www.nytimes.com/";

    private String headline;
    private String imageLink;
    private String webUrl;

    public Article(JSONObject json){
        try {
            headline = json.getJSONObject("headline").getString("main");

            JSONArray multiMedia = json.getJSONArray("multimedia");
            for(int i=0; i<multiMedia.length(); i++){
                JSONObject media = multiMedia.getJSONObject(i);
                if(media.getString("subtype").equals("thumbnail")){
                    this.imageLink = URL_PREFIX + media.getString("url");
                    break;
                }
            }

            webUrl = json.getString("web_url");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getHeadline() {
        return headline;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getWebUrl(){
        return this.webUrl;
    }
}
