package com.shpoints.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/**
	 * 获取指定日期格式yyyyMMDDhhmmss
	 * 
	 * @return
	 */
	public static String getDateyyyyMMddHHmmss() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}

	/**
	 * 获取指定日期格式yyyyMMddHH
	 * 
	 * @return
	 */
	public static String getDateyyyyMMddHH() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
		return sdf.format(new Date());
	}

	/**
	 * 获取指定日期格式MddHHmmssSS
	 * 
	 * @return
	 */
	public static String getDateMddHHmmssSS() {
		SimpleDateFormat sdf = new SimpleDateFormat("MddHHmmssSS");
		return sdf.format(new Date());
	}

	/**
	 * 获取指定日期格式HHmmss
	 * 
	 * @return
	 */
	public static String getDateHHmmss() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		return sdf.format(new Date());
	}

	/**
	 * 获取指定日期格式MMDD
	 * 
	 * @return
	 */
	public static String getDateMMDD() {
		SimpleDateFormat sdf = new SimpleDateFormat("MMDD");
		return sdf.format(new Date());
	}

	/**
	 * 获取指定日期格式yyyyMMdd
	 * 
	 * @return
	 */
	public static String getyyyyMMdd() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date());
	}

	/** 判断日期是否为今天 */
	public static boolean isThisDay(String day) {
		String thisDay = getDateyyyyMMddHH();
		return thisDay.equals(day);
	}

	/**
	 * 获取今天周几
	 * 
	 * @return
	 */
	public static int getThisDay() {
		final int dayNames[] = { 7, 1, 2, 3, 4, 5, 6 };
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek < 0)
			dayOfWeek = 0;
		return dayNames[dayOfWeek];
	}

	/**
	 * 获取两日期之间有几个周
	 * 周一为周的第一天
	 * @param startdate
	 * @param enddate
	 * @return
	 * @throws ParseException
	 */
	public static int getWeekBetweenDate(String startdate,String enddate) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		Date startDate = format.parse(startdate);
		Date endDate = format.parse(enddate);
		start.setTime(startDate);
		end.setTime(endDate);
		int sumSunday = 0;
		while (start.compareTo(end) <= 0) {
			int w = start.get(Calendar.DAY_OF_WEEK);
			if (w == Calendar.MONDAY)
				sumSunday++;
			start.set(Calendar.DATE, start.get(Calendar.DATE) + 1);
		}
		return sumSunday;
	}

	 
	@SuppressWarnings("unused")
	public static void main(String[] args) throws ParseException {

		/** 单活动日名额 */
		String daypointsnum = "1000";
		String activitday = "7#6";
		String startday = "20160101";
		String endday = "20170101";
		System.out.println("thisDayNum:"+getTotalPointsNum(daypointsnum, activitday, startday, endday));
		System.out.println("12#3".contains("1213"));
	}

	/**
	 * 通过传入参数获取今天可以消费多少名额
	 * @param daypointsnum
	 * 					单个活动日名额100
	 * @param activitday
	 * 					活动日多个日期用#分割1#2#3
	 * @param startday
	 * 					活动开始日期20160101
	 * @param endday
	 * 					活动截止日期20160202
	 * @return
	 * @throws ParseException
	 */
	public static int getTotalPointsNum(String daypointsnum,String activitday ,String startday,String endday) throws ParseException{
		/** 单活动日名额 */
		int weekofdate = getWeekBetweenDate(startday,endday);
		int thisDay = getThisDay();
		String[] activityday = activitday.split("#");

		int dayofweek = 0;
		for (String s : activityday) {
			if (Integer.parseInt(s) <= thisDay) {
				++dayofweek;
			}
		}
		// ((本周-1)*周活动天数+今天在本周活动天数)*daypoointsnum=今天可以消费名额
		int thisDayNum = ((weekofdate - 1<0?0:weekofdate) * activityday.length + dayofweek)
				* Integer.parseInt(daypointsnum);
		return thisDayNum;
	}
	
}
