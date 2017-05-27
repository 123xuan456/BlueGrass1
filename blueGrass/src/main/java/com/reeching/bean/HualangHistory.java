package com.reeching.bean;

import java.io.Serializable;
import java.util.List;

public class HualangHistory implements Serializable {
	private String result;

	private String msg;

	private List<Infos> infos;

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

	public void setInfos(List<Infos> infos) {
		this.infos = infos;
	}

	public List<Infos> getInfos() {
		return this.infos;
	}

	public class Infos implements Serializable{
		private String id;

		private boolean isNewRecord;

		private String remarks;

		private String createDate;

		private String updateDate;

		private String galleryId;

		private String theme;

		private String userId;

		private String status;

		private String dateBegin;

		private String dateEnd;

		private String careLevel;

		private String photo;
		private String smallPhoto;

		private String author;

		private String authorIntroduction;

		private String manager;

		private String managerIntroduction;

		private String galleryName;

		private String userName;

		private String checkStatus;

		public void setId(String id) {
			this.id = id;
		}

		public String getId() {
			return this.id;
		}

		public void setIsNewRecord(boolean isNewRecord) {
			this.isNewRecord = isNewRecord;
		}

		public boolean getIsNewRecord() {
			return this.isNewRecord;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		public String getRemarks() {
			return this.remarks;
		}

		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}

		public String getCreateDate() {
			return this.createDate;
		}

		public void setUpdateDate(String updateDate) {
			this.updateDate = updateDate;
		}

		public String getUpdateDate() {
			return this.updateDate;
		}

		public void setGalleryId(String galleryId) {
			this.galleryId = galleryId;
		}

		public String getGalleryId() {
			return this.galleryId;
		}

		public void setTheme(String theme) {
			this.theme = theme;
		}

		public String getSmallPhoto() {
			return smallPhoto;
		}

		public void setSmallPhoto(String smallPhoto) {
			this.smallPhoto = smallPhoto;
		}

		public String getTheme() {
			return this.theme;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getUserId() {
			return this.userId;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getStatus() {
			return this.status;
		}

		public void setDateBegin(String dateBegin) {
			this.dateBegin = dateBegin;
		}

		public String getDateBegin() {
			return this.dateBegin;
		}

		public void setDateEnd(String dateEnd) {
			this.dateEnd = dateEnd;
		}

		public String getDateEnd() {
			return this.dateEnd;
		}

		public void setCareLevel(String careLevel) {
			this.careLevel = careLevel;
		}

		public String getCareLevel() {
			return this.careLevel;
		}

		public void setPhoto(String photo) {
			this.photo = photo;
		}

		public String getPhoto() {
			return this.photo;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public String getAuthor() {
			return this.author;
		}

		public void setAuthorIntroduction(String authorIntroduction) {
			this.authorIntroduction = authorIntroduction;
		}

		public String getAuthorIntroduction() {
			return this.authorIntroduction;
		}

		public void setManager(String manager) {
			this.manager = manager;
		}

		public String getManager() {
			return this.manager;
		}

		public void setManagerIntroduction(String managerIntroduction) {
			this.managerIntroduction = managerIntroduction;
		}

		public String getManagerIntroduction() {
			return this.managerIntroduction;
		}

		public void setGalleryName(String galleryName) {
			this.galleryName = galleryName;
		}

		public String getGalleryName() {
			return this.galleryName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getUserName() {
			return this.userName;
		}

		public void setCheckStatus(String checkStatus) {
			this.checkStatus = checkStatus;
		}

		public String getCheckStatus() {
			return this.checkStatus;
		}

	}
}
