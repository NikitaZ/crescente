package su.ioffe.crescente.data.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private static final TimeZone MOSCOW_TZ = TimeZone.getTimeZone("Europe/Moscow");

    static {
        FORMATTER.setTimeZone(MOSCOW_TZ);
    }

    private DateUtils() {
    }

    public static Date stringToDate(String str) {
        try {
            return FORMATTER.parse(str);
        } catch (ParseException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            return null;
        }
    }

    public static String dateToString(Date date) {
        return FORMATTER.format(date);
    }

    public static Date getToday() {
        return Calendar.getInstance(MOSCOW_TZ).getTime();
    }
}
