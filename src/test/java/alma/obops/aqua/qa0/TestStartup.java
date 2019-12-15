/*******************************************************************************
 * ALMA - Atacama Large Millimiter Array (c) European Southern Observatory, 2015
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
 *******************************************************************************/

package alma.obops.aqua.qa0;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import alma.obops.aqua.qa0.Application;

/**
 * This test is useful during Spring/Hibernate/Springboot upgrades to confirm that the application can at least still start. 
 * 
 * @author rkurowsk
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Application.UNIT_TEST_PROFILE)
@SpringBootTest
@TestPropertySource("/application-test.properties")
public class TestStartup {

	@Test
	public void testSpringBootStartup() throws Exception {
		System.out.println(">>>> Started application!");

	}
}