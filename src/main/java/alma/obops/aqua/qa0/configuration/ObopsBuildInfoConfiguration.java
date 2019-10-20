package alma.obops.aqua.qa0.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
@PropertySource("classpath:build-info.properties")
public class ObopsBuildInfoConfiguration {
   @Autowired
   private Environment env;
   
   public String getBuildGitRev() {
       final String propName = "build.info.git.revision";
       final String configData = env.getProperty( propName );
       return configData;
   }
   public String getBuildUser() {
       final String propName = "build.info.user";
       final String configData = env.getProperty( propName );
       return configData;
   }
   public String getBuildHostname() {
       final String propName = "build.info.hostname";
       final String configData = env.getProperty( propName );
       return configData;
   }
   public String getBuildTimestamp() {
       final String propName = "build.info.timestamp";
       final String configData = env.getProperty( propName );
       return configData;
   }
}