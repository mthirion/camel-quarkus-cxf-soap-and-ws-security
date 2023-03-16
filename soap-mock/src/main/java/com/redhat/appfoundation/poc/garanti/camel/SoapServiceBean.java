package com.redhat.appfoundation.poc.garanti.camel;

import javax.enterprise.context.ApplicationScoped;
import org.apache.camel.component.cxf.jaxws.*;
import javax.inject.Named;
import javax.xml.namespace.QName;

@Named("registration")
@ApplicationScoped
public class SoapServiceBean extends CxfEndpoint {


    public SoapServiceBean() {
        super();

        try {
            this.configure();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
    }

    private void configure() throws ClassNotFoundException {

        QName endpointQname = new QName("https://www.herongyang.com/Service/", "registrationPort");
        this.setEndpointNameAsQName(endpointQname);
        QName serviceQname = new QName("https://www.herongyang.com/Service/", "registrationService");
        this.setServiceNameAsQName(serviceQname);
        
       this.setAddress("http://localhost:8055/soap/registration");

       
       
    }


}
