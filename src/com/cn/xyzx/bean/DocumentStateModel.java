package com.cn.xyzx.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.qianjiang.framework.orm.BaseModel;

public class DocumentStateModel extends BaseModel implements Parcelable {
	private static final long serialVersionUID = 3710620581696203798L;
	private String fileName; // 歌曲名字
	private String url; // 下载地址
	private int state; // 文件下载当前状态 1为正在下载， 0为暂停， 2为已经完成
	private int completeSize; // 已下载的长度
	private int fileSize; // 文件长度
	private String title; // 用来显示的文件名字
	private String picture; // 视频图片路径

	public DocumentStateModel() {
	}

	public DocumentStateModel(String fileName, String url, int completeSize, int fileSize, int state) {
		super();
		this.fileName = fileName;
		this.url = url;
		this.state = state;
		this.completeSize = completeSize;
		this.fileSize = fileSize;
	}

	public String getFileName() {
		return null == fileName ? "" : fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getCompleteSize() {
		return completeSize;
	}

	public void setCompleteSize(int completeSize) {
		this.completeSize = completeSize;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getTitle() {
		return null == title ? "" : title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public boolean isComplete() {
		return 0 != fileSize && fileSize == completeSize;
	}

	public static final Parcelable.Creator<DocumentStateModel> CREATOR = new Parcelable.Creator<DocumentStateModel>() {

		@Override
		public DocumentStateModel createFromParcel(Parcel in) {
			DocumentStateModel fileState = new DocumentStateModel();
			fileState.fileName = in.readString();
			fileState.url = in.readString();
			fileState.completeSize = in.readInt();
			fileState.fileSize = in.readInt();
			fileState.state = in.readInt();
			return fileState;
		}

		@Override
		public DocumentStateModel[] newArray(int size) {
			return new DocumentStateModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(fileName);
		parcel.writeString(url);
		parcel.writeInt(completeSize);
		parcel.writeInt(fileSize);
		parcel.writeInt(state);
	}

}
