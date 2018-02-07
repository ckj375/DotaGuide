package com.ckj.dotaguide.model;

import java.util.ArrayList;

/**
 * Created by chenkaijian on 17-3-7.
 */

public class Hero {

    private String name;
    private String portraitUrl;
    private String coverUrl;
    // 推荐加点方案
    private ArrayList<String> skillOrderList;
    private String skillOrderDesc;

    public Hero() {
    }

    public Hero(String name, String portraitUrl) {
        this.name = name;
        this.portraitUrl = portraitUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setSkillOrderList(ArrayList<String> skillOrderList) {
        this.skillOrderList = skillOrderList;
    }

    public ArrayList<String> getSkillOrderList() {
        return skillOrderList;
    }

//    public String toString() {
//        StringBuilder hero = new StringBuilder();
//        hero.append("name:" + name + "\n");
//        hero.append("portraitUrl:" + portraitUrl + "\n");
//        hero.append("coverUrl:" + coverUrl + "\n");
//        for (String item : skillOrderList) {
//            hero.append("skillOrderList:" + item + "\n");
//        }
//        return hero.toString();
//    }
}
