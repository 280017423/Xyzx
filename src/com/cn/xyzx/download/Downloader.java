package com.cn.xyzx.download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.cn.xyzx.bean.DownloadInfoModel;
import com.cn.xyzx.bean.LoadInfoModel;
import com.cn.xyzx.db.DownLoadDao;

public class Downloader {
	private String downPath;// 下载路径
	private String savePath;// 保存路径
	private String fileName;// 歌曲名字
	private int threadCount;// 线程数
	private Handler mHandler;
	private Context context;
	private int fileSize;// 文件大小
	private int range;
	private List<DownloadInfoModel> infos;// 存放下载信息类的集合
	private int state = INIT;
	private static final int INIT = 1;// 定义三种下载的状态：初始化状态，正在下载状态，暂停状态
	private static final int DOWNLOADING = 2;
	private static final int PAUSE = 3;

	/**
	 * @param downPath
	 *            下载地址
	 * @param savePath
	 *            保存地址
	 * @param fileName
	 *            文件名称
	 * @param threadcount
	 *            使用的线程数
	 * @param context
	 *            用来创建一个DAO对象
	 * **/
	public Downloader(String downPath, String savePath, String fileName, int threadCount, Context context,
			Handler mHandler) {
		this.downPath = downPath;
		this.savePath = savePath;
		this.fileName = fileName;
		this.threadCount = threadCount;
		this.context = context;
		this.mHandler = mHandler;
	}

	/**
	 * 判断是否正在下载
	 */
	public boolean isdownloading() {
		return state == DOWNLOADING;
	}

	/**
	 * 判断是否暂停
	 * **/
	public boolean isPause() {
		return state == PAUSE;
	}

	/**
	 * 得到downloader里的信息 首先进行判断是否是第一次下载，如果是第一次就要进行初始化，并将下载器的信息保存到数据库中
	 * 如果不是第一次下载，那就要从数据库中读出之前下载的信息（起始位置，结束为止，文件大小等），并将下载信息返回给下载器
	 */
	public LoadInfoModel getDownloaderInfors() {
		if (isFirst(downPath)) {
			init();// 第1次下载要进行初始化
			if (fileSize <= 0) {
				return null;
			}
			range = this.fileSize / this.threadCount;// 设置每个线程应该下载的长度
			System.out.println("range is:" + range);
			infos = new ArrayList<DownloadInfoModel>();// List<DownloadInfoModel>infos
														// 里装的是每条线程的下载信息
			// 初始化线程信息集合器,初始化每条线程的信息,为每条线程分配开始下载位置，结束位置
			for (int i = 0; i < this.threadCount - 1; i++) {
				// startPos是每条线程数乘以每条线程应该下载的长度,第0条,从0开始
				// endPos要减去1Byte是因为,不减1byte的地方是下一个线程开始的位置
				DownloadInfoModel info = new DownloadInfoModel(i, i * range, (i + 1) * range - 1, 0, downPath);
				infos.add(info);// 把每条线程的信息加入到infos这个线程信息集合器里
			}
			// 这里加入最后1个线程的信息,只所以单独拿出来是因为最后一条线程下载的结束位置应该为fileSize
			DownloadInfoModel info = new DownloadInfoModel(
					this.threadCount - 1, (this.threadCount - 1) * range, this.fileSize, 0, downPath);
			infos.add(info);
			// 将这个infos加入到数据库,表面ListView上的一个item已经初始化，已经不是第1次下载了
			DownLoadDao.saveInfos(infos);
			// 创建一个LoadInfo对象记载下载器的具体信息
			LoadInfoModel loadInfo = new LoadInfoModel(this.fileSize, 0, this.downPath);
			return loadInfo;
		} else {
			// 如果不是第1次下载,得到数据库中已有的urlstr的下载器的具体信息
			infos = DownLoadDao.getInfos(this.downPath);
			int size = 0;
			int completeSize = 0;
			for (DownloadInfoModel info : infos) {
				completeSize += info.getCompeleteSize();// 把每条线程下载的长度累加起来,得到整个文件的下载长度
				size += info.getEndPos() - info.getStartPos() + 1;// 计算出文件的大小,用每条线程的结束位置减去开始下载的位置,等于每条线程要下载的长度，然后累加
			}
			LoadInfoModel loadInfo = new LoadInfoModel(size, completeSize, this.downPath);
			return loadInfo;
		}
	}

	/**
	 * 初始化,要干的事:1.得到下载文件的长度 2.在给定的保存路径创建文件,设置文件的大小
	 */
	private void init() {
		try {
			URL url = new URL(downPath);// 通过给定的下载地址得到一个url
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 得到一个http连接
			conn.setConnectTimeout(5 * 1000);// 设置连接超时为5秒钟
			conn.setRequestMethod("GET");// 设置连接方式为GET
			// 如果http返回的代码是200或者206则为连接成功
			if (conn.getResponseCode() == 200 || conn.getResponseCode() == 206) {
				fileSize = conn.getContentLength();// 得到文件的大小
				if (fileSize <= 0) {
					Toast.makeText(this.context, "网络故障,无法获取文件大小.", Toast.LENGTH_SHORT).show();
				}
				File dir = new File(savePath);
				// 如果文件不存在,则创建一个指定的文件,这里可以扩展一下,如果文件已经存在,弹出对话框提醒用户是否覆盖
				if (!dir.exists()) {
					if (dir.mkdirs()) {
						System.out.println("mkdirs success.");
					}
				}
				File file = new File(this.savePath, this.fileName);
				RandomAccessFile randomFile = new RandomAccessFile(file, "rwd");
				randomFile.setLength(fileSize);// 设置保存文件的大小
				randomFile.close();
				conn.disconnect();
			} else {
				Toast.makeText(this.context, "网络故障,无法获取文件大小.", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 利用线程开始下载数据
	 */
	public void download() {
		if (infos != null) {
			System.out.println("infos is not null");
			if (state == DOWNLOADING) {
				return;
			}
			DownLoadDao.updateFileState(downPath, 1);
			state = DOWNLOADING;// 把状态设置为正在下载
			for (DownloadInfoModel info : infos) {
				new MyThread(
						info.getThreadId(), info.getStartPos(), info.getEndPos(), info.getCompeleteSize(),
						info.getUrl()).start();
			}
		}
	}

	// 删除数据库中urlstr对应的下载器信息
	public void delete(String urlstr) {
		DownLoadDao.delete(urlstr);
	}

	// 设置暂停
	public void pause() {
		DownLoadDao.updateFileState(downPath, 0);
		state = PAUSE;
	}

	// 重置下载状态,将下载状态设置为init初始化状态
	public void reset() {
		state = INIT;
	}

	/**
	 * 判断是否是第一次下载，利用dao查询数据库中是否有下载这个地址的记录
	 * 
	 * @return boolean 有的话返回false,没有返回true
	 */
	private boolean isFirst(String urlstr) {
		return DownLoadDao.isFirstDownLoad(urlstr);
	}

	/**
	 * MyThread是一个内部类，它继承自Thread类
	 * **/
	public class MyThread extends Thread {
		private int threadId;
		private int startPos;
		private int endPos;
		private int compeleteSize;
		private String urlstr;

		/**
		 * @param threadId
		 *            线程号
		 * @param startPos
		 *            线程开始下载的位置
		 * @param endPos
		 *            线程结束下载的位置
		 * @param compeleteSize
		 *            每个线程完成下载的长度
		 * @param urlstr
		 *            下载地址
		 * **/
		public MyThread(int threadId, int startPos, int endPos, int compeleteSize, String urlstr) {
			this.threadId = threadId;
			this.startPos = startPos;
			this.endPos = endPos;
			this.compeleteSize = compeleteSize;
			this.urlstr = urlstr;
		}

		@Override
		public void run() {
			HttpURLConnection conn = null;
			RandomAccessFile randomAccessFile = null;
			InputStream inStream = null;
			File file = new File(savePath, fileName);
			try {
				URL url = new URL(this.urlstr);
				conn = (HttpURLConnection) url.openConnection();
				constructConn(conn);
				System.out.println("responseCode:" + conn.getResponseCode());
				if (conn.getResponseCode() == 200 || conn.getResponseCode() == 206) {
					randomAccessFile = new RandomAccessFile(file, "rwd");
					// 这里的参数只所以要加上compeleteSize完全是为了断点续传考虑,因为很可能不是第1次下载
					randomAccessFile.seek(this.startPos + this.compeleteSize);// 这里设置线程从哪个地方开始写入数据,这里是与网上获取数据是一样的
					inStream = conn.getInputStream();
					byte buffer[] = new byte[4096];
					int length = 0;

					while ((length = inStream.read(buffer, 0, buffer.length)) != -1) {
						randomAccessFile.write(buffer, 0, length);
						compeleteSize += length;// 累加已经下载的长度
						// 更新数据库中的下载信息
						DownLoadDao.updataInfos(threadId, compeleteSize, urlstr);
						// 用消息将下载信息传给进度条，对进度条进行更新
						Message message = mHandler.obtainMessage();
						message.what = 1;
						message.obj = urlstr;
						message.arg1 = length;
						mHandler.sendMessage(message);// 给DownloaderService发送消息
						if (state == PAUSE) {
							System.out.println("-----pause----");
							break;
						}
					}
					System.out.println("threadid:" + this.threadId + "is over");
				} else {
					pause();
					Message message = mHandler.obtainMessage();
					message.what = -1;
					message.obj = urlstr;
					mHandler.sendEmptyMessage(-1);// 给DownloaderService发送消息
				}
			} catch (Exception e) {
				pause();
				Message message = mHandler.obtainMessage();
				message.what = -1;
				message.obj = urlstr;
				mHandler.sendEmptyMessage(-1);// 给DownloaderService发送消息
				e.printStackTrace();
			} finally {
				try {
					if (null != inStream) {
						inStream.close();
					}
					if (null != randomAccessFile) {
						randomAccessFile.close();
					}
					conn.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		/***********************************************************************
		 * 构建请求连接时的参数 返回开始下载的位置
		 * 
		 * @throws IOException
		 **********************************************************************/
		private void constructConn(HttpURLConnection conn) throws IOException {
			conn.setConnectTimeout(5 * 1000);// 一定要设置连接超时噢。这里定为5秒
			conn.setRequestMethod("GET");// 采用GET方式提交
			conn.setRequestProperty(
					"Accept",
					"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			conn.setRequestProperty("Accept-Language", "zh-CN");
			conn.setRequestProperty("Referer", this.urlstr);
			conn.setRequestProperty("Charset", "UTF-8");
			int startPosition = this.startPos + this.compeleteSize;
			// 设置范围，格式为Range：bytes x-y;
			// 这行代码就是实现多线程的关键,Range字段允许用户设置下载的开始地址和结束地址,当然range还有很多其他的用法
			conn.setRequestProperty("Range", "bytes=" + startPosition + "-" + this.endPos);// 设置获取实体数据的范围
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.connect();
		}
	}

}
