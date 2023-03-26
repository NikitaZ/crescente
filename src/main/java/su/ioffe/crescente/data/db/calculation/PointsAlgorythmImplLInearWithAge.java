package su.ioffe.crescente.data.db.calculation;

import su.ioffe.medstat.ira.attempt2.CalcChildren;
import su.ioffe.medstat.ira.attempt2.data.Analysis;

import java.util.Date;

public class PointsAlgorythmImplLInearWithAge implements PointsAlgorythm {

    private final String name = "Alg-linear-with-age";
    private final int minMonth = 12*15;
    private final int maxMonth = 12*18;

    private final double minK = 1;
    private final double maxK = 2;


    @Override
    public double calcPoints(Analysis analysis) {

        System.out.println("Starting to calculate points");

        AnalysisData analysisData = analysis.getAnalysisData();
        Double lhValue = analysisData.getLhValue();
        Double fshValue = analysisData.getFshValue();
        Double tesValue = analysisData.getTesValue();
        Double inhValue = analysisData.getInhValue();

        double lhKoef = 8.3;
        double k_lh_1 = -13.04;
        double k_lh_2 = -6.67;
        double k_lh_3 = -1.16;
        double k_lh_4 = 0;
        double b_lh_1 = 5;
        double b_lh_2 = 3.53;
        double b_lh_3 = 1.44;
        double b_lh_4 = 0;
        double lhPos1 = 0.23;
        double lhPos2 = 0.38;
        double lhPos3 = 1.24;
        double lhMark = 0;


        boolean lhFound = false;
        if (lhValue != null) {
            lhFound = true;
        }
        if (!lhFound) {
            lhMark = 0;
        } else if (lhValue <= lhPos1) {
            lhMark = k_lh_1*lhValue + b_lh_1;
        } else if (lhValue <= lhPos2) {
            lhMark = k_lh_2*lhValue + b_lh_2;
        } else if (lhValue < lhPos3) {
            lhMark = k_lh_3*lhValue + b_lh_3;
        } else {
            lhMark = k_lh_4*lhValue + b_lh_4;
        }

        double fshKoef = 10;
        double k_fsh_1 = -3.33;
        double k_fsh_2 = -2.7;
        double k_fsh_3 = -6.45;
        double k_fsh_4 = 0;
        double b_fsh_1 = 6;
        double b_fsh_2 = 5.43;
        double b_fsh_3 = 10.19;
        double b_fsh_4 = 0;
        double fshPos1 = 0.9;
        double fshPos2 = 1.27;
        double fshPos3 = 1.57;
        double fshMark = 0;
        boolean fshFound = false;
        if (fshValue != null) {
            fshFound = true;
        }
        if (!fshFound) {
            fshMark = 0;
        } else if (fshValue <= fshPos1) {
            fshMark = k_fsh_1*fshValue + b_fsh_1;
        } else if (fshValue <= fshPos2) {
            fshMark = k_fsh_2*fshValue + b_fsh_2;
            fshKoef = 8.6;
        } else if (fshValue < fshPos3) {
            fshMark = k_fsh_3*fshValue + b_fsh_3;
            fshKoef = 8.6;
        } else {
            fshMark = k_fsh_4*fshValue + b_fsh_4;
            fshKoef = 8.6;
        }

        double tesKoef = 7.5;
        double k_tes_1 = -27.27;
        double k_tes_2 = -2.70;
        double k_tes_3 = -1.64;
        double k_tes_4 = 0;
        double b_tes_1 = 6;
        double b_tes_2 = 3.30;
        double b_tes_3 = 2.79;
        double b_tes_4 = 0;
        double tesPos1 = 0.11;
        double tesPos2 = 0.49;
        double tesPos3 = 1.71;
        double tesMark = 0;
        boolean tesFound = false;
        if (tesValue != null) {
            tesFound = true;
        }
        if (!tesFound) {
            tesMark = 0;
        } else if (tesValue <= tesPos1) {
            tesMark = k_tes_1*tesValue + b_tes_1;
        } else if (tesValue <= tesPos2) {
            tesMark = k_tes_2*tesValue + b_tes_2;
        } else if (tesValue < tesPos3) {
            tesMark = k_tes_3*tesValue + b_tes_3;
        } else {
            tesMark = k_tes_4*tesValue + b_tes_4;
        }


        double inhKoef = 6;
        double k_inh_1 = -0.07;
        double k_inh_2 = -0.25;
        double k_inh_3 = 0;
        double b_inh_1 = 10;
        double b_inh_2 = 16.25;
        double b_inh_3 = 0;
        double inhPos1 = 35;
        double inhPos2 = 65.01;
        double inhMark = 0;
        boolean inhFound = false;
        if (inhValue != null) {
            inhFound = true;
        }
        if (!inhFound) {
            inhMark = 0;
        } else if (inhValue <= inhPos1) {
            inhMark = k_inh_1*inhValue + b_inh_1;
        } else if (inhValue <= inhPos2) {
            inhMark = k_inh_2*inhValue + b_inh_2;
        } else {
            inhMark = k_inh_3*inhValue + b_inh_3;
        }


        double totalMark = lhKoef*lhMark + fshKoef*fshMark + tesKoef*tesMark + inhKoef*inhMark;

        double totalMaxMark = 0;
        if (lhFound) {
            totalMaxMark +=5*lhKoef;
        }
        if (fshFound) {
            totalMaxMark +=6*fshKoef;
        }
        if (tesFound) {
            totalMaxMark +=6*tesKoef;
        }
        if (inhFound) {
            totalMaxMark +=10*inhKoef;
        }

        if (totalMaxMark == 0) {
            return -1000;
        }

        Date birthDate = CalcChildren.childrenMap.get(analysis.getChildID()).getBirthdate();
        Date analysisDate = analysis.getAnalysisDate();
        long monthAge = getMonthAge(birthDate,analysisDate);
        totalMaxMark = totalMaxMark;
        double mark = totalMark/totalMaxMark;
        if (monthAge >= minMonth) {
//            double k = getK(monthAge);
            double k = getKCubic(monthAge);
            mark = (k+mark-1)/k;
        }
        return mark*100;
    }

    private double getK(long monthAge) {
        double k = (maxK-minK)/(maxMonth-minMonth);
        double b = minK-k*minMonth;
        return k*monthAge+b;
    }

    private double getKCubic (long monthAge) {
        double x = monthAge/12 - 15;
        double y = (x*(x*x-3*x+8))/12;
        return y + 1;
    }

    private long getMonthAge(Date childBirth, Date analysisDate) {
        return ((analysisDate.getTime() - childBirth.getTime())/(1000*60*60*24))/30;
    }

    @Override
    public String getName() {
        return name;
    }
}
