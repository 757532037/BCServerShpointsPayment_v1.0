package bc.core.utils;

import com.shpoints.util.DateUtil;

/**
 * 上海银行积分POS
 * @author xf.hu
 *
 */
public class ShpointsFunction {

	/**兑换物品*/
	public static String FASTTRACK_N="N";
	/**兑换金额*/
	public static String FASTTRACK_B="B";
	/**
	 * 左填充
	 * @param length 
	 * 				要填充长度
	 * @param str
	 * 			原字符串
	 * @param type
	 * 				填充字符串
	 * @return
	 */
	public static String formatLeft(int length,String str,String type){
		if(str==null) {
			str="";
		}
		for(int i=str.length();i<length;i++){
			str = type+str;
		}
		return str;
	}
	
	/**
	 * 右填充
	 * @param length 
	 * 				要填充长度
	 * @param str
	 * 			原字符串
	 * @param type
	 * 				填充字符串
	 * @return
	 */
	public static String formatRight(int length,String str,String type){
		if(str==null) {
			str="";
		}
		for(int i=str.length();i<length;i++){
			str = str+type;
		}
		return str;
	}
	
	/**
	 *判断积分余额是否充足 
	 * @param points
	 * 				已有积分
	 * @param tagPoints
	 * 				兑换积分
	 * @param per
	 * 			积分兑换比例
	 * @return
	 */
	public static boolean pointsIsEnough(String points,String tagPoints,int per){
		if(points==null||points.equals("")) points="0";
		if(tagPoints==null||tagPoints.equals("")) tagPoints="0";
		return Integer.parseInt(points.trim())>=(Integer.parseInt(tagPoints.trim())*per);
		
	}
	
	/**判断是否在活动日期*/
	public static boolean isAlive(String startdate,String enddate,String thisdate){
		int start = Integer.parseInt(startdate);
		int end = Integer.parseInt(enddate);
		int thi = Integer.parseInt(thisdate);
		return thi>=start?thi<=end:false;
	}
	
	/**是否为今天日期*/
	public static boolean isToday(String tagdate){

		return DateUtil.getyyyyMMdd().equals(tagdate.substring(0,8));
	}
	
	public static void main(String[] args) {
		System.out.println(isToday("2016041218"));
	}
	
}
