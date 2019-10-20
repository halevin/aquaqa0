/*******************************************************************************
 * ALMA - Atacama Large Millimeter Array
 * Copyright (c) ESO - European Southern Observatory, 2014
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

package alma.obops.aqua.qa0;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import alma.asdm.dao.AsdmDao;
import alma.asdm.dao.AsdmJdbcDao;
import alma.asdm.service.AsdmEphemerisService;
import alma.asdm.service.AsdmEphemerisServiceImpl;
import alma.asdm.service.AsdmService;
import alma.asdm.service.AsdmServiceImpl;
import alma.obops.dam.apdm.dao.SchedBlockJdbcDao;

@SpringBootApplication
@EntityScan( basePackages = {"alma.obops.aqua.qa0", "alma.obops.boot"} )
@EnableJpaRepositories(basePackages= {"alma.obops.aqua.qa0", "alma.obops.boot"})
@EnableCaching
@EnableTransactionManagement
public class Application {

    public static final String UNIT_TEST_PROFILE = "test";
    public static final String PRODUCTION_PROFILE = "production";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;
    
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("protrack");
    }	

    @Bean
    public AsdmDao asdmDao() {
        return new AsdmJdbcDao();
    }	
    
    @Bean
    public AsdmService asdmService() {
        return new AsdmServiceImpl();
    }

    @Bean
    public AsdmEphemerisService asdmEphemerisService() {
        return new AsdmEphemerisServiceImpl();
    }
    
    @Bean
    public SchedBlockJdbcDao schedBlockJdbcDao() {
    	SchedBlockJdbcDao dao = new SchedBlockJdbcDao();
        dao.setDataSource(dataSource);
        dao.setJdbcTemplate(jdbcTemplate);
        return dao;
    }
    
    @Bean
    public alma.obops.boot.security.AlmaUserDetailsService almaUserDetailsService() {
    	return new alma.obops.boot.security.AlmaUserDetailsService();
    }

    public static void main(String[] args) {
        String acsDataEnv = System.getenv("ACSDATA");
        if(acsDataEnv == null) {
            throw new RuntimeException("The environment variable ACSDATA is not set!");
        }
        System.setProperty("ACS.data", acsDataEnv);
        SpringApplication.run(Application.class, args);
    }
}
