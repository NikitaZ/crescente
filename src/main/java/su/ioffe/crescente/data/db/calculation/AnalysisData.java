package su.ioffe.crescente.data.db.calculation;

public class AnalysisData {

    private Double lhValue;
    private Double fshValue;
    private Double tesValue;
    private Double inhValue;

    public AnalysisData() {

    }
    public AnalysisData(Double lhValue, Double fshValue, Double tesValue, Double inhValue) {
        this.lhValue = lhValue;
        this.fshValue = fshValue;
        this.tesValue = tesValue;
        this.inhValue = inhValue;
    }

    public Double getLhValue() {
        return lhValue;
    }

    public void setLhValue(Double lhValue) {
        this.lhValue = lhValue;
    }

    public Double getFshValue() {
        return fshValue;
    }

    public void setFshValue(Double fshValue) {
        this.fshValue = fshValue;
    }

    public Double getTesValue() {
        return tesValue;
    }

    public void setTesValue(Double tesValue) {
        this.tesValue = tesValue;
    }

    public Double getInhValue() {
        return inhValue;
    }

    public void setInhValue(Double inhValue) {
        this.inhValue = inhValue;
    }
}
