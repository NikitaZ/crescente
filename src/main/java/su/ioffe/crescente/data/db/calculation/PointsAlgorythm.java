package su.ioffe.crescente.data.db.calculation;

import su.ioffe.crescente.data.db.entities.Analysis;

public interface PointsAlgorythm {

    public double calcPoints(Analysis analysis);
    public String getName();
}
