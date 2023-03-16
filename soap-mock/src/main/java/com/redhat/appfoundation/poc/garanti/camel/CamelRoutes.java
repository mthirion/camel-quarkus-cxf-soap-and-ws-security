package com.redhat.appfoundation.poc.garanti.camel;


import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import https.www_herongyang_com.service.RegistrationResponse;
import https.www_herongyang_com.service.RegistrationResponse.Confirmation;

@ApplicationScoped
public class CamelRoutes extends RouteBuilder {

    @Override
    public void configure() {

        //from("cxf:registration?serviceClass=https.www_herongyang_com.service.RegistrationPortType&cxfConfigurer=#wssecurity&dataFormat=PAYLOAD")
        from("cxf:registration?serviceClass=https.www_herongyang_com.service.RegistrationPortType&dataFormat=PAYLOAD")
        .routeId("soapmock-route")
        .log("CXF Mock : Received request ")
        .log(LoggingLevel.INFO, body().toString())
        .process(e -> {
            RegistrationResponse r= new RegistrationResponse();
            Confirmation c = new Confirmation();
            c.setGuest("Jeremy");
            c.setEvent("456");
            r.getConfirmation().add(c);
            e.getMessage().setBody(r);
        })
        .log("CXF Mock : Response successfully created")
        .convertBodyTo(RegistrationResponse.class)
	    .marshal().jaxb();

    }

}
