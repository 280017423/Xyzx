package com.cn.xyzx.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.qianjiang.framework.util.AppDownloader.DownloadProgressListener;

public class HttpDownloader {

	private URL url = null;

	/**
	 * 
	 * @param urlStr
	 * @param path
	 * @param fileName
	 * @return -1:文件下载出错 0:文件下载成功 1:文件已经存在
	 */
	public void downFile(String urlStr, String path, String fileName, DownloadProgressListener listener) {
		InputStream inputStream = null;
		int downloadLength = 0;
		int max = 0;
		try {
			url = new URL(urlStr);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			max = urlConn.getContentLength();
			inputStream = urlConn.getInputStream();
			File resultFile = null;
			OutputStream output = null;
			try {
				resultFile = createSDFile(path + fileName);
				output = new FileOutputStream(resultFile);
				byte[] buffer = new byte[FILESIZE];
				int length;
				while ((length = (inputStream.read(buffer))) > 0) {
					downloadLength += length;
					output.write(buffer, 0, length);
					listener.onDownloading(downloadLength, max);
				}
				output.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (resultFile == null) {
				listener.onError(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			listener.onError(false);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		listener.onDownloadComplete();
	}
	private int FILESIZE = 4 * 1024;

	/**
	 * 在SD卡上创建文件
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public File createSDFile(String fileName) throws IOException {
		File file = new File(fileName);
		file.createNewFile();
		return file;
	}

}
