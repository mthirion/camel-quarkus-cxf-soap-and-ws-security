package com.redhat.appfoundation.camel.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.ExpressionBuilder;
import org.apache.camel.converter.stream.InputStreamCache;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class CamelRouteTest extends CamelQuarkusTestSupport {

    @Test
    public void testCamelRoute() throws Exception {


        String soaprequest = IOUtils.toString(this.getClass().getResourceAsStream("/soap-request.xml"),"UTF-8");
        String soapresponse = IOUtils.toString(this.getClass().getResourceAsStream("/soap-response.xml"),"UTF-8");


        AdviceWith.adviceWith(context, "soapmock-route", a -> {
      
                a.replaceFromWith("direct:soapinit");
                a.interceptSendToEndpoint("cxf:*").skipSendToOriginalEndpoint().setBody(ExpressionBuilder.simpleExpression(soaprequest));
        });

        Object o = template.requestBodyAndHeader("direct:soapinit", soapresponse , "CamelHttpMethod", "POST");
        String s = new String( ((InputStreamCache)o).readAllBytes() );

        s = s.replaceAll("\n","").replaceAll("\t", "").replaceAll(" ", "");
        soapresponse = soapresponse.replaceAll("\n","").replaceAll("\t", "").replaceAll(" ", "");
        assertEquals(s, soapresponse, "The response does not match");

    }
}
