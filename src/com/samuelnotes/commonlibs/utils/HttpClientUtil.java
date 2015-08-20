package com.samuelnotes.commonlibs.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpClientUtil {

	public HttpClient httpClientUtil = getHttpClient();

	public static Executor executor = MThreadFactory.getExecutorService();

	/**
	 * 服务器默认响应时间 3秒 如果服务器太烂可以适当添加
	 */
	private static final int DEFAULT_READTIMEOUT_TIME = 10 * 1000;

	public HttpClient getHttpClient() {

		HttpParams params = new BasicHttpParams();
		// 版本
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		// 编码
		HttpProtocolParams.setContentCharset(params,
				HTTP.DEFAULT_CONTENT_CHARSET);
		// Activates 'Expect: 100-continue' handshake for the entity enclosing
		// methods.
		HttpProtocolParams.setUseExpectContinue(params, true);
		// 最大连接数
		ConnManagerParams.setMaxTotalConnections(params, 100);
		// 超时 目前服务器
		HttpConnectionParams.setConnectionTimeout(params,
				DEFAULT_READTIMEOUT_TIME);
		HttpConnectionParams.setSoTimeout(params, DEFAULT_READTIMEOUT_TIME);
		// 计划注册,可以注册多个计划
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
				params, schReg);
		return new DefaultHttpClient(conMgr, params);
	}

	public String getRequest(final String url) {
		try {
			FutureTask<String> task = new FutureTask<String>(
					new Callable<String>() {
						@Override
						public String call() throws Exception {
							HttpGet get = new HttpGet(url);
							HttpResponse httpResponse = httpClientUtil
									.execute(get);
							if (httpResponse.getStatusLine().getStatusCode() == 200) {
								String result = EntityUtils
										.toString(httpResponse.getEntity());
								return result;
							}
							return null;
						}
					});

			executor.execute(task);
			return task.get();

		} catch (Exception e) {
			if (e.getCause() instanceof ExecutionException) {
				e.printStackTrace();
				return null;
			}
			e.printStackTrace();
			return null;
		}
	}

	public static String getRequest2(final String BASE_URL)
			throws InterruptedException, ExecutionException {
		String result = null;
		try {
			HttpGet get = new HttpGet(BASE_URL);
			HttpClient httpClientUtil = new DefaultHttpClient();
			HttpResponse httpResponse = httpClientUtil.execute(get);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResponse.getEntity());
				if (result != null && result.length() > 0) {
					return result;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Bitmap show(final String path) throws InterruptedException,
			ExecutionException {
		FutureTask<Bitmap> task = new FutureTask<Bitmap>(
				new Callable<Bitmap>() {
					@Override
					public Bitmap call() throws Exception {
						return getBitmap(path);
					}

					public Bitmap getBitmap(String path) throws Exception {
						Bitmap bimg = null;
						URL url = new URL(path);
						HttpURLConnection conn = (HttpURLConnection) url
								.openConnection();
						conn.setRequestMethod("GET");
						conn.setConnectTimeout(DEFAULT_READTIMEOUT_TIME);
						conn.setRequestProperty(
								"Accept",
								"image/gif, image/jpeg, image/pjpeg, image/pjpeg,application/x-shockwave-flash, application/xaml+xml,application/vnd.ms-xpsdocument, application/x-ms-xbap,application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword");
						conn.setRequestProperty("Charset", "UTF-8");
						int code = conn.getResponseCode();
						if (code == 200) {
							InputStream is = conn.getInputStream();
							bimg = BitmapFactory.decodeStream(is);
							is.close();
						}
						return bimg;
					}
				});
		executor.execute(task);
		return task.get();
	}

	public String postRequest(final String url,
			final Map<String, String> rawParams) throws InterruptedException,
			ExecutionException {
		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					public String call() throws Exception {
						HttpPost post = new HttpPost(url);
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						for (String key : rawParams.keySet()) {
							params.add(new BasicNameValuePair(key, rawParams
									.get(key)));
						}
						post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
						HttpResponse httpResponse = httpClientUtil
								.execute(post);
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							String resut = EntityUtils.toString(httpResponse
									.getEntity());
							return resut;
						}
						return null;
					}
				});
		executor.execute(task);
		return task.get();

	}

	public void getBookCache(String path) throws IOException {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setReadTimeout(5000);
		int code = conn.getResponseCode();
		if (code == 200) {
			int length = conn.getContentLength();
			System.out.println("文件总长度：" + length);
			RandomAccessFile raf = new RandomAccessFile("/sdcard/" + "图书.txt",
					"rwd");
			raf.setLength(conn.getContentLength());
			raf.close();
			int blockSize = length / 10;
			int startPosition = 0;
			int endPosition = blockSize;
			// System.out.println("线程-->" + i + "开始位置---->" + startPosition
			// + "结束位置---->" + endPosition);
			new DownloadThread(path, startPosition, endPosition).start();
		}
	}

	class DownloadThread extends Thread {
		private String path;
		private int startPosition;
		private int endPosition;
		private int threadId;

		public DownloadThread(String path, int startPosition, int endPosition) {
			super();
			this.path = path;
			this.startPosition = startPosition;
			this.endPosition = endPosition;
			this.threadId = threadId;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				File themfile = new File("/sdcard/" + "1" + ".txt");
				if (themfile.exists() && themfile.length() > 0) {
					FileInputStream fis = new FileInputStream(themfile);
					byte[] temp = new byte[1024];
					int leng = fis.read(temp);
					String download = new String(temp, 0, leng);
					int downloadInt = Integer.parseInt(download);
					int alreadyDownload = downloadInt - startPosition;
					startPosition = downloadInt;
					fis.close();
				}
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestMethod("GET");
				conn.setReadTimeout(5000);
				conn.setRequestProperty("Range", "bytes=" + startPosition + "-"
						+ endPosition);
				int code = conn.getResponseCode();
				System.out.println("code===" + code);
				if (code == 206) {
					InputStream is = conn.getInputStream();
					RandomAccessFile raf = new RandomAccessFile("/sdcard/"
							+ "tushu.txt", "rwd");
					raf.seek(startPosition);
					int len = 0;
					byte[] buffer = new byte[1024];
					int total = 0;
					while ((len = is.read(buffer)) != -1) {
						RandomAccessFile file = new RandomAccessFile("/sdcard/"
								+ "1" + ".txt", "rwd");
						raf.write(buffer, 0, len);
						total += len;
						file.write((total + startPosition + "").getBytes());
						file.close();
						// synchronized (.this) {
						// currentProcess += len;
						// pb.setProgress(currentProcess);
						// Message msg = Message.obtain();
						// msg.what = DOWNLOAD_PROCESS;
						// handler.sendMessage(msg);
						// }
					}
					is.close();
					raf.close();
					is.close();
					raf.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// Message msg = Message.obtain();
				// msg.what = DOWNLOAD_ERROR;
				// handler.sendMessage(msg);
			} finally {
				// runningDelete();
			}

		}
	}
}
