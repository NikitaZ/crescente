package su.ioffe.crescente.data.db.example;

//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartUtils;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.plot.XYPlot;
//import org.jfree.data.xy.XYDataset;
//import org.jfree.data.xy.XYSeries;
//import org.jfree.data.xy.XYSeriesCollection;

import su.ioffe.crescente.data.db.calculation.AnalysisData;
import su.ioffe.crescente.data.db.calculation.PointsAlgorythm;
import su.ioffe.crescente.data.db.calculation.PointsAlgorythmImpl2;
import su.ioffe.crescente.data.db.calculation.PointsAlgorythmImplLInearWithAge;
import su.ioffe.crescente.data.db.entities.Analysis;
import su.ioffe.crescente.data.db.entities.Child;
import su.ioffe.crescente.data.db.entities.ChildResult;
import su.ioffe.crescente.data.utils.DateUtils;
import su.ioffe.crescente.data.utils.FileUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalcChildren {

    public static Map<Long, Child> childrenMap = new HashMap<>();

    public static void main(String[] args) {

        String childrenFileName = args[0];
        List<Child> children = null;
        Map<Long, Child> childrenMap = null;
        try {
            children = loadChildren(childrenFileName);
            childrenMap = new HashMap<>();
            for (Child child : children) {
                childrenMap.put(child.getId(), child);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        for (Child child: children) {
            childrenMap.put(child.getId(), child);
        }

        String analysisFileName = args[1];
        List<Analysis> analysisList = null;
        try {
            analysisList = loadAnalysis(analysisFileName, childrenMap);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

//        PointsAlgorythm algo = new PointsAlgorythmImpl2();
        PointsAlgorythm algo = new PointsAlgorythmImplLInearWithAge();
        List<ChildResult> results = new ArrayList<>();
        for (Analysis analysis : analysisList) {
            ChildResult result = new ChildResult();
            AnalysisData data = analysis.getAnalysisData();
            result.setChildID(analysis.getChildID());
            result.setAlgUsed(algo.getName());
            result.setCalcDate(DateUtils.getToday());
            result.setResult(algo.calcPoints(analysis));
            results.add(result);
        }

        String savePath = args[2];
        saveResults(savePath, results);

//
//        XYDataset dataset = createDataset(results, childrenMap);
//        JFreeChart chart = ChartFactory.createScatterPlot(
//                "Sick vs ZPR with age WithFake27and54Ing",
//                "X-Axis", "Y-Axis", dataset);
//
//
//        String graphFile = args[3];
//        //Changes background color
//        XYPlot plot = (XYPlot) chart.getPlot();
//        plot.setBackgroundPaint(new Color(255, 228, 196));
//
//        try {
//            ChartUtils.saveChartAsPNG(new File(graphFile), chart, 450, 400);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }

    private static List<Child> loadChildren(String filePath) throws IOException {

        List<Child> children = new ArrayList<Child>();

        List<String> lines = Files.readAllLines(Paths.get(filePath), Charset.forName("windows-1251"));
        if (lines == null || lines.isEmpty()) {
            return children;
        }
        for (String line : lines) {
            line = line.trim();
            if (!"".equals(line) && !line.startsWith("#")) {
                Child child = Child.parseCSV(line);
                children.add(child);
            }
        }

        return children;
    }

    private static List<Analysis> loadAnalysis(String filePath, Map<Long, Child> childrenMap) throws IOException {

        List<Analysis> analysisList = new ArrayList<Analysis>();

        List<String> lines = Files.readAllLines(Paths.get(filePath));
        if (lines == null || lines.isEmpty()) {
            return analysisList;
        }
        for (String line : lines) {
            line = line.trim();
            if (!"".equals(line) && !line.startsWith("#")) {
                Analysis analysis = Analysis.parseCSV(line);
                analysis.setChild(childrenMap.get(analysis.getChildID()));
                analysisList.add(analysis);
            }
        }

        return analysisList;
    }

    private static void saveResults(String path, List<ChildResult> results) {

        StringBuffer stringBuffer = new StringBuffer();
        for (ChildResult result : results) {
            stringBuffer.append(result.toCSV());
            stringBuffer.append("\r\n");
        }

        FileUtils.saveToFile(path, stringBuffer.toString());
    }

//    private static XYDataset createDataset(List<ChildResult> results, Map<Long, Child> children) {
//        XYSeriesCollection dataset = new XYSeriesCollection();
//        XYSeries seriesSick = new XYSeries("Sick");
//        XYSeries seriesCDP = new XYSeries("CDP");
//
//        for (ChildResult result : results) {
//            long childId = result.getChildID();
//            Boolean diagnos = children.get(childId).isDiagnoz();
//            if (diagnos == null) {
//                continue;
//            }
//            if (diagnos) {
//                seriesSick.add(childId, result.getResult());
//            } else {
//                seriesCDP.add(childId, result.getResult());
//            }
//        }
//        dataset.addSeries(seriesSick);
//        dataset.addSeries(seriesCDP);
//
//        return dataset;
//    }
}
