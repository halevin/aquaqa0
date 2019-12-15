/*
 * ALMA - Atacama Large Millimiter Array (c) European Southern Observatory, 2017
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alma.obops.aqua.qa0.configuration;

import javax.sql.DataSource;
import alma.obops.utils.config.SourceCatDbConfig;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import java.beans.PropertyVetoException;

/**
 * Configure the application's data source according to ALMA's conventions, e.g.
 * storing the data source parameters in
 * <em>$ACSDATA/config/archiveConfig.properties</em>
 * 
 * @author amchavan
 */

@Configuration
@PropertySource("file:${ACSDATA}/config/archiveConfig.properties")
public class SourceCatalogueDataSourceConfiguration {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	Environment env;

	@Bean(name="sourceCatalogueDataSource")
	public ComboPooledDataSource sourceCatalogueDataSource() {

		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		try {
			dataSource.setDriverClass(sourceCatDbConfig().getDriver());
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		dataSource.setPassword(sourceCatDbConfig().getPassword());
		dataSource.setUser(sourceCatDbConfig().getUsername());
		dataSource.setJdbcUrl(sourceCatDbConfig().getConnectionUrl());
		dataSource.setMinPoolSize(1);
		dataSource.setMaxPoolSize(10);
		dataSource.setMaxConnectionAge(1800);
		dataSource.setMaxIdleTimeExcessConnections(120);
		dataSource.setMaxStatements(0);
		dataSource.setMaxStatementsPerConnection(0);
		dataSource.setNumHelperThreads(6);
		dataSource.setTestConnectionOnCheckout(true);
		if (logger.isInfoEnabled()) {
			logger.info("Source catalogue Data Source created. " + dataSource.toString(true));
		}

		return dataSource;
	}

	@Bean
	public SourceCatDbConfig sourceCatDbConfig() {
		return new SourceCatDbConfig();
	}

	@Bean
	public JdbcTemplate sourceCatalogueJdbcTemplate(@Qualifier("sourceCatalogueDataSource")DataSource sourceCatalogueDataSource){
		return new JdbcTemplate(sourceCatalogueDataSource);
	}

}

