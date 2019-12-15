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

package alma.obops.aqua.qa0.configuration;

import javax.persistence.EntityManagerFactory;

import alma.obops.utils.config.RelationalDbConfig;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

import java.beans.PropertyVetoException;
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
	@Primary
	public ComboPooledDataSource dataSource() {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		try {
			dataSource.setDriverClass(dbConfig().getDriver());
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		dataSource.setPassword(dbConfig().getPassword());
		dataSource.setUser(dbConfig().getUsername());
		dataSource.setJdbcUrl(dbConfig().getConnectionUrl());
		dataSource.setMinPoolSize(1);
		dataSource.setMaxPoolSize(10);
		dataSource.setMaxConnectionAge(1800);
		dataSource.setMaxIdleTimeExcessConnections(300);
		dataSource.setMaxStatements(0);
		dataSource.setMaxStatementsPerConnection(0);
		dataSource.setNumHelperThreads(6);
		dataSource.setTestConnectionOnCheckout(true);

		if (logger.isInfoEnabled()) {
			logger.info("Data Source created. " + dataSource.toString(true));
		}

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
		factory.setPackagesToScan(
				"alma.obops.aqua.qa0",
				"alma.obops.boot.security",
				"alma.obops.aqua.domain",
				"alma.lifecycle.persistence.domain",
				"alma.obops.dam.apdm.domain",
				"alma.obops.dam.userreg.domain",
				"alma.obops.ocd.domain"
//				"alma.acs.tmcdb",
//				"alma.obops.reporting.security",
//				"alma.obops.dam.shiftlog.domain"
		);
		factory.setDataSource(dataSource());
		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
		return new JpaTransactionManager(sessionFactory);
	}
}

