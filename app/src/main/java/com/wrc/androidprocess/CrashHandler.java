package com.wrc.androidprocess;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.wrc.androidprocess.utils.ByteUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 应用异常崩溃处理器
 * 1)重置应用异常处理器;
 * 2)捕获应用异常堆栈信息并存储;
 * 3)重启应用
 *
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
	public static final String CRASH_DIR = "/AndroidProcess";
	
	private static CrashHandler mInstance = new CrashHandler();
	
	private Context mContext;
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	
	
	public static CrashHandler getInstance() {
		return mInstance;
	}
	
	public void init(Context context) {
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}
	
	private CrashHandler() {}
	
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		boolean handle = handleException(thread, ex);
		if (!handle && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
			return;
		}
	}
	/**
	 * 异常处理，自定义异常处理实现。
	 * @param thread 抛出异常的线程
	 * @param ex 异常栈
	 * @return 是否处理
	 */
	private boolean handleException(Thread thread, Throwable ex) {
		if (ex == null) {
			return false;
		}
		
		Map<String,String> pkgInfo = collectApplicationPacketInfo(mContext, thread);
		String crashInfo = makeCrashInfo(pkgInfo, ex);
		String fileName = makeFileName();
		saveCrashInfo(fileName, crashInfo);
		refreshCrashDir(fileName);
		
		return true;
	}
	
	/**
	 * 刷新外部存储目录
	 * @param fileName 待刷新的文件名
	 */
	private void refreshCrashDir(String fileName) {
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File file = new File(getCrashDir() + File.separator + fileName);
		intent.setData(Uri.fromFile(file));
		mContext.sendBroadcast(intent);
	}
	
	/**
	 * 存放异常日志的目录
	 * @return
	 */
	private static String getCrashDir() {
		File extDir = Environment.getExternalStorageDirectory();
		return extDir.getAbsolutePath() + CRASH_DIR;
	}
	
	/**
	 * 生成异常日志文件名</br>
	 * 生成规则：crash-yyyyMMddHHmmss.log
	 * @return 异常日志文件名
	 */
	private static String makeFileName() {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HHmmss-");
		String time = formatter.format(new Date());
		return  time +"crash" + ".log";
	}
	
	/**
	 * 存储异常日志数据到外部存储的文件中
	 * @param fileName 异常日志文件名
	 * @param crashInfo 异常日志数据
	 * @return
	 */
	private boolean saveCrashInfo(String fileName, String crashInfo) {
		String state = Environment.getExternalStorageState();
		if (!state.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}
		
		String dirName = getCrashDir();
		File crashDir = new File(dirName);
		if (!crashDir.exists()) {
			crashDir.mkdirs();
		}
		
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			fos = new FileOutputStream(dirName + File.separator + fileName);
			bos = new BufferedOutputStream(fos);
			bos.write(ByteUtil.toGBK(crashInfo));
			bos.flush();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeQuietly(bos);
			closeQuietly(fos);
		}
		return false;
	}
	
	/**
	 * 收集应用程序包和平台相关的信息
	 * @param context 应用上下文对象
	 * @param thread 抛出异常的线程
	 * @return 包和平台相关信息
	 */
	private Map<String,String> collectApplicationPacketInfo(Context context, Thread thread) {
		Map<String,String> pkgInfo = new HashMap<String,String>();
		pkgInfo.put("Thread-ID", Long.toString(thread.getId()));
		pkgInfo.put("Thread-Name", thread.getName());
		pkgInfo.put("Thread-Priority", Integer.toString(thread.getPriority()));
		
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				pkgInfo.put("versionName", versionName);
				pkgInfo.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				pkgInfo.put(field.getName(), field.get(null).toString());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return pkgInfo;
	}
	
	/**
	 * 构造异常日志信息
	 * @param pkgInfo 应用包及平台相关信息
	 * @param ex 异常栈
	 * @return 异常日志信息
	 */
	private String makeCrashInfo(Map<String,String> pkgInfo, Throwable ex) {
		StringBuffer info = new StringBuffer();
		for (Map.Entry<String, String> entry : pkgInfo.entrySet()) {
			
			if (entry.getKey().equals("versionName") ||entry.getKey().equals("DISPLAY") ||entry.getKey().equals("MODEL")  )
			{info.append(entry.getKey())
					.append(" = ")
					.append(entry.getValue())
					.append("\n");}
		}
		info.append("--------------------------------------------------------------------------------\n");
		Writer stack = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stack);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		info.append(stack.toString());
		
		return info.toString();
	}
	
	/**
	 * 关闭输出流对象
	 * @param os 输出流对象
	 */
	private static void closeQuietly(OutputStream os) {
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}