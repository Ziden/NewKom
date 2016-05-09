/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.util;

import java.text.DecimalFormat;

/**
 *
 * @author Feldmann
 */
public class TimeUtils {

    private final static long ONE_SECOND = 1000;
    private final static long SECONDS = 60;

    private final static long ONE_MINUTE = ONE_SECOND * 60;
    private final static long MINUTES = 60;

    private final static long ONE_HOUR = ONE_MINUTE * 60;
    private final static long HOURS = 24;

    private final static long ONE_DAY = ONE_HOUR * 24;

    public static String millisToLongDHMS(long duration) {
        StringBuffer res = new StringBuffer();
        long temp = 0;
        if (duration >= ONE_SECOND) {
            temp = duration / ONE_DAY;
            if (temp > 0) {
                duration -= temp * ONE_DAY;
                res.append(temp).append(" day").append(temp > 1 ? "s" : "")
                        .append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_HOUR;
            if (temp > 0) {
                duration -= temp * ONE_HOUR;
                res.append(temp).append(" hour").append(temp > 1 ? "s" : "")
                        .append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_MINUTE;
            if (temp > 0) {
                duration -= temp * ONE_MINUTE;
                res.append(temp).append(" minute").append(temp > 1 ? "s" : "");
            }

            if (!res.toString().equals("") && duration >= ONE_SECOND) {
                res.append(" and ");
            }

            temp = duration / ONE_SECOND;
            if (temp > 0) {
                res.append(temp).append(" second").append(temp > 1 ? "s" : "");
            }
            return res.toString();
        } else {
            return "0 second";
        }
    }

    public static String millisToLongDHMSResumido(long duration) {
        StringBuffer res = new StringBuffer();
        long temp = 0;
        if (duration >= ONE_SECOND) {
            temp = duration / ONE_DAY;
            if (temp > 0) {
                duration -= temp * ONE_DAY;
                res.append(temp).append("d").append(temp > 1 ? "" : "")
                        .append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_HOUR;
            if (temp > 0) {
                duration -= temp * ONE_HOUR;
                res.append(temp).append("h").append(temp > 1 ? "" : "")
                        .append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_MINUTE;
            if (temp > 0) {
                duration -= temp * ONE_MINUTE;
                res.append(temp).append("m").append(temp > 1 ? "" : "");
            }

            if (!res.toString().equals("") && duration >= ONE_SECOND) {
                res.append(" e ");
            }

            temp = duration / ONE_SECOND;
            if (temp > 0) {
                res.append(temp).append(" s").append(temp > 1 ? "" : "");
            }
            return res.toString();
        } else {
            return "0 second";
        }
    }

    public static double trim(int degree, double d) {
        String format = "0.0";

        for (int i = 1; i < degree; i++) {
            format = format + "0";
        }
        DecimalFormat twoDForm = new DecimalFormat(format);

        return Double.valueOf(twoDForm.format(d).replace(",", "."));
    }

    public static boolean elapsed(long from, long required) {
        return System.currentTimeMillis() - from > required;
    }

    public static String convertString(long time, int trim, TimeUnit type) {
        if (time == -1L) {
            return "Permanente";
        }

        if (type == TimeUnit.FIT) {
            if (time < 60000L) {
                type = TimeUnit.SECONDS;
            } else if (time < 3600000L) {
                type = TimeUnit.MINUTES;
            } else if (time < 86400000L) {
                type = TimeUnit.HOURS;
            } else {
                type = TimeUnit.DAYS;
            }
        }
        if (type == TimeUnit.DAYS) {
            return trim(trim, time / 86400000.0D) + " Dias";
        }
        if (type == TimeUnit.HOURS) {
            return trim(trim, time / 3600000.0D) + " Horas";
        }
        if (type == TimeUnit.MINUTES) {
            return trim(trim, time / 60000.0D) + " Minutos";
        }
        if (type == TimeUnit.SECONDS) {
            return trim(trim, time / 1000.0D) + " Segundos";
        }
        return trim(trim, time) + " Milliseconds";
    }

    public static String getTimeToString(int t) {
        int tem = t;
        int min = (int) tem / 60;
        int sec = tem % 60;

        String mins = (min >= 10 ? "" : "0") + min + "";
        String secs = (sec >= 10 ? "" : "0") + sec + "";

        return mins + ":" + secs;
    }

    public static enum TimeUnit {

        FIT,
        DAYS,
        HOURS,
        MINUTES,
        SECONDS,
        MILLISECONDS;
    }
}
