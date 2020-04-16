package com.yupont.engine.log;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日志文件打印工具
 * @author fenghuaigang
 * @date 2020/2/14
 */
@Slf4j
public class FileAppenderUtil {
	/**
	 *	正则匹配<START></START> ,<END></END> 日志分段内容
	 */
	public static final String START_S_S_END = "<START:(.*?)>([\\s\\S]*?)<END:(.*?)>";

	/** 
	 *  初始化日志文件
	 * @methodName initLogPath  文件路径
	 * @param logPath 日志路径
	 * @return
	 * @author fenghuaigang
	 * @date 2020/3/4
	 */
	public static void initLogPath(String logPath) {

		File logPathDir = new File(logPath);
		if (!logPathDir.exists()) {
			boolean mkdirs = logPathDir.mkdirs();
			if(!mkdirs){
				log.info("日志文件夹创建失败！");
			}
		}
		File glueBaseDir = new File(logPathDir, "gluesource");
		if (!glueBaseDir.exists()) {
			boolean mkdirs = glueBaseDir.mkdirs();
			if(!mkdirs){
				log.info("gluesource文件夹创建失败！");
			}
		}
	}

	/**
	 * 创建文件
	 * @methodName makeLogFileName
	 * @param logPath 路径
	 * @param triggerDate 日期
	 * @param logId 系统日志ID
	 * @param fileName 文件名
	 * @return 文件存放路径全名
	 * @author fenghuaigang
	 * @date 2020/3/6
	 */
	public static String makeLogFileName(String logPath,String triggerDate, String logId,String fileName) {
		File logFilePath = new File(logPath, triggerDate.concat(File.separator).concat(logId));
		if (!logFilePath.exists()) {
			boolean mkdir = logFilePath.mkdir();
			if(!mkdir){
				log.info("日志文件夹创建失败！");
			}
		}
		return logFilePath.getPath().concat(File.separator).concat(fileName).concat(".log");
	}

	/**
	 *  写日志
	 *  1、判断是否存在日志文件，如不存在则创建
	 *  2、开启流写入日志
	 *  3、关闭资源
	 * @methodName appendLog
	 * @param logFileName 文件名
     * @param appendLogs 日志
	 * @return
	 * @author fenghuaigang
	 * @date 2020/3/4
	 */
	public static void appendLog(String logFileName, String... appendLogs) {
		//1、判断是否存在日志文件，如不存在则创建
		if (logFileName != null && logFileName.trim().length() != 0) {
			File logFile = new File(logFileName);
			if (!logFile.exists()) {
				try {
					boolean createNewFile = logFile.createNewFile();
					if(!createNewFile){
						log.info("日志文件创建失败！");
					}
				} catch (IOException arg14) {
					log.error(arg14.getMessage(), arg14);
					return;
				}
			}

			if (appendLogs == null) {
				appendLogs = new String[]{""};
			}
			//2、开启流写入日志
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(logFile, true);
				for (int i = 0; i < appendLogs.length; i++) {
					String appendLog = appendLogs[i] + "\r\n";
					fos.write(appendLog.getBytes(StandardCharsets.UTF_8));
				}
				fos.flush();
			} catch (Exception arg13) {
				log.error(arg13.getMessage(), arg13);
			} finally {
				// 3、关闭资源
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException arg12) {
						log.error(arg12.getMessage(), arg12);
					}
				}
			}
		}
	}
	
	/** 
	 *  读取日志文件
	 * @methodName readLines
	 * @param logFile 日志文件对象
	 * @return 日志内容
	 * @author fenghuaigang
	 * @date 2020/3/4
	 */
	public static String readLines(File logFile) {
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile), StandardCharsets.UTF_8));
			if (reader != null) {
				StringBuilder e = new StringBuilder(2048);
				String line = null;

				while ((line = reader.readLine()) != null) {
					e.append(line).append("\n");
				}

				String arg3 = e.toString();
				return arg3;
			}
		} catch (IOException arg14) {
			log.error(arg14.getMessage(), arg14);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException arg13) {
					log.error(arg13.getMessage(), arg13);
				}
			}

		}

		return null;
	}

	/**
	 *  读取日志文件，返回Map对象
	 * @methodName readLogMapByLogFileName
	 * @param logFileName 文件名
	 * @return 以STRAT\END标识位为键的日志内容Map集合
	 * @author fenghuaigang
	 * @date 2020/3/4
	 */
	public static Map<String,String> readLogMapByLogFileName(String logFileName) {
		Map<String,String> logMap=new HashMap<String,String>();
		if (logFileName != null && logFileName.trim().length() != 0) {
			File logFile = new File(logFileName);
			if (!logFile.exists()) {
				logMap.put("err", "readLog fail, logFile not exists");
				return logMap;
			} else {
					String wholeLog=FileAppenderUtil.readLines(logFile);
					Pattern p = Pattern.compile(START_S_S_END);
					Matcher m = p.matcher(wholeLog);
					while (m.find()) {
						if(logMap.containsKey(m.group(1))){
							String content = logMap.get(m.group(1))+m.group(2);
							logMap.put(m.group(1),content);
						}else{
							String content = m.group(2);
							logMap.put(m.group(1), content);
						}
					}
				return logMap;

			}
		} else {
			logMap.put("err", "readLog fail, logFile not found");
			return logMap;
		}
	}
}