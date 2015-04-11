package cn.edu.sjzc.fanyafeng.util;


public class FileManagerUtil {

	public static String getSaveFilePath() {
		if (CommonUtil.hasSDCard()) {
			return CommonUtil.getRootFilePath() + "cn.edu.sjzc.fanyafeng/files/";
		} else {
			return CommonUtil.getRootFilePath() + "cn.edu.sjzc.fanyafeng/files";
		}
	}
}
