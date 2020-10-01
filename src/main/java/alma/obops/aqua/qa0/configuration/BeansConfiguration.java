package alma.obops.aqua.qa0.configuration;

import alma.obops.aqua.qa0.service.AquaUserSession;
import alma.obops.dam.CrudDao;
import alma.obops.dam.CrudDaoHibernateImpl;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
public class BeansConfiguration {

    @Autowired
    SessionFactory sessionFactory;

    @Bean
    public CrudDao crudDao(){
        return new CrudDaoHibernateImpl();
    }

    @Bean
    @SessionScope
    public AquaUserSession aquaUserSession(){
        return new AquaUserSession();
    }

}
