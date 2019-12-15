/*******************************************************************************
 * ALMA - Atacama Large Millimeter Array
 * Copyright (c) ESO - European Southern Observatory, 2018
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

package alma.obops.aqua.qa0.persistence;

import static alma.obops.aqua.qa0.DRAToolConstants.QUALIFICATION_MANUAL_CAL;
import static alma.obops.aqua.qa0.DRAToolConstants.QUALIFICATION_MANUAL_IMG;
import static alma.obops.aqua.qa0.DRAToolConstants.QUALIFICATION_QA2_APPROVAL;
import static alma.obops.aqua.qa0.DRAToolConstants.QUALIFICATION_WEBLOG_REVIEW;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import alma.obops.aqua.qa0.domain.DTDataReducer;

@Service
public class DataReducerRepositoryImpl implements DataReducerRepository {

    private static final Logger logger = Logger.getLogger(DataReducerRepository.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Autowired
    AvailabilityDao availabilityDao;

    @Override
    public List<DTDataReducer> findDataReducers(String name, String arc, String node, Set<String> qualifications, boolean withAvailability) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DTDataReducer> query = cb.createQuery(DTDataReducer.class).distinct(true);
        Root<DTDataReducer> dr = query.from(DTDataReducer.class);
        Predicate cond = cb.conjunction();

        if ( withAvailability ) {
            dr.fetch("availabilityList", JoinType.LEFT);
            cond = cb.and(cond, cb.isNotEmpty(dr.get("availabilityList")));
        }

        query.select(dr);

        if ( name != null && name.length() != 0) {
            Predicate p1  = cb.like(dr.join("user").get("username"), "%" + name + "%");
            Predicate p2  = cb.like(dr.join("user").get("firstname"), "%" + name + "%");
            Predicate p3  = cb.like(dr.join("user").get("lastname"), "%" + name + "%");
            cond = cb.and(cond, cb.or(p1, p2, p3));
        }

        if ( arc != null && arc.length() != 0) {
            cond = cb.and(cond, cb.like(dr.join("arcNode").get("arc"), arc));
        }

        if ( node != null && node.length() != 0) {
            cond = cb.and(cond, cb.like(dr.join("arcNode").get("node"), node));
        }

        if ( qualifications != null && qualifications.size() >0 ) {
            Predicate disj = cb.disjunction();
            if (qualifications.contains(QUALIFICATION_MANUAL_CAL)) {
                disj = cb.or(disj, cb.isMember(QUALIFICATION_MANUAL_CAL, dr.get("qualifications")));
            }
            if (qualifications.contains(QUALIFICATION_MANUAL_IMG)) {
                disj = cb.or(disj, cb.isMember(QUALIFICATION_MANUAL_IMG, dr.get("qualifications")));
            }
            if (qualifications.contains(QUALIFICATION_QA2_APPROVAL)) {
                disj = cb.or(disj, cb.isMember(QUALIFICATION_QA2_APPROVAL, dr.get("qualifications")));
            }
            if (qualifications.contains(QUALIFICATION_WEBLOG_REVIEW)) {
                disj = cb.or(disj, cb.isMember(QUALIFICATION_WEBLOG_REVIEW, dr.get("qualifications")));
            }
            cond = cb.and(cond, disj);
        }

        query.where(cond);
        TypedQuery<DTDataReducer> q = em.createQuery(query);
        List<DTDataReducer> resultList = q.getResultList();

        calculateLoad(resultList, withAvailability);


        return resultList;
    }

    private void calculateLoad(List<DTDataReducer> dataReducers, boolean withAvailability) {
        List<String> drUserIds = new ArrayList<>();
        for (DTDataReducer d : dataReducers) {
            drUserIds.add(d.getUserId());
        }
        if ( drUserIds.size() > 0 && withAvailability ) {
            Query loadQuery = em.createNativeQuery("select oo.DATA_REDUCER_ACCOUNT_ID, count(*) from ous_operations oo " +
                    "left join drm_data_reducer ddr on oo.DATA_REDUCER_ACCOUNT_ID = ddr.DR_USER_ID " +
//                                    "left join drm_node dn on ddr.NODE_ID = dn.id " +
                    "left join OBS_UNIT_SET_STATUS ouss on ouss.STATUS_ENTITY_ID = oo.OBS_UNIT_SET_ID " +
                    "WHERE oo.DATA_REDUCER_ACCOUNT_ID in (?1) " +
                    "AND ( ouss.DOMAIN_ENTITY_STATE = 'ReadyForProcessing' " +
                    "OR ouss.DOMAIN_ENTITY_STATE = 'Processing' " +
                    "OR ouss.DOMAIN_ENTITY_STATE = 'ProcessingProblem' " +
                    "OR ouss.DOMAIN_ENTITY_STATE = 'ReadyForReview' " +
                    "OR ouss.DOMAIN_ENTITY_STATE = 'Reviewing' " +
                    "OR ouss.DOMAIN_ENTITY_STATE = 'Verified' " +
                    "OR ouss.DOMAIN_ENTITY_STATE = 'QA3InProgress' )" +
                    "GROUP BY oo.DATA_REDUCER_ACCOUNT_ID ");
            loadQuery.setParameter(1, drUserIds);
            List<Object[]> result = loadQuery.getResultList();
            Map<String, BigDecimal> loadMap = new HashMap<>();
            for (Object[] ob : result) {
                loadMap.put((String)ob[0], (BigDecimal) ob[1]);
            }

            for (DTDataReducer d : dataReducers) {
                BigDecimal loadValue = loadMap.get(d.getUserId());
                int load = (loadValue!=null)?loadValue.intValueExact():0;
                d.setLoad(load);
            }
        }
    }
}
