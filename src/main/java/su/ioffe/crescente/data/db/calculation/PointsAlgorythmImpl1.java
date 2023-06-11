package su.ioffe.crescente.data.db.calculation;


import su.ioffe.crescente.data.db.entities.Analysis;

public class PointsAlgorythmImpl1 implements PointsAlgorythm {

    private final String name = "Alg1-stairs";

    @Override
    public double calcPoints(Analysis analysis) {

        AnalysisData analysisData = analysis.getAnalysisData();
        double lhValue = analysisData.getLhValue();
        double fshValue = analysisData.getFshValue();
        double tesValue = analysisData.getTesValue();
        double inhValue = analysisData.getInhValue();

        double lhKoef = 8.3;
        double lhPos1 = 0.23;
        double lhPos2 = 0.38;
        double lhPos3 = 1.24;
        double lhMark = 0;

        double fshKoef = 0;
        double fshPos1 = 0.9;
        double fshPos2 = 1.27;
        double fshPos3 = 1.58;
        double fshMark = 0;

        double tesKoef = 7.5;
        double tesPos1 = 0.11;
        double tesPos2 = 0.48;
        double tesPos3 = 1.7;
        double tesMark = 0;

        double inhKoef = 6;
        double inhPos1 = 35;
        double inhPos2 = 65;
        double inhMark = 0;

        double highestMark = 5;
        if (lhValue <= lhPos1) {
            lhMark = highestMark;
        } else if (lhValue <= lhPos2) {
            lhMark = 0.4*highestMark;
        } else if (lhValue < lhPos3) {
            lhMark = 0.2*highestMark;
        } else {
            lhMark = 0;
        }

        if (fshValue <= fshPos1) {
            fshMark = highestMark;
            fshKoef = 10;
        } else if (fshValue <= fshPos2) {
            fshMark = 0.4*highestMark;
            fshKoef = 8.6;
        } else if (fshValue < fshPos3) {
            fshMark = 0.2*highestMark;
            fshKoef = 8.6;
        } else {
            fshMark = -0.2*highestMark;
            fshKoef = 10;
        }

        if (tesValue <= tesPos1) {
            tesMark = highestMark;
        } else if (tesValue <= tesPos2) {
            tesMark = 0.4*highestMark;
        } else if (tesValue < tesPos3) {
            tesMark = 0.2*highestMark;
        } else {
            tesMark = -0.2*highestMark;
        }

        if (inhValue < inhPos1) {
            inhMark = highestMark;
        } else if (inhValue < inhPos2) {
            inhMark = 0.6*highestMark;
        } else {
            inhMark = 0;
        }

        double totalMark = lhKoef*lhMark + fshKoef*fshMark + tesKoef*tesMark + inhKoef*inhMark;


        double totalMaxMark = 0;
        if (lhValue >=0) {
            totalMaxMark +=highestMark*lhKoef;
        }
        if (fshValue >=0) {
            totalMaxMark +=highestMark*fshKoef;
        }
        if (tesValue >=0) {
            totalMaxMark +=highestMark*tesKoef;
        }
        if (inhValue >=0) {
            totalMaxMark +=highestMark*inhKoef;
        }

        if (totalMaxMark == 0) {
            return -1000;
        }


        return 100*totalMark/totalMaxMark;
    }

    @Override
    public String getName() {
        return name;
    }
}
