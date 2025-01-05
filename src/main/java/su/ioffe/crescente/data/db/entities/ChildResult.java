package su.ioffe.crescente.data.db.entities;


import su.ioffe.crescente.data.utils.DateUtils;

import java.util.Date;

public class ChildResult {
    private long childID;
    private Date calcDate;
    private String algUsed;
    private double result;


    public String toCSV() {
        StringBuffer strBuf = new StringBuffer(Long.toString(childID));
        strBuf.append(",");
        strBuf.append(DateUtils.dateToString(calcDate));
        strBuf.append(",");
        strBuf.append(algUsed);
        strBuf.append(",");
        strBuf.append(Double.toString(result));

        return strBuf.toString();
    }

    public long getChildID() {
        return childID;
    }

    public void setChildID(long childID) {
        this.childID = childID;
    }

    public Date getCalcDate() {
        return calcDate;
    }

    public void setCalcDate(Date calcDate) {
        this.calcDate = calcDate;
    }

    public String getAlgUsed() {
        return algUsed;
    }

    public void setAlgUsed(String algUsed) {
        this.algUsed = algUsed;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
