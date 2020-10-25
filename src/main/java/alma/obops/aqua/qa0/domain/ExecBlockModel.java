package alma.obops.aqua.qa0.domain;

import alma.obops.aqua.domain.aoscheck.AosCheckSummary;

import java.util.List;

public class ExecBlockModel {

    public String execBlockUid;
    public String qa0Status;
    public String qa0StatusReason;
    public String finalComment;
    public String execFraction;
    public String jiraTickets;
    public String schedBlockUID;
    public String ousStatusUid;
    public String schedBlockName;
    public String obsProjectCode;
    public String array;
    public String correlator;
    public String representativeFrequency;
    public AosCheckSummary aosCheckSummary;
    public String weatherData;
    public String phaseRMS;
    public String antennasInfo;
    public String bandDetails;
    public boolean isFluxCalibratorMissing;
    public List<String> warnings;
    public boolean isAntennasDetailsAsWarning;
    public boolean isBandDetailsAsWarning;


    public ExecBlockModel(){}


    @Override
    public String toString() {
        return "ExecBlockModel{" +
                "execBlockUid='" + execBlockUid + '\'' +
                ", qa0Status='" + qa0Status + '\'' +
                ", qa0StatusReason='" + qa0StatusReason + '\'' +
                ", finalComment='" + finalComment + '\'' +
                ", execFraction='" + execFraction + '\'' +
                ", jiraTickets='" + jiraTickets + '\'' +
                ", schedBlockUID='" + schedBlockUID + '\'' +
                ", schedBlockName='" + schedBlockName + '\'' +
                ", obsProjectCode='" + obsProjectCode + '\'' +
                ", array='" + array + '\'' +
                ", correlator='" + correlator + '\'' +
                ", representativeFrequency='" + representativeFrequency + '\'' +
                ", aosCheckSummary=" + aosCheckSummary +
                ", weatherData='" + weatherData + '\'' +
                ", phaseRMS='" + phaseRMS + '\'' +
                ", antennasInfo='" + antennasInfo + '\'' +
                '}';
    }
}
