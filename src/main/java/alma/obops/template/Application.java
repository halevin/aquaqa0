/*******************************************************************************
 * ALMA - Atacama Large Millimeter Array
 * Copyright (c) ESO - European Southern Observatory, 2019
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

package alma.obops.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication(scanBasePackages = {"alma.obops.template", "alma.icd.adapt"})
@EnableScheduling
@EnableTransactionManagement
@EntityScan( basePackages = {"alma.obops.template", "alma.obops.boot"} )
@EnableJpaRepositories(basePackages= {"alma.obops.template", "alma.obops.boot"})
@EnableCaching
public class Application
{
    public static final String PRODUCTION_PROFILE = "production";
    public static final String UNIT_TEST_PROFILE = "unit-test";
    public static final String RABBITMQ  	= "rabbitmq";

    public static void main(String[] args) {
        String acsDataEnv = System.getenv("ACSDATA");
        if(acsDataEnv == null) {
            throw new RuntimeException("The environment variable ACSDATA is not set!");
        }
        System.setProperty("ACS.data", acsDataEnv);
        SpringApplication.run(Application.class, args);
    }
}


