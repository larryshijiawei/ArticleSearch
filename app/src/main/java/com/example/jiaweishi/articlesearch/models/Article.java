package com.example.jiaweishi.articlesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jiaweishi on 2/10/16.
 */
public class Article {
    private final String URL_PREFIX = "http://www.nytimes.com/";

    private String headline;
    private String imageLink;

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
}
