package alma.obops.template;

import alma.icd.adapt.messagebus.MessageBroker;
import alma.obops.template.domain.StateWithSubstate;
import alma.obops.template.persistence.ObsUnitSetDao;
import alma.obops.utils.FileIoUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Application.UNIT_TEST_PROFILE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties=
        {"spring.autoconfigure.exclude=alma.icd.adapt.messagebus.configuration.RabbitMqMessageBrokerConfiguration.class"})
public class TestObsUnitSetDao {

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    ObsUnitSetDao obsUnitSetDao;

    @Autowired
    MessageBroker messageBroker;

    @PostConstruct
    private void postConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Before
    public void createTables() throws Exception {
        runScript("create-hsqldb-ddl.sql", dataSource);
        String sqlData = "INSERT INTO OBS_UNIT_SET_STATUS (STATUS_ENTITY_ID, DOMAIN_ENTITY_STATE, OBS_PROJECT_ID, FLAGS) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sqlData, new Object[] {"uid://A001/X21f/X19", "ReadyForProcessing", "000", "PipelineCalibration"});
//        jdbcTemplate.update(sqlData, new Object[] {"uid://A001/X2fb/X7d5", "ReadyForProcessing", "000", null});
//        jdbcTemplate.update(sqlData, new Object[] {"uid://A001/X2fb/X44b", "ReadyForProcessing", "000", null});
//        jdbcTemplate.update(sqlData, new Object[] {"uid://A001/X33e/X15", "Processing", "000", null});
//        jdbcTemplate.update(sqlData, new Object[] {"uid://A001/X121/X307", "Processing", "000", null});
    }

    @After
    public void dropTables() throws Exception {
        runScript("drop-hsqldb-ddl.sql", dataSource);
    }

    protected void runScript(String scriptLocation, DataSource dataSource) throws IOException, SQLException {
        InputStream is = FileIoUtils.getResourceStream(scriptLocation);
        String sql = FileIoUtils.fileToString(is);
        jdbcTemplate.update(sql);
    }

    @Test
    public void testObsUnitSetsCriteria(){

        StateWithSubstate stateWithSubstate = obsUnitSetDao.obsUnitSetGetState("uid://A001/X21f/X19");

        Assert.isTrue(stateWithSubstate != null,"stateWithSubstate is null");
        Assert.isTrue(stateWithSubstate.state == "ReadyForProcessing","Incorrect state for ObsUnitSet");
        Assert.isTrue(stateWithSubstate.substate == "PipelineCalibration","Incorrect state for ObsUnitSet");
    }


}
