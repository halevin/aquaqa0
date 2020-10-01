package alma.obops.aqua.qa0.service;

import alma.asdm.AsdmTables;
import alma.asdm.domain.SourceCoverage;
import alma.obops.aqua.service.asdm.ASDMAtmosphereSummaryContainer;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AquaUserSession {

    private static final int CLEANING_TIMEOUT = 3600 * 1000;

    private Map<String, AsdmTables> asdmTablesMap = new HashMap<>();

    private Map<String, Set<SourceCoverage>> coveragesMap = new HashMap<>();

    private Map<String, ASDMAtmosphereSummaryContainer> atmosphereSummaryContainerMap = new HashMap<>();


    public Map<String, AsdmTables> getAsdmTablesMap() {
        return asdmTablesMap;
    }

    public Map<String, Set<SourceCoverage>> getCoveragesMap() {
        return coveragesMap;
    }

    public Map<String, ASDMAtmosphereSummaryContainer> getAtmosphereSummaryContainerMap() {
        return atmosphereSummaryContainerMap;
    }

    @Scheduled(fixedDelay = CLEANING_TIMEOUT, initialDelay = CLEANING_TIMEOUT)
    private void clearMemory() {

    }

}
