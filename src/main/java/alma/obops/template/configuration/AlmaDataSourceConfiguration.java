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

package alma.obops.template.configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import alma.obops.utils.config.RelationalDbConfig;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
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

import java.util.HashMap;

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
		props.put("v$session.program", "template");

		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setProperties(props);
		dataSource.setUser(username);
		dataSource.setPassword(password);
		dataSource.setJdbcUrl(url);

		return dataSource;
	}

	@Bean
	public RelationalDbConfig dbConfig() {
		return new alma.obops.utils.config.RelationalDbConfig();
	}

	@Bean
	public SessionFactory sessionFactory(@Qualifier("entityManagerFactory") EntityManagerFactory emf) {
		return emf.unwrap(SessionFactory.class);
	}

	@Bean
	public PhysicalNamingStrategy physicalNamingStrategy() {
		return new SpringPhysicalNamingStrategy();
	}

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
		factory.setPackagesToScan("alma.obops.template", "alma.obops.boot.security");
		factory.setDataSource(dataSource());
		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
		return new JpaTransactionManager(sessionFactory);
	}
}

