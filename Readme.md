# Camel Quarkus CXF SOAP and WS-Security

## Introduction
This is an example of the usage of the new Camel v3 CXF component, aimed at supporting legacy SOAP web services.  
The example application is Quarkus-based; thus it leverages the camel-quarkus-cxf-soap extension.
The Camel routes themselves are written in Camel Java DSL language.

## Architecture
The configuration for a SOAP service differs from the configuration of a SOAP client.  
The common use case of exposing a SOAP service behind a REST endpoint has been chosen in order to demonstrate the 2 sides at the same time.  
- the "soap-mock" subdirectory is a mocked implementation of a SOAP webservice whose public definition has been sourced from here:
https://www.herongyang.com/1000230_Live_Examples_of_Web_Services.html
- the "rest2soap" subdirectory is a REST endpoint, designed with the Camel REST DSL, and that calls the previous SOAP mock. 

![architecture](images/architecture.png?raw=true)

## Client and server configuration overview
The server side (soap-mock) has a Camel route that starts with a "from(cxf://)" URI endpoint.

The URI endpoint references the name of a CxfEndpoint Java bean that configures the CXF runtime.  
While the URI endpoint is used to configure the serviceclass that points to the generated class linked to the portType of the WSDL, the CxfEndpoint is used to configure the service name, endpoint name and exposed URL.  The exposed URL can contain a root prefix, coming from a configuration in the application.properties file.  

The client side (rest2soap) has a Camel route that contains a "to(cxf://)" URI endpoint.  In this case, the URI does not require the CxfEndpoint bean and can directly embed the URL of the target SOAP service.


## Applying WS-Security
The configuration of WS-Security still leverages the notion of Interceptors in the CXF model.
For simplicity, the example applies the UsernameToken (without password) WS-Security policy.  The same principle applies for other WS-Security policies as well as other SOAp features, such as WS-Addressing.

To enable WS-Security on the server side, switch the 'from()' line of the Camel route with the one that contains the "cxfConfigurer" option in the source code.  The cxfConfigurer bean is the entry point to configure the related CXF inbound interceptors.

To enable WS-Security on the client side, switch the 'to()' line of the Camel route with the one that contains the "cxfConfigurer" option.  The cxfConfigurer bean is here also the entry point to configure the related CXF outbound interceptors.


## Using the example
1) Clone this repository  
2) cd soap-mock  
mvn generate-sources  
mvn package  
mvn quarkus:dev  

2) cd rest2soap  
mvn generate-sources  
mvn package  
mvn quarkus:dev  

3) Test the SOAP webservice is isolation with SOAPUI  
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ser="https://www.herongyang.com/Service/">  
<<soap:Header/>>  
   <<soap:Body>>  
      <ser:RegistrationRequest date="now" event="123">
         <Guest>John</Guest>
      </ser:RegistrationRequest>
   </soap:Body>  
</soap:Envelope>


4) Test the use case by hitting the in-front REST API  
curl -XPOST http://localhost:8050/api/registration  
 -H "Content-Type: application/json"  
 -d '{ "event" : "123" , "guest" : "John" }'  


