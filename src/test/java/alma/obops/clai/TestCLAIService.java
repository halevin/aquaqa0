package alma.obops.template;

import alma.lifecycle.stateengine.constants.StateFlag;
import alma.obops.template.service.templateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.logging.Logger;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Application.UNIT_TEST_PROFILE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties=
        {"spring.autoconfigure.exclude=alma.icd.adapt.messagebus.configuration.RabbitMqMessageBrokerConfiguration.class"})
public class TesttemplateService {

    @Autowired
    private templateService templateService;

    @Test
    public void testRunJobTry(){

        String result = templateService.sendJob(false, "uid://A001/X21f/X19", StateFlag.PIPELINECALIBRATION.toString());

        Assert.isTrue(result != null, "sendJob result is null");
        Assert.isTrue(result.contains(Constants.DARED.PipelineCalibration.toString()), "incorrect sendJob result");
        Assert.isTrue(result.contains("--mous=uid://A001/X21f/X19"), "incorrect sendJob result");

        result = templateService.sendJob(false, "uid://A001/X21f/X19", StateFlag.PIPELINECALANDIMG.toString());
        Assert.isTrue(result.contains(Constants.DARED.PipelineCalAndImg.toString()), "incorrect sendJob result");

        result = templateService.sendJob(false, "uid://A001/X21f/X19", StateFlag.PIPELINESINGLEDISH.toString());
        Assert.isTrue(result.contains(Constants.DARED.PipelineSingleDish.toString()), "incorrect sendJob result");

        result = templateService.sendJob(false, "uid://A001/X21f/X19", StateFlag.PIPELINEIMAGING.toString());
        Assert.isTrue(result.contains(Constants.DARED.PipelineImaging.toString()), "incorrect sendJob result");

    }

}
