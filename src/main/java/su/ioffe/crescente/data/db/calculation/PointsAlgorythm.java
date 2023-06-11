package su.ioffe.crescente.data.db.calculation;

import su.ioffe.medstat.ira.attempt2.data.Analysis;

public interface PointsAlgorythm {

    public double calcPoints(Analysis analysis);
    public String getName();
}
