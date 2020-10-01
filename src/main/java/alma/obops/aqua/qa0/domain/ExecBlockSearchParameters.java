package alma.obops.aqua.qa0.domain;

import java.util.Date;

public class ExecBlockSearchParameters {

    // Correlator
    private boolean bl;
    private boolean aca;
    // Project type
    private boolean science;
    private boolean calibrator;
    // QA0 Flag
    private boolean unset;
    private boolean pending;
    private boolean pass;
    private boolean semipass;
    private boolean fail;

    private Date dateStart;
    private Date dateEnd;

    private String projectCode;
    private String obsUnitSetStatusID;
    private String schedBlockName;
    private String schedBlockUID;
    private String execBlockUID;

    public boolean isBl() {
        return bl;
    }

    public void setBl(boolean bl) {
        this.bl = bl;
    }

    public boolean isAca() {
        return aca;
    }

    public void setAca(boolean aca) {
        this.aca = aca;
    }

    public boolean isScience() {
        return science;
    }

    public void setScience(boolean science) {
        this.science = science;
    }

    public boolean isCalibrator() {
        return calibrator;
    }

    public void setCalibrator(boolean calibrator) {
        this.calibrator = calibrator;
    }

    public boolean isUnset() {
        return unset;
    }

    public void setUnset(boolean unset) {
        this.unset = unset;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public boolean isSemipass() {
        return semipass;
    }

    public void setSemipass(boolean semipass) {
        this.semipass = semipass;
    }

    public boolean isFail() {
        return fail;
    }

    public void setFail(boolean fail) {
        this.fail = fail;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getObsUnitSetStatusID() {
        return obsUnitSetStatusID;
    }

    public void setObsUnitSetStatusID(String obsUnitSetStatusID) {
        this.obsUnitSetStatusID = obsUnitSetStatusID;
    }

    public String getSchedBlockName() {
        return schedBlockName;
    }

    public void setSchedBlockName(String schedBlockName) {
        this.schedBlockName = schedBlockName;
    }

    public String getSchedBlockUID() {
        return schedBlockUID;
    }

    public void setSchedBlockUID(String schedBlockUID) {
        this.schedBlockUID = schedBlockUID;
    }

    public String getExecBlockUID() {
        return execBlockUID;
    }

    public void setExecBlockUID(String execBlockUID) {
        this.execBlockUID = execBlockUID;
    }


    @Override
    public String toString() {
        return "ExecBlockSearchParameters{" +
                "bl=" + bl +
                ", aca=" + aca +
                ", science=" + science +
                ", calibrator=" + calibrator +
                ", unset=" + unset +
                ", pending=" + pending +
                ", pass=" + pass +
                ", semipass=" + semipass +
                ", fail=" + fail +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                ", projectCode='" + projectCode + '\'' +
                ", obsUnitSetStatusID='" + obsUnitSetStatusID + '\'' +
                ", schedBlockName='" + schedBlockName + '\'' +
                ", schedBlockUID='" + schedBlockUID + '\'' +
                ", execBlockUID='" + execBlockUID + '\'' +
                '}';
    }
}