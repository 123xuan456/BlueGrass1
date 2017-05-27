package com.reeching.bean;

import java.io.Serializable;
import java.util.List;

public class AlterHualngBean implements Serializable {
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

	public class Infos {
		private String id;

		private boolean isNewRecord;

		private String remarks;

		private String createDate;

		private String updateDate;

		private String name;

		private String linkMan;

		private String mobile;

		private String mapLng;

		private String mapLat;

		private String mapLocation;

		private String address;

		private String userId;

		private String status;

		private String description;

		private String photo;

		private String registAddress;

		private String legalPerson;

		private String enterpriseType;

		private String manageType;

		private String area;

		private String style;

		private String liveTime;

		private String careLevel;

		private String userName;
		private String areaNo;
		public String getAreaNo() {
			return areaNo;
		}

		public void setAreaNo(String areaNo) {
			this.areaNo = areaNo;
		}



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

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public void setLinkMan(String linkMan) {
			this.linkMan = linkMan;
		}

		public String getLinkMan() {
			return this.linkMan;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getMobile() {
			return this.mobile;
		}

		public void setMapLng(String mapLng) {
			this.mapLng = mapLng;
		}

		public String getMapLng() {
			return this.mapLng;
		}

		public void setMapLat(String mapLat) {
			this.mapLat = mapLat;
		}

		public String getMapLat() {
			return this.mapLat;
		}

		public void setMapLocation(String mapLocation) {
			this.mapLocation = mapLocation;
		}

		public String getMapLocation() {
			return this.mapLocation;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getAddress() {
			return this.address;
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

		public void setDescription(String description) {
			this.description = description;
		}

		public String getDescription() {
			return this.description;
		}

		public void setPhoto(String photo) {
			this.photo = photo;
		}

		public String getPhoto() {
			return this.photo;
		}

		public void setRegistAddress(String registAddress) {
			this.registAddress = registAddress;
		}

		public String getRegistAddress() {
			return this.registAddress;
		}

		public void setLegalPerson(String legalPerson) {
			this.legalPerson = legalPerson;
		}

		public String getLegalPerson() {
			return this.legalPerson;
		}

		public void setEnterpriseType(String enterpriseType) {
			this.enterpriseType = enterpriseType;
		}

		public String getEnterpriseType() {
			return this.enterpriseType;
		}

		public void setManageType(String manageType) {
			this.manageType = manageType;
		}

		public String getManageType() {
			return this.manageType;
		}

		public void setArea(String area) {
			this.area = area;
		}

		public String getArea() {
			return this.area;
		}

		public void setStyle(String style) {
			this.style = style;
		}

		public String getStyle() {
			return this.style;
		}

		public void setLiveTime(String liveTime) {
			this.liveTime = liveTime;
		}

		public String getLiveTime() {
			return this.liveTime;
		}

		public void setCareLevel(String careLevel) {
			this.careLevel = careLevel;
		}

		public String getCareLevel() {
			return this.careLevel;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getUserName() {
			return this.userName;
		}

	}
}
