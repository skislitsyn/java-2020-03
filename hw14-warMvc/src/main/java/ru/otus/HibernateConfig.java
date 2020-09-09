package ru.otus;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ru.otus.core.model.Address;
import ru.otus.core.model.Phone;
import ru.otus.core.model.User;
import ru.otus.hibernate.HibernateUtils;

@Configuration
@ComponentScan
public class HibernateConfig {

    @Bean
    public SessionFactory sessionFactoryResolver() {
	SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class,
		Address.class, Phone.class);
	return sessionFactory;
    }
}
