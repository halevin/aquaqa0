/*******************************************************************************
 * ALMA - Atacama Large Millimeter Array
 * Copyright (c) ESO - European Southern Observatory, 2019
 * (in the framework of the ALMA collaboration).
 * All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
 *******************************************************************************/
package alma.obops.aqua.qa0.service;

import alma.entity.xmlbinding.valuetypes.types.StatusTStateType;
import alma.lifecycle.persistence.domain.StateChangeRecord;
import alma.lifecycle.stateengine.constants.StateFlag;
import alma.obops.aqua.AquaConstants;
import alma.obops.aqua.ObsUnitSetSearchCriteria;
import alma.obops.aqua.dao.ExecBlockDao;
import alma.obops.aqua.dao.ObsUnitSetHibernateDao;
import alma.obops.aqua.domain.ObsUnitSet;
import alma.obops.aqua.domain.ObsUnitSetComment;
import alma.obops.aqua.qa0.domain.QA2DashboardModel;
import alma.obops.common.domain.ObopsTool;
import alma.obops.common.domain.OusFlagName;
import alma.obops.common.domain.SearchType;
import alma.obops.dam.apdm.domain.AccountToolSearch;
import alma.obops.dam.apdm.domain.AquaExecBlockLite;
import alma.obops.dam.apdm.domain.ArcNode;
import alma.obops.dam.apdm.domain.DataReducer;
import alma.obops.dam.apdm.domain.OUSStatusNoXml;
import alma.obops.dam.apdm.domain.ObsProjectView;
import alma.obops.dam.apdm.domain.OusFlag;
import alma.obops.dam.apdm.domain.OusOperations;
import alma.obops.dam.apdm.domain.PrjOperations;
import alma.obops.dam.apdm.domain.SchedBlockView;
import alma.obops.dam.apdm.service.UserQueriesService;
import alma.obops.dam.userreg.domain.PortalAccount;
import alma.obops.dam.userreg.service.PortalAccountService;
import alma.obops.utils.DateUtilsLite;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Transactional
public class QA2DashboardService {

    @Autowired
    private ObsUnitSetHibernateDao obsUnitSetHibernateDao;

    @Autowired
    private ExecBlockDao execBlockDao;

    @Autowired
    private PortalAccountService portalAccountService;

    @Autowired
    private UserQueriesService userQueriesService;

    private static Logger logger = Logger.getLogger(QA2DashboardService.class.getSimpleName());


    public List<QA2DashboardModel> searchObsUnitSets(ObsUnitSetSearchCriteria obsUnitSetSearchCriteria){
        List<QA2DashboardModel> qa2DashboardModelList = new ArrayList<>();

        List<ObsUnitSet> obsUnitSets = obsUnitSetHibernateDao.findWithCriteria(obsUnitSetSearchCriteria);

        for ( ObsUnitSet obsUnitSet : obsUnitSets ) {
            QA2DashboardModel qa2DashboardModel = generateQA2DashboardModel(obsUnitSet);
            qa2DashboardModelList.add(qa2DashboardModel);
        }

        return qa2DashboardModelList;
    }

    private QA2DashboardModel generateQA2DashboardModel(ObsUnitSet obsUnitSet) {
        QA2DashboardModel qa2DashboardModel = new QA2DashboardModel();
        OUSStatusNoXml ousStatusPF = obsUnitSet.getObsUnitSetView().getOusStatusPf();
        if ( ousStatusPF != null ) {
            try {
                String ousStatusId = ousStatusPF.getStatusEntityId();

                String state = ousStatusPF.getDomainEntityState();
                qa2DashboardModel.mousState = state;
                String flag = ousStatusPF.getFlags();
                qa2DashboardModel.mousSubState = flag;
                qa2DashboardModel.mousStatusId = ousStatusId;
                OusOperations ousOperations = obsUnitSet.getObsUnitSetView().getOusOperations();
                if (ousOperations != null) {
                    DataReducer dataReducer = ousOperations.getDataReducer();
                    if ( dataReducer != null ) {
                        qa2DashboardModel.dataReducerName = dataReducer.getAccount().getFirstName() + " " + dataReducer.getAccount().getLastName();
                        ArcNode arcNode = dataReducer.getArcNode();
                        if ( arcNode != null ) {
                            qa2DashboardModel.dataReducerArc = arcNode.getArc();
                            qa2DashboardModel.dataReducerNode = arcNode.getNode();
                        }
                    }
                    DataReducer drm = ousOperations.getDrm();
                    if (drm != null) {
                        qa2DashboardModel.drmName = drm.getAccount().getFirstName() + " " + drm.getAccount().getLastName();
                    }
                }
                qa2DashboardModel.plProcessingExec = findOusFlagValue(ousStatusPF.getOusFlags(), OusFlagName.PL_PROCESSING_EXECUTIVE);
                qa2DashboardModel.projectsCode = obsUnitSet.getObsProjectCode();
                if (obsUnitSet.getJiraTickets() != null) {
                    List<String> prtsprTickets = Arrays.asList(obsUnitSet.getJiraTickets().split(","));
                    qa2DashboardModel.prtsprTickets = prtsprTickets;
                }

                ObsProjectView obsProjectView = obsUnitSet.getObsUnitSetView().getObsProjectView();
                Set<AquaExecBlockLite> execBlocks = null;
                if ( obsProjectView != null ) {
                    Set<SchedBlockView> schedBlockSet = obsProjectView.getSchedBlockViews();
                    if (schedBlockSet != null && schedBlockSet.size() > 0) {
                        for ( SchedBlockView schedBlockView : schedBlockSet ) {
                            if ( schedBlockView.getSchedBlockStatusPf().getParentOusStatusId().equals(ousStatusId)) {
                                qa2DashboardModel.receiverBand = schedBlockView.getReceiverBand();
                                qa2DashboardModel.schedBlockName = schedBlockView.getName();
                                execBlocks = schedBlockView.getAquaExecBlocks();
                            }
                        }
                    }

                    if (obsUnitSet.getObsUnitSetView().getObsProjectView().getPiAccount()!=null &&
                            obsUnitSet.getObsUnitSetView().getObsProjectView().getPiAccount().getPreferredArc() != null ) {
                        qa2DashboardModel.preferredArc = obsUnitSet.getObsUnitSetView().getObsProjectView().getPiAccount().getPreferredArc().toUpperCase();
                    }

                    PrjOperations prjOperations = obsUnitSet.getObsUnitSetView().getObsProjectView().getPrjOperations();
                    if (prjOperations != null && prjOperations.getContactScientistAccount() != null) {
                        PortalAccount contactScientist = prjOperations.getContactScientistAccount();
                        qa2DashboardModel.contactScientistName = contactScientist.getFirstName() + " " + contactScientist.getLastName();
                    }

                }

                Date latestQa0PassExecBlockDate = null;
                if (execBlocks != null) {
                    List<AquaExecBlockLite> passExecBlockViews = execBlocks
                            .stream()
                            .filter(execBlock -> execBlock.getQA0Status().equals("Pass"))
                            .collect(Collectors.toList());
                    qa2DashboardModel.numberQA0PassExecBlocks = passExecBlockViews.size();
                    if (passExecBlockViews != null && passExecBlockViews.size() > 0) {
                        latestQa0PassExecBlockDate = passExecBlockViews.get(passExecBlockViews.size() - 1).getStartTime();
                        qa2DashboardModel.latestQa0PassExecBlockDate = dateFormatter(latestQa0PassExecBlockDate);
                    }
                }

                List<StateChangeRecord> records = ousStatusPF.getStateChangeRecords();

                Date firstReadyForProcessingDate = getTheFirstStateOccuranceDate(records, StatusTStateType.READYFORPROCESSING);
                qa2DashboardModel.firstDateReadyForProcessing = dateFormatter(firstReadyForProcessingDate);

                Date firstProcessingDate = getTheFirstStateOccuranceDate(records, StatusTStateType.PROCESSING);
                qa2DashboardModel.firstDateProcessing = dateFormatter(firstProcessingDate);

                Date lastReadyForReviewDate = getTheLastStateOccuranceDate(records, StatusTStateType.READYFORREVIEW);
                qa2DashboardModel.lastDateReadyForReview = dateFormatter(lastReadyForReviewDate);

                Date lastVerifiedDate = getTheLastStateOccuranceDate(records, StatusTStateType.VERIFIED);
                qa2DashboardModel.lastDateVerified = dateFormatter(lastVerifiedDate);

                qa2DashboardModel.qa2Status = obsUnitSet.getQA2Status().toString();

                Date lastDeliveredDate = getTheLastStateOccuranceDate(records, StatusTStateType.DELIVERED);
                qa2DashboardModel.lastDateDelivered = dateFormatter(lastDeliveredDate);

                qa2DashboardModel.stagingTimeDays = getDifferenceDays(latestQa0PassExecBlockDate, firstReadyForProcessingDate);
                qa2DashboardModel.assignmentTimeDays = getDifferenceDays(firstReadyForProcessingDate, firstProcessingDate);
                qa2DashboardModel.analysisTimeDays = getDifferenceDays(lastReadyForReviewDate, firstProcessingDate);
                qa2DashboardModel.reviewTimeDays = getDifferenceDays(lastVerifiedDate, lastReadyForReviewDate);
                qa2DashboardModel.deliveryTimeDays = getDifferenceDays(lastDeliveredDate, lastVerifiedDate);
                qa2DashboardModel.obsToDeliveryTimeDays = getDifferenceDays(lastDeliveredDate, latestQa0PassExecBlockDate);

                Date pipeCalDate = getTheLastStateOccuranceDate(records, StatusTStateType.PROCESSING, StateFlag.PIPELINECALIBRATION);
                qa2DashboardModel.pipeCalDate = dateFormatter(pipeCalDate);

                Date manualCalDate = getTheLastStateOccuranceDate(records, StatusTStateType.PROCESSING, StateFlag.MANUALCALIBRATION);
                qa2DashboardModel.manualCalDate = dateFormatter(manualCalDate);

                Date pipeImgDate = getTheLastStateOccuranceDate(records, StatusTStateType.PROCESSING, StateFlag.PIPELINEIMAGING);
                qa2DashboardModel.pipeImgDate = dateFormatter(pipeImgDate);

                Date manualImgDate = getTheLastStateOccuranceDate(records, StatusTStateType.PROCESSING, StateFlag.MANUALIMAGING);
                qa2DashboardModel.manualImgDate = dateFormatter(manualImgDate);

                ObsUnitSetComment comment = findCommentForOUS(obsUnitSet);
                qa2DashboardModel.comment = comment == null ? "" : comment.getComment();
            } catch (Exception e) {
                logger.warning(e.getMessage());
            }
        }
        return qa2DashboardModel;
    }

    public void saveObsUnitSetComment(String obsUnitSetId, String commentText, String author) {

        ObsUnitSet obsUnitSet = obsUnitSetHibernateDao.findByOUSStatusUID(obsUnitSetId);

        if ( obsUnitSet != null ) {
            ObsUnitSetComment comment = findCommentForOUS(obsUnitSet);
            if ( comment == null) {
                comment = new ObsUnitSetComment();
                comment.setObsUnitSet(obsUnitSet);
                comment.setCommentSection(AquaConstants.QA2_DASHBOARD_SECTION);
            }
            comment.setAuthor(author);
            comment.setTimestamp(DateUtilsLite.getNowUT());
            comment.setComment(commentText);
            obsUnitSetHibernateDao.update(comment);
        } else {
            throw new RuntimeException("ObsUnitSet "+obsUnitSetId+" is not found");
        }

    }

    public List<AccountToolSearch> getUserFilters(String userId ){

        PortalAccount account = portalAccountService.findByAccountId(userId);
        return userQueriesService.findSearchCriteria(ObopsTool.DRATOOL, SearchType.QA2DB, account);

    }

    public void saveUserFilter(String name, Map<String, Object> filter, String userId ) {
        PortalAccount account = portalAccountService.findByAccountId(userId);
        try {
            userQueriesService.saveSearchCriteria(null, filter, ObopsTool.DRATOOL, SearchType.QA2DB, name, account);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void deleteUserFilter( String name, String userId ) {
        PortalAccount account = portalAccountService.findByAccountId(userId);
        AccountToolSearch accountToolSearch =  userQueriesService.findSearchCriterium(ObopsTool.DRATOOL, SearchType.QA2DB, account, name);
        userQueriesService.deleteSearchCriteria(accountToolSearch);
    }

    public AccountToolSearch getUserFilter( String name, String userId ) {

        PortalAccount account = portalAccountService.findByAccountId(userId);
        return userQueriesService.findSearchCriterium(ObopsTool.DRATOOL, SearchType.QA2DB, account, name);

    }




    private ObsUnitSetComment findCommentForOUS(ObsUnitSet obsUnitSet) {

        if ( obsUnitSet != null && obsUnitSet.getComments() != null ) {
            for ( ObsUnitSetComment obsUnitSetComment : obsUnitSet.getComments() ) {
                if ( AquaConstants.QA2_DASHBOARD_SECTION.equals(obsUnitSetComment.getCommentSection())) {
                    obsUnitSetHibernateDao.refresh(obsUnitSetComment);
                    return obsUnitSetComment;
                }
            }
        }
        return null;
    }

    private String dateFormatter(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        if ( date != null ) return simpleDateFormat.format(date);
        return "-";
    }

    private Date getTheFirstStateOccuranceDate(List<StateChangeRecord> records, StatusTStateType statusTStateType ) {
        if ( records != null && statusTStateType != null ) {
            for (StateChangeRecord record : records) {
                if (statusTStateType.toString().equals(record.getDomainEntityState())) {
                    return record.getTimestamp();
                }
            }
        }
        return null;
    }

    private Date getTheLastStateOccuranceDate(List<StateChangeRecord> records, StatusTStateType statusTStateType ) {
        if ( records != null && statusTStateType != null ) {
            for (int i = records.size()-1; i !=0 ; i--) {
                StateChangeRecord record = records.get(i);
                if (statusTStateType.toString().equals(record.getDomainEntityState())) {
                    return record.getTimestamp();
                }
            }
        }
        return null;
    }

    private Date getTheLastStateOccuranceDate(List<StateChangeRecord> records, StatusTStateType statusTStateType, StateFlag stateFlag) {
        if ( records != null && statusTStateType != null && stateFlag != null ) {
            for (int i = records.size()-1; i !=0 ; i--) {
                StateChangeRecord record = records.get(i);
                if (statusTStateType.toString().equals(record.getDomainEntityState()) &&
                    stateFlag.toString().equals(record.getFlags())) {
                    return record.getTimestamp();
                }
            }
        }
        return null;
    }


    private double getDifferenceDays(Date date1, Date date2) {
        if ( date1 != null && date2 != null ) {
            return ChronoUnit.DAYS.between(date1.toInstant(), date2.toInstant());
        }
        return -1;
    }

    private String findOusFlagValue(Set<OusFlag> ousFlags, OusFlagName ousFlagName) {

        if ( ousFlags != null && ousFlagName != null ) {
            for ( OusFlag ousFlag : ousFlags ) {
                if ( ousFlag.getFlagName().equals(ousFlagName.name())) {
                    return ousFlag.getFlagValue();
                }
            }
        }
        return "";
    }

}
