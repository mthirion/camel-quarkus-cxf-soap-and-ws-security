package com.redhat.appfoundation.camel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.datatype.DatatypeFactory;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import https.www_herongyang_com.service.RegistrationRequest;
import https.www_herongyang_com.service.RegistrationResponse;


@ApplicationScoped
public class CamelRoutes extends RouteBuilder {


    @Inject
    @ConfigProperty(name = "app.webservice.soap.url")
    private String soapurl;
        
    @Override
    public void configure() {
        JaxbDataFormat df = new JaxbDataFormat();
        df.setContextPath("https.www_herongyang_com.service");
   
        restConfiguration()
            .bindingMode(RestBindingMode.off)
            .component("servlet");

        rest("/")
            .post("/registration")
                .id("registration")
                .to("direct:register");


        from("direct:register")
        .routeId("rest2soap")
        .log("REST 2 SOAP : received Json request")
        .log(body().toString())

        .unmarshal(new JacksonDataFormat(com.redhat.appfoundation.camel.Registration.class))

        .process(e -> {
            Message m = e.getIn();
            Registration r = (Registration) m.getBody();

            RegistrationRequest soaprequest = new RegistrationRequest();
            soaprequest.setEvent(r.getEvent());
            soaprequest.getContent().add(r.getGuest());
            soaprequest.setDate(DatatypeFactory.newInstance().newXMLGregorianCalendar());
            
            m.setBody(soaprequest);
        })
        .convertBodyTo(RegistrationRequest.class)
        .setHeader(CxfConstants.OPERATION_NAME, constant("Registration"))
        .setHeader(CxfConstants.OPERATION_NAMESPACE, constant("https://www.herongyang.com/Service/"))
        .marshal().jaxb()
        .log("REST 2 SOAP : SOAP request payload")
        .log(body().toString())

        //.toD("cxf:" + soapurl + "?serviceClass=https.www_herongyang_com.service.RegistrationPortType&cxfConfigurer=#wssecurity&exchangePattern=InOut&dataFormat=PAYLOAD")
        .toD("cxf:" + soapurl + "?serviceClass=https.www_herongyang_com.service.RegistrationPortType&exchangePattern=InOut&dataFormat=PAYLOAD")

        .log("REST 2 SOAP : SOAP response")
        .log(body().toString())

        .unmarshal(df)

        .process(e -> {
            Message m = e.getIn();
            RegistrationResponse soapresponse = (RegistrationResponse) m.getBody();

            Confirmation c = new Confirmation();
            c.setEvent(soapresponse.getConfirmation().get(0).getEvent().toString());
            c.setGuest(soapresponse.getConfirmation().get(0).getGuest().toString());

            m.setBody(c);
        })
        .marshal(new JacksonDataFormat())
        .log("REST 2 SOAP : JSON response")
        .log(body().toString())
        ;
        
    }

}
