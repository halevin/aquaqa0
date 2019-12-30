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

package alma.obops.aqua.qa0;

import alma.obops.boot.ObopsBootUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {
        "alma.obops.aqua.qa0",
        "alma.obops.aqua.service",
        "alma.obops.aqua.dao",
        "alma.asdm.service",
        "alma.asdm.dao",
        "alma.obops.dam.apdm.dao",
        "alma.obops.dam.apdm.service",
        "alma.lifecycle",
        "alma.obops.dam.userreg",
        "alma.obops.ocd.domain"})
@EntityScan( basePackages = {"alma.obops.aqua.qa0", "alma.obops.aqua.domain", "alma.asdm.domain", "alma.obops.boot", "alma.obops.ocd.domain"} )
@EnableJpaRepositories(basePackages= {"alma.obops.aqua.qa0", "alma.obops.boot", "alma.obops.ocd.domain"})
@EnableCaching
@EnableTransactionManagement
public class Application {

    public static final String UNIT_TEST_PROFILE = "test";
    public static final String PRODUCTION_PROFILE = "production";


    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("dratool");
    }

    public static void main(String[] args) {
        ObopsBootUtils.initACSDataSystemProperty();
        SpringApplication.run(Application.class, args);
    }

}

