package com.lamps.lamps.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by stew on 16/9/7.
 * mail: stewforani@gmail.com
 */
public class TimeUtils {

    /**
     * 时间戳转为年月日，时分秒
     * @param cc_time
     * @return
     */
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }
}
