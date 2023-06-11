package su.ioffe.crescente.data.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StringUtils
{
    private StringUtils() {
    }

    public static String traceToString(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }

}