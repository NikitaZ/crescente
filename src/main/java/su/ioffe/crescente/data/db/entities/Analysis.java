package su.ioffe.crescente.data.entities;

import su.ioffe.medstat.ira.attempt2.calculation.AnalysisData;
import su.ioffe.medstat.ira.attempt2.utils.DateUtils;

import java.util.Date;

public class Analysis {

    private Date analysisDate;
    private long childID;
    private AnalysisData analysisData;


    public static Analysis parseCSV(String line) {
        Analysis analysis = new Analysis();
        String[] fields = line.split(",");

        analysis.childID = Long.parseLong(fields[0].trim());
        analysis.analysisDate = DateUtils.stringToDate(fields[1].trim());

        Double data1 = null;
        fields[2] = fields[2].trim();
        if (!"".equals(fields[2])) {
            data1 = Double.parseDouble(fields[2]);
        }

        Double data2 = null;
        fields[3] = fields[3].trim();
        if (!"".equals(fields[3])) {
            data2 = Double.parseDouble(fields[3]);
        }

        Double data3 = null;
        fields[4] = fields[4].trim();
        if (!"".equals(fields[4])) {
            data3 = Double.parseDouble(fields[4]);
        }

        Double data4 = null;
        if (fields.length >5) {
            fields[5] = fields[5].trim();
            if (!"".equals(fields[5])) {
                data4 = Double.parseDouble(fields[5]);
            }
        }

        AnalysisData analysisData = new AnalysisData(data1, data2, data3, data4);
        analysis.analysisData = analysisData;

        return analysis;
    }

    public Date getAnalysisDate() {
        return analysisDate;
    }

    public void setAnalysisDate(Date analysisDate) {
        this.analysisDate = analysisDate;
    }

    public long getChildID() {
        return childID;
    }

    public void setChildID(long childID) {
        this.childID = childID;
    }

    public AnalysisData getAnalysisData() {
        return analysisData;
    }

    public void setAnalysisData(AnalysisData analysisData) {
        this.analysisData = analysisData;
    }
}
