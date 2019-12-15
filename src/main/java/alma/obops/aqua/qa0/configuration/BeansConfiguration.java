package alma.obops.aqua.qa0.configuration;

import alma.obops.aqua.dao.ExecBlockDao;
import alma.obops.aqua.dao.ExecBlockHibernateDao;
import alma.obops.aqua.dao.ObsUnitSetHibernateDao;
import alma.obops.aqua.dao.ShiftlogReplicator;
import alma.obops.aqua.dao.ShiftlogReplicatorJdbcImpl;
import alma.obops.dam.CrudDao;
import alma.obops.dam.CrudDaoHibernateImpl;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BeansConfiguration {

    @Autowired
    SessionFactory sessionFactory;

    @Bean
    public ObsUnitSetHibernateDao obsUnitSetHibernateDao(){
        ObsUnitSetHibernateDao obsUnitSetHibernateDao = new ObsUnitSetHibernateDao();
        obsUnitSetHibernateDao.setSessionFactory(sessionFactory);
        return obsUnitSetHibernateDao;
    }

    @Bean
    public ExecBlockDao execBlockDao(){
        ExecBlockHibernateDao execBlockHibernateDao = new ExecBlockHibernateDao();
        execBlockHibernateDao.setSessionFactory(sessionFactory);
        return execBlockHibernateDao;
    }

    @Bean
    public ShiftlogReplicator shiftlogReplicator(){
        ShiftlogReplicatorJdbcImpl shiftlogReplicatorJdbc = new ShiftlogReplicatorJdbcImpl();
        return shiftlogReplicatorJdbc;
    }

    @Bean
    public CrudDao crudDao(){
        return new CrudDaoHibernateImpl();
    }

}
