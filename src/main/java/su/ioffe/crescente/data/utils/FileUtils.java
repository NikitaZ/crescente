package su.ioffe.crescente.data.utils;

//import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileUtils {

    public static List<String> readIDsFromFile(String path) {
        try (InputStream in = FileUtils.class.getResourceAsStream(path)) {
            if (in != null) {
                List<String> ids = new ArrayList<>();
                BufferedReader bufReader = new BufferedReader(new InputStreamReader(in));
                String line = bufReader.readLine();
                while (line != null) {
                    ids.add(line.trim());
                    line = bufReader.readLine();
                }
                return ids;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

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
            ex.printStackTrace();
//            LogManager.getRootLogger().error(StringUtils.traceToString(ex));
        }
        return stringBuilder.toString();
    }

    public static String readFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
//            LogManager.getRootLogger().error(StringUtils.traceToString(e));
        }
        return contentBuilder.toString();
    }

    public static void saveToFile(String filePath, String str) {

        try (BufferedWriter bufWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filePath))))) {
            bufWriter.write(str);
        } catch (IOException e) {
            e.printStackTrace();
//            LogManager.getRootLogger().error(StringUtils.traceToString(e));
        }
    }
}
