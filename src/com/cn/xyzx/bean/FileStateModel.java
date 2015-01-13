package com.cn.xyzx.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class FileStateModel implements Parcelable {
	private String musicName; // 歌曲名字
	private String url; // 下载地址
	private int state; // 歌曲当前状态 1为正在下载 0为已下载
	private int completeSize; // 已下载的长度
	private int fileSize; // 文件长度

	public FileStateModel() {
	}

	public FileStateModel(String musicName, String url, int completeSize, int fileSize, int state) {
		super();
		this.musicName = musicName;
		this.url = url;
		this.state = state;
		this.completeSize = completeSize;
		this.fileSize = fileSize;
	}

	public String getMusicName() {
		return null == musicName ? "" : musicName;
	}

	public void setMusicName(String musicName) {
		this.musicName = musicName;
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

	public static final Parcelable.Creator<FileStateModel> CREATOR = new Parcelable.Creator<FileStateModel>() {

		@Override
		public FileStateModel createFromParcel(Parcel in) {
			FileStateModel fileState = new FileStateModel();
			fileState.musicName = in.readString();
			fileState.url = in.readString();
			fileState.completeSize = in.readInt();
			fileState.fileSize = in.readInt();
			fileState.state = in.readInt();
			return fileState;
		}

		@Override
		public FileStateModel[] newArray(int size) {
			return new FileStateModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(musicName);
		parcel.writeString(url);
		parcel.writeInt(completeSize);
		parcel.writeInt(fileSize);
		parcel.writeInt(state);
	}

}
