package com.reeching.bluegrass;

import java.io.Serializable;

public class PostInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] uploadType = null;
	private String[] picName;
	private String[] picArrayPath;
	private String galleryId; // 画廊id
	private String theme; // 展览主题
	private String status; // 状态 1:已完成 0：进行中
	private String dateBegin; // 开始时间
	private String dateEnd; // 结束时间
	private String careLevel; // 关注等级 0:低 1：中 2：高
	private String author; // 作者
	private String authorIntroduction; // 作者简介
	private String manager; // 策展人
	private String managerIntroduction; // 策展人简介
	private String userId; // 上报人id
	private String exhibitionIntroduction;
	private String remarks;

	public String getExhibitionIntroduction() {
		return exhibitionIntroduction;
	}

	public void setExhibitionIntroduction(String exhibitionIntroduction) {
		this.exhibitionIntroduction = exhibitionIntroduction;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String[] getPicArrayPath() {
		return picArrayPath;
	}

	public void setPicArrayPath(String[] picArrayPath) {
		this.picArrayPath = picArrayPath;
	}

	public String[] getUploadType() {
		return uploadType;
	}

	public void setUploadType(String[] uploadType) {
		this.uploadType = uploadType;
	}

	public String[] getPicName() {
		return picName;
	}

	public void setPicName(String[] picName) {
		this.picName = picName;
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

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getCareLevel() {
		return careLevel;
	}

	public void setCareLevel(String careLevel) {
		this.careLevel = careLevel;
	}

	public String getAuthor() {
		return author;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
