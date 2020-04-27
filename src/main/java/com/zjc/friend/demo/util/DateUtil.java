package com.zjc.friend.demo.util;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ Author     ：zjc
 * @ Date       ：Created in 16:05 2020/3/3
 * @ Description：
 * @ Modified By：
 * @Version: $
 */

/**
 * 时间的工具类
 */
public class DateUtil {

    /**
     * String转时间戳
     *
     * @param str
     * @throws Exception
     */
    public static Long dateToStamp(String str) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(str);
        long ts = date.getTime();
        System.out.println(ts);
        return ts;
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }
}
