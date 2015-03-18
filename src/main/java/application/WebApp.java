package application;

import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;

public class WebApp
implements WebApplicationInitializer{

@Override
public void onStartup(ServletContext servletContext)
        throws ServletException {
	System.out.println("system start");
    // can be set runtime before Spring instantiates any beans
    // TimeZone.setDefault(TimeZone.getTimeZone("GMT+00:00"));
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

    // cannot override encoding in Spring at runtime as some strings have already been read
    // however, we can assert and ensure right values are loaded here

    // locale
    // set and verify language

}

}