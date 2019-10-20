package alma.obops.aqua.qa0.configuration;

import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyHbmImpl;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import alma.obops.utils.config.RelationalDbConfig;

/**
 * Configure the application's data source according to ALMA's conventions, e.g.
 * storing the data source parameters in
 * <em>$ACSDATA/config/archiveConfig.properties</em>
 * 
 * @author amchavan
 */

@Configuration
@PropertySource("file:${ACSDATA}/config/archiveConfig.properties")
public class AlmaDataSourceConfiguration {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	Environment env;
	
	@Bean
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource dataSource() {

        String url      = env.getProperty( "archive.relational.connection" );
		String username = env.getProperty( "archive.relational.user" );
		String password = env.getProperty( "archive.relational.passwd" );

		logger.info( "Database URL : " + url );
		logger.info( "Database user: " + username );

		java.util.Properties props = new java.util.Properties();
		props.put("v$session.program", "SnooPI");

		return DataSourceBuilder
		        .create()
		        .username( username )
		        .password( password )
		        .url( url )
		        .build();
	}

	@Bean
	public RelationalDbConfig dbConfig() {
		return new alma.obops.utils.config.RelationalDbConfig();
	}

	@Bean
	public SessionFactory sessionFactory(@Qualifier("entityManagerFactory") EntityManagerFactory emf) {
		return emf.unwrap(SessionFactory.class);
	}

//	@Bean
//	public PhysicalNamingStrategy physicalNamingStrategy() {
//		return new SpringPhysicalNamingStrategy();
////		return new PhysicalNamingStrategyStandardImpl();
//	}

//	@Bean
//	public ImplicitNamingStrategy implicitNamingStrategy() {
//		return new ImplicitNamingStrategyLegacyHbmImpl();
//	}

	@Bean
	@Primary
	public EntityManagerFactory entityManagerFactory() {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(false);
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.current_session_context_class", env.getProperty("spring.jpa.properties.hibernate.current_session_context_class"));
		properties.put("hibernate.physical_naming_strategy", env.getProperty("spring.jpa.hibernate.naming.physical-strategy"));
		factory.setJpaPropertyMap(properties);

		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan(
				"alma.obops.aqua", 
				"alma.obops.dam", 
				"alma.lifecycle",
				"alma.obops.boot.security");
		factory.setDataSource(dataSource());
		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
		return new JpaTransactionManager(sessionFactory);
	}

}

