package com.reeching.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/6.
 */

public class HuaLangShowing implements Serializable {

    private String result;

    private String msg;

    private List<HuaLangShowing.Infos> infos;

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return this.result;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setInfos(List<HuaLangShowing.Infos> infos) {
        this.infos = infos;
    }

    public List<HuaLangShowing.Infos> getInfos() {
        return this.infos;
    }

    public class Infos implements Serializable{

        private String exhibitionId;//展览id
        private String remarks;
       private String exhibitionIntroduction;
        private String galleryId;		// 画廊id
        private String theme;		// 展览主题
        private String status;		// 状态  1:已完成  0：进行中
        private String  dateBegin;		// 开始时间
        private String dateEnd;		// 结束时间
        private String careLevel;		//关注等级  0:低 1：中 2：高
        private String photo;		// 作品介绍
        private String photoFalse;//问题图片
        private String smallPhoto;//小图
        private String smallPhotoFalse;//问题小图
        private  String author;		// 作者
        private String authorIntroduction;		// 作者简介
        private String manager;		// 策展人
        private String managerIntroduction;		// 策展人简介
        private String galleryName;   //画廊名称
        private  String userId;   //上报人id
        private String userName;    //上报人姓名
        private String mapLng;    //经度
        private String mapLat;    //纬度
        private String address;   //地址
        private String checkStatus;  //0：检查 1：核查
        private String name;
        private String createDate;
        private String id;
        private String linkMan;
        private String liveTime;
        private String description;
        private String mobile;
        private String questionRemarks;

        public void setQuestionRemarks(String questionRemarks) {
            this.questionRemarks = questionRemarks;
        }

        public String getQuestionRemarks() {

            return questionRemarks;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public void setLinkMan(String linkMan) {
            this.linkMan = linkMan;
        }

        public void setLiveTime(String liveTime) {
            this.liveTime = liveTime;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getName() {
            return name;
        }

        public String getCreateDate() {
            return createDate;
        }

        public String getLinkMan() {
            return linkMan;
        }

        public String getLiveTime() {
            return liveTime;
        }

        public String getDescription() {
            return description;
        }

        public String getMobile() {
            return mobile;
        }

        public String getGalleryId() {
            return galleryId;
        }

        public void setGalleryId(String galleryId) {
            this.galleryId = galleryId;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public String getStatus() {
            return status;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSmallPhotoFalse() {
            return smallPhotoFalse;
        }

        public void setSmallPhotoFalse(String smallPhotoFalse) {
            this.smallPhotoFalse = smallPhotoFalse;
        }

        public void setExhibitionId(String exhibitionId) {
            this.exhibitionId = exhibitionId;
        }

        public String getExhibitionId() {

            return exhibitionId;
        }

        public String getExhibitionIntroduction() {
            return exhibitionIntroduction;
        }

        public void setExhibitionIntroduction(String exhibitionIntroduction) {
            this.exhibitionIntroduction = exhibitionIntroduction;
        }

        public String getId() {

            return id;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDateBegin() {
            return dateBegin;
        }

        public void setDateBegin(String dateBegin) {
            this.dateBegin = dateBegin;
        }

        public String getDateEnd() {
            return dateEnd;
        }

        public void setDateEnd(String  dateEnd) {
            this.dateEnd = dateEnd;
        }

        public String getCareLevel() {
            return careLevel;
        }

        public void setCareLevel(String careLevel) {
            this.careLevel = careLevel;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getAuthor() {
            return author;
        }

        public String getPhotoFalse() {
            return photoFalse;
        }

        public void setPhotoFalse(String photoFalse) {

            this.photoFalse = photoFalse;
        }

        public String getSmallPhoto() {
            return smallPhoto;
        }

        public void setSmallPhoto(String smallPhoto) {
            this.smallPhoto = smallPhoto;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAuthorIntroduction() {
            return authorIntroduction;
        }

        public void setAuthorIntroduction(String authorIntroduction) {
            this.authorIntroduction = authorIntroduction;
        }

        public String getManager() {
            return manager;
        }

        public void setManager(String manager) {
            this.manager = manager;
        }

        public String getManagerIntroduction() {
            return managerIntroduction;
        }

        public void setManagerIntroduction(String managerIntroduction) {
            this.managerIntroduction = managerIntroduction;
        }

        public String getGalleryName() {
            return galleryName;
        }

        public void setGalleryName(String galleryName) {
            this.galleryName = galleryName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getMapLng() {
            return mapLng;
        }

        public void setMapLng(String mapLng) {
            this.mapLng = mapLng;
        }

        public String getMapLat() {
            return mapLat;
        }

        public void setMapLat(String mapLat) {
            this.mapLat = mapLat;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCheckStatus() {
            return checkStatus;
        }

        public void setCheckStatus(String checkStatus) {
            this.checkStatus = checkStatus;
        }
    }

}
