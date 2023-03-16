package com.redhat.appfoundation.poc.garanti.camel;

import java.util.*;

import javax.enterprise.context.ApplicationScoped;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.AbstractWSDLBasedEndpointFactory;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.common.ConfigurationConstants;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.camel.component.cxf.jaxws.CxfConfigurer;

@Named("wssecurity")
@ApplicationScoped
public class WSSecBean implements CxfConfigurer {

    @Inject
    @ConfigProperty (name = "app.webservice.soap.wssecurity.user")
    private String user;

    @Inject
    @ConfigProperty (name = "app.webservice.soap.wssecurity.mustunderstand")
    private String mustunderstand;    

    public WSSecBean() {
        super();

    }


    @Override
    public void configure(AbstractWSDLBasedEndpointFactory factoryBean) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'configure'");
    }

    @Override
    public void configureClient(Client client) {
        // TODO Auto-generated method stub

        Map<String,Object> wsproperties = new HashMap<String, Object>();
        wsproperties.put(ConfigurationConstants.ACTION, ConfigurationConstants.USERNAME_TOKEN_NO_PASSWORD);
        //wsproperties.put(ConfigurationConstants.ALLOW_USERNAMETOKEN_NOPASSWORD, "true");
        wsproperties.put(ConfigurationConstants.USER, user);
        //wsproperties.put(ConfigurationConstants.PASSWORD_TYPE, "PasswordNone");
        wsproperties.put(ConfigurationConstants.MUST_UNDERSTAND, mustunderstand);
        WSS4JOutInterceptor wssecurity = new WSS4JOutInterceptor(wsproperties);  

        client.getOutInterceptors().add(wssecurity);
        //throw new UnsupportedOperationException("Unimplemented method 'configureClient'");
    }

    @Override
    public void configureServer(Server server) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'configureServer'");
    }
}
