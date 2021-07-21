package com.baidu.duer.dcs.util;

import java.util.List;

public class Image {
    private String request_id;
    private String image_id;
    private int time_used;
   // private String error_message;
    private List<Faces> faces;

    public void setFaces(List<Faces> faces) {
        this.faces = faces;
    }

    public List<Faces> getFaces() {
        return faces;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

//    public String getError_message() {
//        return error_message;
//    }
//
//    public void setError_message(String error_message) {
//        this.error_message = error_message;
//    }

}
