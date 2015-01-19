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

	private static final int FILESIZE = 4 * 1024;

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
		File resultFile = null;
		URL url = null;
		try {
			url = new URL(urlStr);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setConnectTimeout(10 * 1000);
			urlConn.setReadTimeout(10 * 1000);
			max = urlConn.getContentLength();
			inputStream = urlConn.getInputStream();
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
				if (null != resultFile) {
					resultFile.delete();
				}
				listener.onError(false);
				e.printStackTrace();
			} finally {
				try {
					if (null != output) {
						output.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (resultFile == null) {
				listener.onError(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (null != resultFile) {
				resultFile.delete();
			}
			listener.onError(false);
		} finally {
			try {
				if (null != inputStream) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		listener.onDownloadComplete();
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @param fileName
	 *            指定的文件名字
	 * @return File 生成的文件
	 * @throws IOException
	 */
	public File createSDFile(String fileName) throws IOException {
		File file = new File(fileName);
		file.createNewFile();
		return file;
	}

}
