package com.redhat.appfoundation.camel.test;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.ExpressionBuilder;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.apache.commons.io.IOUtils;
import org.apache.camel.converter.stream.InputStreamCache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class CamelRouteTest extends CamelQuarkusTestSupport {

    

    @Test
    //@Disabled
    public void testCamelRoute() throws Exception {

        String jsonrequest = IOUtils.toString(this.getClass().getResourceAsStream("/rest-request.json"),"UTF-8");
        String jsonresponse = IOUtils.toString(this.getClass().getResourceAsStream("/rest-response.json"),"UTF-8");
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/soap-mock-response.xml"),"UTF-8");


        AdviceWith.adviceWith(context, "rest2soap", a -> {
      
                a.replaceFromWith("direct:rest2soap");
                a.interceptSendToEndpoint("cxf:*").skipSendToOriginalEndpoint().setBody(ExpressionBuilder.simpleExpression(xml));
        });

        Object o = template.requestBodyAndHeader("direct:rest2soap", jsonrequest , "CamelHttpMethod", "POST");

        String s = new String( ((InputStreamCache)o).readAllBytes() );

        s = s.replaceAll("\n","").replaceAll("\t", "").replaceAll(" ", "");
        jsonresponse = jsonresponse.replaceAll("\n","").replaceAll("\t", "").replaceAll(" ", "");
        assertEquals(s, jsonresponse, "The response does not match");

    }
}
