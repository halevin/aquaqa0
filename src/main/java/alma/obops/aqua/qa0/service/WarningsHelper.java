/*******************************************************************************
 * ALMA - Atacama Large Millimeter Array
 * Copyright (c) ESO - European Southern Observatory, 2020
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPO1st pointingSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
 *******************************************************************************/

package alma.obops.aqua.qa0.service;

import alma.asdm.AsdmTables;
import alma.asdm.domain.Scan;
import alma.entity.xmlbinding.schedblock.SchedBlock;
import alma.obops.aqua.domain.ExecBlockView;
import alma.obops.aqua.domain.aoscheck.AosCheckSummary;
import alma.obops.aqua.service.ArrayInformationCalculator;
import alma.obops.aqua.service.ExecBlockService;
import alma.obops.aqua.service.QAWarningsService;
import alma.obops.aqua.service.ScanExpert;
import alma.obops.aqua.service.execfraction.ExecutionFractionCalculator;
import alma.obops.aqua.utils.AquaUtils;
import alma.obops.common.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class WarningsHelper {

    private static Logger logger = Logger.getLogger(ExecBlockHelper.class.getSimpleName());

    @Autowired
    private ArrayInformationCalculator arrayInformationCalculator;

    @Autowired
    private ExecutionFractionCalculator executionFractionCalculator;

    @Autowired
    private ExecBlockService execBlockService;

    @Autowired
    private QAWarningsService qaWarningsService;

    @Autowired
    private ScanExpert scanExpert;

    public String getShadowingWarning(ExecBlockView execBlockView, AsdmTables asdmTables){
        int numberAntennasExpected = arrayInformationCalculator.getExpectedNumberOfAntennas(execBlockView);
        double shadowing = executionFractionCalculator.getShadowingFraction(numberAntennasExpected, asdmTables);
        return qaWarningsService.getShadowingWarning(shadowing*100);
    }

    public String checkBand5Warning(SchedBlock schedBlock){
        return qaWarningsService.getBand5Warning(schedBlock);
    }



    public boolean isAntennasDetailsAsWarning(ExecBlockView execBlockView, AosCheckSummary aosCheckSummary) {
        int cycle = AquaUtils.estimateCycle(execBlockView.getObsProjectCode());
        int minAcceptable = AquaUtils.getMinimumAcceptableNumberOfAntennas(cycle, execBlockView.getArrayFamily());
        int availableAntennas = (aosCheckSummary.getAvailableAntennas() != null) ? aosCheckSummary.getAvailableAntennas() : 0;
        return (availableAntennas < minAcceptable);
    }

    public boolean isBandDetailsAsWarning(ExecBlockView execBlockView, AosCheckSummary aosCheckSummary) {
        String bandDetails = qaWarningsService.getHighestBandWarning(execBlockView, aosCheckSummary.getBandHighestRecommended());
        return bandDetails != null && bandDetails.contains("!");
    }

    public String fracFlagWarning(AosCheckSummary aosCheckSummary){
        return qaWarningsService.getFracFlagWarning(aosCheckSummary.getFracFlag());
    }

    public String remCloudWarning(AosCheckSummary aosCheckSummary) {
        return qaWarningsService.getRemcloudWarning(aosCheckSummary.getRemCloud());
    }

    public String fluxCalWarning(ExecBlockView execBlockView, List<Scan> scans){

        Set<Scan> scansWithMissingSIorFlux = scanExpert.missingSpectralIndexMeasurements(scans, execBlockView.getBandName());
        return qaWarningsService.getMissingSpectralIndexorFluxWarning(scansWithMissingSIorFlux);

    }

    public String getBandDetails(ExecBlockView execBlockView, AosCheckSummary aosCheckSummary){
        return qaWarningsService.getHighestBandWarning(execBlockView, aosCheckSummary.getBandHighestRecommended());
    }



}
