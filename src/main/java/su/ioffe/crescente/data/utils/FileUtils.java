package su.ioffe.crescente.data.utils;

//import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileUtils {

    private FileUtils() {
    }

    public static List<String> readIDsFromFile(String path) {
        try (InputStream in = FileUtils.class.getResourceAsStream(path)) {
            if (in != null) {
                final List<String> ids = new ArrayList<>();
                final BufferedReader bufReader = new BufferedReader(new InputStreamReader(in));
                String line = bufReader.readLine();
                while (line != null) {
                    ids.add(line.trim());
                    line = bufReader.readLine();
                }
                return ids;
            }
        } catch (IOException ex) {
            //noinspection CallToPrintStackTrace
            ex.printStackTrace();
        }
        return null;
    }

    // TODO is this the best way? Is it some sort of win->linux line endings converter? like Files.read()...
    public static String readResourceString(String path) {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream in = FileUtils.class.getResourceAsStream(path)) {
            if (in != null) {
                BufferedReader bufReader = new BufferedReader(new InputStreamReader(in, "Cp1251"));
                String line;
                while ((line = bufReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
            }
        } catch (IOException ex) {
            System.out.println(StringUtils.traceToString(ex));
        }
        return stringBuilder.toString();
    }

    public static String readFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        // todo compare to above
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
//            LogManager.getRootLoggergger().error(StringUtils.traceToString(e));
            System.out.println(StringUtils.traceToString(e));
        }
        return contentBuilder.toString();
    }

    public static void saveToFile(String filePath, String str) {
        try (BufferedWriter bufWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filePath))))) {
            bufWriter.write(str);
        } catch (IOException e) {
            System.out.println(StringUtils.traceToString(e));
//            LogManager.getRootLogger().error(StringUtils.traceToString(e));
        }
    }
}
