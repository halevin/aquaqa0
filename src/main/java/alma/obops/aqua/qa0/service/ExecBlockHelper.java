package alma.obops.aqua.qa0.service;

import alma.asdm.AsdmFlagTable;
import alma.asdm.AsdmTables;
import alma.asdm.AsdmUids;
import alma.asdm.domain.Scan;
import alma.asdm.domain.SourceCoverage;
import alma.asdm.service.AsdmService;
import alma.entity.xmlbinding.obsproject.types.ControlBlockTArrayRequestedType;
import alma.entity.xmlbinding.schedblock.SchedBlock;
import alma.obops.aqua.domain.*;
import alma.obops.aqua.domain.aoscheck.AosCheckSummary;
import alma.obops.aqua.qa0.domain.AquaStatusHistoryModel;
import alma.obops.aqua.qa0.domain.ExecBlockCommentModel;
import alma.obops.aqua.qa0.domain.ExecBlockModel;
import alma.obops.aqua.qa0.persistence.CommentRepository;
import alma.obops.aqua.reports.AquaReport;
import alma.obops.aqua.service.*;
import alma.obops.aqua.service.asdm.ASDMAtmosphereSummaryContainer;
import alma.obops.aqua.service.asdm.AquaAsdmService;
import alma.obops.aqua.service.asdm.WeatherResult;
import alma.obops.aqua.service.execfraction.ExecutionFractionCalculator;
import alma.obops.aqua.servlet.AquaQA0ReportProducer;
import alma.obops.aqua.utils.AquaUtils;
import alma.obops.dam.apdm.service.SchedBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ExecBlockHelper {

    private static Logger logger = Logger.getLogger(ExecBlockHelper.class.getSimpleName());

    @Autowired
    private ExecBlockService execBlockService;

    @Autowired
    private ObsUnitSetService obsUnitSetService;

    @Autowired
    private AOSCheckService aosCheckService;

    @Autowired
    private AquaAsdmService aquaAsdmService;

    @Autowired
    private SourceCoverageCalculator sourceCoverageCalculator;

    @Autowired
    private AsdmService asdmService;

    @Autowired
    private ArrayInformationCalculator arrayInformationCalculator;

    @Autowired
    private ExecutionFractionCalculator executionFractionCalculator;

    @Autowired
    private ScanExpert scanExpert;

    @Autowired
    private SchedBlockService schedBlockService;

    @Autowired
    AquaUserSession aquaUserSession;

    @Autowired
    private AquaQA0ReportProducer aquaQA0ReportProducer;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private WarningsHelper warningsHelper;


    public ExecBlockModel build(String execBlockUID) {

        ExecBlock execBlock = execBlockService.findByExecBlockUID(execBlockUID);
        execBlockService.hydrate(execBlock);
        ExecBlockView execBlockView = execBlockService.readExecBlockView(execBlockUID);
        AosCheckSummary aosCheckSummary = aosCheckService.readAosCheckSummary(execBlock);

        AsdmTables aquaAsdm = getAsdmTables(execBlockUID);

        WeatherResult weatherResult = aquaAsdmService.computeWeatherSummary(aquaAsdm);

        ExecBlockModel execBlockModel =  new ExecBlockModel();

        if ( execBlock != null && execBlockView != null ) {
            String schedlBlockUID = execBlockView.getSchedBlockUid();
            ObsUnitSet obsUnitSet = obsUnitSetService.findBySchedBlockUID(schedlBlockUID);

            List<Scan> scans = asdmService.getScans(execBlockUID, aquaAsdm.getAsdmScanTable());

            if ( obsUnitSet != null ) {
                execBlockModel.ousStatusUid = obsUnitSet.getObsUnitSetView().getOusStatusPf().getStatusEntityId();
            }
            execBlockModel.execBlockUid = read(execBlockUID);
            execBlockModel.execFraction = String.format("%.2f",execBlock.getExecFraction());
            execBlockModel.qa0Status = execBlock.getQA0Status().toString();
            execBlockModel.qa0StatusReason = read(execBlock.getQA0StatusReason());
            execBlockModel.finalComment = ( execBlock.getFinalComment() != null ) ? execBlock.getFinalComment().getComment() : "";
            execBlockModel.jiraTickets = read(execBlock.getJiraTickets());
            execBlockModel.obsProjectCode = read(execBlockView.getObsProjectCode());
            execBlockModel.schedBlockUID = read(schedlBlockUID);
            execBlockModel.schedBlockName = read(execBlockView.getSchedBlockName());
            execBlockModel.array = read(execBlockView.getArrayFamily());
            execBlockModel.correlator = read(execBlockView.getCorrelatorType());
            execBlockModel.representativeFrequency = String.format("%.3f GHz",execBlockView.getRepresentativeFrequency());
            execBlockModel.aosCheckSummary = aosCheckSummary;

            execBlockModel.warnings = getWarnings(execBlockView, aquaAsdm, aosCheckSummary, scans);
            execBlockModel.isAntennasDetailsAsWarning = warningsHelper.isAntennasDetailsAsWarning(execBlockView, aosCheckSummary);

            execBlockModel.bandDetails = warningsHelper.getBandDetails(execBlockView, aosCheckSummary);
            execBlockModel.isBandDetailsAsWarning = warningsHelper.isBandDetailsAsWarning(execBlockView, aosCheckSummary);

            if (weatherResult!=null){
                execBlockModel.weatherData = String.format("PWV %.2f mm; Wind %.2f m/s; Humidity %.2f %%; Pressure %.2f hPa",
                        weatherResult.getPwv(), weatherResult.getWind(), weatherResult.getHumidity(), weatherResult.getPressure());
            }

            if ( aosCheckSummary != null ) {
                execBlockModel.antennasInfo = antennasInfo(execBlockView, aosCheckSummary);
                execBlockModel.phaseRMS = String.format("Phase rms: %.3f (microns)", aosCheckSummary.getPhaseRMSantenna());
            }

            SchedBlock schedBlock = schedBlockService.read(execBlockView.getSchedBlockUid());

            boolean isFluxCalibratorMissing	= !scanExpert.isSpectralIndexMeasurementExists(scans, execBlockView.getBandName()) &&
                    !ControlBlockTArrayRequestedType.TP_ARRAY.equals(schedBlock.getObsUnitControl().getArrayRequested());

            execBlockModel.isFluxCalibratorMissing = isFluxCalibratorMissing;
        }

        return execBlockModel;
    }

    public void updateExecBlock(ExecBlockModel execBlockModel) {
        if ( execBlockModel != null ) {
            ExecBlock execBlock = execBlockService.findByExecBlockUID(execBlockModel.execBlockUid);
            execBlockService.hydrateSession(execBlock);
            if ( execBlock == null ) { throw new RuntimeException("Cannot find ExecBlock for UID " + execBlockModel.execBlockUid);}

            Float executionFraction = Float.parseFloat(execBlockModel.execFraction);
            execBlock.setExecFraction(executionFraction);
            ExecBlockComment execBlockComment = execBlock.getFinalComment();
            if ( execBlockComment == null ) { execBlockComment = new ExecBlockComment();}
            execBlockComment.setComment(execBlockModel.finalComment);
            execBlock.setFinalComment(execBlockComment);

            Set<QA0StatusReason> qa0StatueReasons = new HashSet<>();
            if (StringUtils.hasText(execBlockModel.qa0StatusReason)) {
                QA0StatusReason qa0StatusReason = null;
                try {
                    qa0StatusReason = QA0StatusReason.valueOf(execBlockModel.qa0StatusReason);
                } catch (IllegalArgumentException e) {
                    logger.warning("Cannot parse " + execBlockModel.qa0StatusReason + " as QA0StatusReason");
                }
                if (qa0StatusReason != null) {
                    qa0StatueReasons.add(qa0StatusReason);
                }
            }
            execBlock.setQA0StatusReasonsFromSet(qa0StatueReasons);

            execBlockService.update(execBlock);
        }
    }

    private String read(String value){
        return ( value != null ) ? value : "";
    }

    private String antennasInfo(ExecBlockView execBlockView, AosCheckSummary aosCheckSummary){

        int cycle = AquaUtils.estimateCycle(execBlockView.getObsProjectCode());
        int expectedAntennas =  arrayInformationCalculator.getExpectedNumberOfAntennas(execBlockView);
        int	antennas = (aosCheckSummary.getAntennas() != null) ? aosCheckSummary.getAntennas() : 0;

        return antennas + " total " + String.format("(%.1f %% for Cycle %d)", (float) antennas / expectedAntennas * 100.0, cycle);
    }


    public ExecutionFraction computeExecutionFraction(String execBlockUID) {
        ExecBlock execBlock = execBlockService.findByExecBlockUID(execBlockUID);
        execBlockService.hydrate(execBlock);
        AosCheckSummary aosCheckSummary = aosCheckService.readAosCheckSummary(execBlock);

        AsdmTables aquaAsdm = getAsdmTables(execBlockUID);

        return executionFractionCalculator.calculateExecutionFractionForExecBlock(execBlockUID, aosCheckSummary, aquaAsdm);
    }


    public Set<SourceCoverage> getCoverages(String execBlockUID) {

        Set<SourceCoverage> sourceCoverages = null;
        try {
            if ( aquaUserSession.getCoveragesMap().containsKey(execBlockUID) && aquaUserSession.getCoveragesMap().get(execBlockUID) != null ) {
                sourceCoverages = aquaUserSession.getCoveragesMap().get(execBlockUID);
            } else {
                SourceCoverageCalculatorImpl.DISPLAY_INDIVIDUAL_POINTINGS = true;
                SourceCoverageCalculatorImpl.INDIVIDUAL_POINTINGS_SELECTION = 1;
                sourceCoverages = sourceCoverageCalculator.getCoverage(execBlockUID, getAsdmTables(execBlockUID));
                aquaUserSession.getCoveragesMap().put(execBlockUID, sourceCoverages);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return sourceCoverages;

    }

    public List<ExecBlockComment> getExecBlockComments(String execBlockUID) {
        ExecBlock execBlock = execBlockService.findByExecBlockUID(execBlockUID);

        return commentRepository.findByExecBlockOrderByTimestampDesc(execBlock);
    }

    public void updateComment(ExecBlockCommentModel execBlockCommentModel, String author) {
        if ( execBlockCommentModel != null ) {
            ExecBlock execBlock = execBlockService.findByExecBlockUID(execBlockCommentModel.execBlockUid);
            if ( execBlock == null ) { throw new RuntimeException("Cannot find ExecBlock for UID " + execBlockCommentModel.execBlockUid);}

            ExecBlockComment execBlockComment = null;
            if ( execBlockCommentModel.id != null ) {
                Optional<ExecBlockComment> execBlockCommentOpt = commentRepository.findById(execBlockCommentModel.id);
                if ( execBlockCommentOpt.isPresent() ) {
                    execBlockComment = execBlockCommentOpt.get();
                    execBlockComment.setComment(execBlockCommentModel.comment);
                    execBlockComment.setTimestamp(execBlockCommentModel.timestamp);
                }
            } else {
                execBlockComment = new ExecBlockComment();
                execBlockComment.setExecBlock(execBlock);
                execBlockComment.setTimestamp(new Date());
                execBlockComment.setAuthor(author);
                execBlockComment.setComment(execBlockCommentModel.comment);
            }
            if ( execBlockComment != null ) {
                commentRepository.save(execBlockComment);
            }
        }
    }

    public void deleteComment(Long commentId, String author) {
        if ( commentId != null ) {
             commentRepository.deleteById(commentId);
        }
    }

    public ASDMAtmosphereSummaryContainer getAtmosphereSummaryContainer(String execBlockUID) {

        ASDMAtmosphereSummaryContainer atmosphereSummaryContainer = null;
        try {
            if ( aquaUserSession.getAtmosphereSummaryContainerMap().containsKey(execBlockUID) && aquaUserSession.getAtmosphereSummaryContainerMap().get(execBlockUID) != null ) {
                atmosphereSummaryContainer = aquaUserSession.getAtmosphereSummaryContainerMap().get(execBlockUID);
            } else {
                atmosphereSummaryContainer = aquaAsdmService.getAtmosphereSummaries(getAsdmTables(execBlockUID));
                aquaUserSession.getAtmosphereSummaryContainerMap().put(execBlockUID, atmosphereSummaryContainer);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return atmosphereSummaryContainer;

    }



    public AsdmTables getAsdmTables(String execBlockUID) {
        AsdmUids asdmUids = asdmService.getAsdmUids(execBlockUID);

        AsdmTables aquaAsdm = null;
        try{
            if ( aquaUserSession.getAsdmTablesMap().containsKey(execBlockUID) && aquaUserSession.getAsdmTablesMap().get(execBlockUID) != null ) {
                aquaAsdm = aquaUserSession.getAsdmTablesMap().get(execBlockUID);
            } else {
                aquaAsdm = asdmService.initializeAsdm(asdmUids, true);
                aquaUserSession.getAsdmTablesMap().put(execBlockUID, aquaAsdm);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return aquaAsdm;
    }

    public AosCheckSummary readAosCheck(String execBlockUID) {
        ExecBlock execBlock = execBlockService.findByExecBlockUID(execBlockUID);
        return aosCheckService.readAosCheckSummary(execBlock);
    }

    public List<AquaStatusHistoryModel> readQA0StatusHistory(String execBlockUID) {
        List<AquaStatusHistory> qa0history = execBlockService.getQA0StatusHistory(execBlockUID);
        return qa0history.stream().map(aquaStatusHistory ->
                new AquaStatusHistoryModel(aquaStatusHistory.getTimestamp(), aquaStatusHistory.getAuthor().getId(), aquaStatusHistory.getQaStatus(), aquaStatusHistory.getReason(), aquaStatusHistory.getComment())
        ).collect(Collectors.toList());
    }

    public InputStream getQA0Report(String execBlockUID, String reportFormat) throws Exception {

        AquaReport aquaReport = aquaQA0ReportProducer.produceReport(execBlockUID, reportFormat);
        if ( aquaReport != null ) {
            return new ByteArrayInputStream(aquaReport.get_contents());
        } else {
            throw new RuntimeException();
        }

    }

    public List<List<String>> getAntennaFlags(String execBlockUID) {

        AsdmTables asdmTables = getAsdmTables(execBlockUID);
        if ( asdmTables.getAsdmFlagTable() == null ) {
            AsdmFlagTable asdmFlagTable = asdmService.getFlagTable(asdmTables);
            asdmTables.setAsdmFlagTable(asdmFlagTable);
        }

        Map<String, Map<String, Integer>> flagSummaries = aquaAsdmService.computeFlagsSummary(asdmTables);

        List<List<String>> data = new ArrayList<List<String>>();

        List<String> antennas = new ArrayList<String>();
        Set<String> reasons = new HashSet<String>();
        List<String> reasonsList = new ArrayList<String>();

        antennas.addAll(flagSummaries.keySet());
        sort(antennas);

        ArrayList<String> headerLine = new ArrayList<String>();
        headerLine.add("");
        for (String antenna: antennas){
            reasons.addAll(flagSummaries.get(antenna).keySet());
            headerLine.add(antenna);
        }
        data.add(headerLine);

        reasonsList.addAll(reasons);
        sort(reasonsList);

        for (String reason: reasonsList) {
            ArrayList<String> dataLine = new ArrayList<String>();
            dataLine.add(reason);
            for (String antenna: antennas) {
                Integer count = flagSummaries.get(antenna).get(reason);
                if (count==null){
                    dataLine.add("");
                } else {
                    dataLine.add("" + count);
                }
            }
            data.add(dataLine);
        }

        return data;
    }

    public List<String> getWarnings(ExecBlockView execBlockView, AsdmTables asdmTables, AosCheckSummary aosCheckSummary, List<Scan> scans) {
        List<String> warnings = new ArrayList<>();

        ArrayInformation arrayInformation = arrayInformationCalculator.getArrayInformation(execBlockView, asdmTables);

        String fluxCalWarning = warningsHelper.fluxCalWarning(execBlockView, scans);
        if (StringUtils.hasText(fluxCalWarning)) {
            warnings.add(fluxCalWarning);
        }

        String shadowWarning = warningsHelper.getShadowingWarning(execBlockView, asdmTables);
        if (StringUtils.hasText(shadowWarning)) {
            warnings.add(shadowWarning);
        }

//        String fracFlagWarning = warningsHelper.fracFlagWarning(aosCheckSummary);
//        if (StringUtils.hasText(fracFlagWarning)) {
//            warnings.add(fracFlagWarning);
//        }
//
        String remClodWarning = warningsHelper.remCloudWarning(aosCheckSummary);
        if (StringUtils.hasText(remClodWarning)) {
            warnings.add(remClodWarning);
        }

        String arWarning = arrayInformation.getArWarning();
        if (StringUtils.hasText(arWarning)) {
            warnings.add(arWarning);
        }

        String mrsWarning = arrayInformation.getMrsWarning();
        if (StringUtils.hasText(mrsWarning)) {
            warnings.add(mrsWarning);
        }



        return warnings;
    }

    private void sort(List<String> collection){

        Collections.sort(collection, (s1, s2) -> s1.compareToIgnoreCase(s2));

    }


}


