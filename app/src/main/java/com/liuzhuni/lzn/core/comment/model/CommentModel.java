package com.liuzhuni.lzn.core.comment.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Andrew Lee on 2015/7/21.
 * E-mail:jieooo7@163.com
 * Date: 2015-07-21
 * Time: 10:17
 */
public class CommentModel implements Serializable{



    private int id;
    private String createTime;
    private String text;
    private String userNick;
    private String orderNum;
    private String userPic;
    private List<ReplyModel> comment;


    public CommentModel(int id, String createTime, String text, String userNick, String orderNum, String userPic, List<ReplyModel> comment) {
        this.id = id;
        this.createTime = createTime;
        this.text = text;
        this.userNick = userNick;
        this.orderNum = orderNum;
        this.userPic = userPic;
        this.comment = comment;
    }





    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public List<ReplyModel> getComment() {
        return comment;
    }

    public void setComment(List<ReplyModel> comment) {
        this.comment = comment;
    }
}
