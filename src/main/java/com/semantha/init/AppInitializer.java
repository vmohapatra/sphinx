package com.semantha.init;

import com.semantha.config.WebMvcConfig;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Properties;

public class AppInitializer implements WebApplicationInitializer {

    private static final String CONFIG_LOCATION = "com.semantha.config";
    private static final String MAPPING_URL = "/*";
    private static final String SPRING_PROPERTIES_FILE_NAME = "spring.properties";
    private static final String ACTIVE_PROFILE_PROPERTY_NAME = "spring.profiles.active";
    private static final String DEFAULT_PROFILE = "dev";
    private static final PropertiesLoader propertiesLoader = new PropertiesLoader();

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        WebApplicationContext context = getContext(servletContext);
        servletContext.addListener(new ContextLoaderListener(context));
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping(MAPPING_URL);
    }

    private WebApplicationContext getContext(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        /* Get the webmvc config*/
        context.register(WebMvcConfig.class);
        context.setServletContext(servletContext);

        /*get from config files*/
        context.setConfigLocation(CONFIG_LOCATION);
        Properties prop = propertiesLoader.load(SPRING_PROPERTIES_FILE_NAME);

        context.getEnvironment().setActiveProfiles(prop.getProperty(ACTIVE_PROFILE_PROPERTY_NAME, DEFAULT_PROFILE));
        return context;
    }

}
