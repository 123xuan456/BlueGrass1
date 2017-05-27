package com.reeching.bean;

import java.io.Serializable;
import java.util.List;

public class AllZhanlanInfo implements Serializable {
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

	public class Infos {
		private String name;

		private String id;

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getId() {
			return this.id;
		}

	}

}
