package com.pag.socialz.Models;

import com.pag.socialz.Enums.ItemType;
import com.pag.socialz.Util.FormatterUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Message implements Serializable, LazyLoading{

    private String id;
    private String text;
    private String imagePath;
    private String authorId;

    private boolean isReceived;

    private long sendDate;

    private ItemType itemType;

    public Message() {
        this.sendDate = new Date().getTime();
        itemType = ItemType.ITEM;
    }

    public Message(ItemType itemType){
        this.itemType = itemType;
        setId(itemType.toString());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imageUri) {
        this.imagePath = imageUri;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public boolean isReceived() {
        return isReceived;
    }

    public void setReceived(boolean received) {
        isReceived = received;
    }

    public long getSendDate() {
        return sendDate;
    }

    public void setSendDate(long sendDate) {
        this.sendDate = sendDate;
    }

    @Override
    public ItemType getItemType() {
        return itemType;
    }

    @Override
    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("text",text);
        result.put("imageUri",imagePath);
        result.put("authorId",authorId);
        result.put("isReceived",isReceived);
        result.put("sendDate",FormatterUtil.getFirebaseDateFormat().format(new Date(sendDate)));
        return result;
    }


}
