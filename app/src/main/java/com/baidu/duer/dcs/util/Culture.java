package com.baidu.duer.dcs.util;

public class Culture {

    private String title;
    private int img;
    private String content;

    public Culture(String name, int img, String content) {
        this.title = name;
        this.img = img;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
