package su.ioffe.crescente.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import su.ioffe.crescente.data.db.calculation.AnalysisData;
import su.ioffe.crescente.data.db.calculation.PointsAlgorythm;
import su.ioffe.crescente.data.db.calculation.PointsAlgorythmImplLInearWithAge;
import su.ioffe.crescente.data.db.entities.Analysis;
import su.ioffe.crescente.data.db.entities.Child;
import su.ioffe.crescente.data.db.entities.ChildResult;
import su.ioffe.crescente.data.exceptions.UserNotFoundException;
import su.ioffe.crescente.data.info.UserAccountSummary;
import su.ioffe.crescente.data.utils.DateUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author nikita.zinoviev@gmail.com
 */
@Named("userEditPageBean")
@RequestScoped
public class ResultCalculationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient String childID;

    private transient String birthday;

    private transient String analysisDate;

    private transient String lg;

    private transient String fsg;

    private transient String tst;

    private transient String ing;

    @Inject
    @Named("ServiceBean")
    private transient ServiceBean serviceBean;

    @Inject
    @Named("userBean")
    private transient UserBean userBean;

    @Inject
    private transient SecurityContext securityContext;
//
//    @Inject
//    private transient OpenIdContext openIdContext;

    public ResultCalculationBean() {  }

    public String getChildID() {
        return childID;
    }

    public void setChildID(String childID) {
        this.childID = childID;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAnalysisDate() {
        return analysisDate;
    }

    public void setAnalysisDate(String analysisDate) { this.analysisDate = analysisDate; }

    public String getLg() {
        return lg;
    }

    public void setLg(String lg) {
        this.lg = lg;
    }

    public String getFsg() {
        return fsg;
    }

    public void setFsg(String fsg) {
        this.fsg = fsg;
    }

    public String getTst() {
        return tst;
    }

    public void setTst(String tst) {
        this.tst = tst;
    }
    public String getIng() {
        return ing;
    }

    public void setIng(String ing) {
        this.ing = ing;
    }

    /*
     * JSF action methods, return null to refresh the page
     */
    public String calc() {

        Child child = new Child();
        child.setNickname(getChildID());
        Date birthdate = DateUtils.stringToDate(getBirthday());
        child.setBirthdate(birthdate);

        Analysis analysis = new Analysis();
        analysis.setChild(child);
        Date parsedDate = DateUtils.stringToDate(getAnalysisDate());
        analysis.setAnalysisDate(parsedDate);

        AnalysisData analysisData = new AnalysisData();
        try {
            double lgValue = Double.parseDouble(getLg());
            analysisData.setLhValue(lgValue);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        try {
            double fshValue = Double.parseDouble(getLg());
            analysisData.setFshValue(fshValue);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        try {
            double tstValue = Double.parseDouble(getLg());
            analysisData.setTesValue(tstValue);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        try {
            double ingValue = Double.parseDouble(getLg());
            analysisData.setInhValue(ingValue);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        analysis.setAnalysisData(analysisData);

        PointsAlgorythm algo = new PointsAlgorythmImplLInearWithAge();
        ChildResult result = new ChildResult();
        result.setChildID(analysis.getChildID());
        result.setAlgUsed(algo.getName());
        result.setCalcDate(DateUtils.getToday());
        result.setResult(algo.calcPoints(analysis));


        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Результат: " + result.getResult(), ""));

        return "Users";

    }

}