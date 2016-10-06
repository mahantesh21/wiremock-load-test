package com.foo;

import java.io.StringWriter;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.*;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.tools.ToolManager;

import com.github.tomakehurst.wiremock.common.FileSource;

import javax.inject.Singleton;

/**
 * Class is used in conjunction with wiremock either standalone or
 * library. Used for transforming the response body of a parameterized
 * velocity template.
 * @author yorka012
 *
 */
@Singleton
public class VelocityTransformer extends ResponseDefinitionTransformer {


    static VelocityEngine velocityEngine;
    static ToolManager toolManager;

    static {
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());

        velocityEngine.init();

        toolManager = new ToolManager();

        toolManager.setVelocityEngine(velocityEngine);

    }

    private static final String CLIENT_BY_ID = "contact-by-id-response.vm";

    Logger logger = Logger.getLogger(VelocityTransformer.class);

    /**
     * The Velocity context that will hold our request header
     * data.
     */
    private Context context;

    /**
     * The template
     */
    private FileSource fileSource;

    /* (non-Javadoc)
     * @see com.github.tomakehurst.wiremock.extension.Extension#name()
     */

    /* (non-Javadoc)
     * @see com.github.tomakehurst.wiremock.extension.ResponseTransformer#transform(com.github.tomakehurst.wiremock.http.Request, com.github.tomakehurst.wiremock.http.ResponseDefinition, com.github.tomakehurst.wiremock.common.FileSource)
     */
    public ResponseDefinition transform(Request request, ResponseDefinition response, FileSource files, Parameters parameters) {
        ResponseDefinition responseDefinition = null;
        if (response.specifiesBodyFile() && templateDeclared(response)) {

            context = toolManager.createContext();
            this.fileSource = files;

            final String bodyFileName = response.getBodyFileName();
            stuffContextWithVariables(request, bodyFileName);

            addBodyToContext(request.getBodyAsString());
            addHeadersToContext(request.getHeaders());

            logger.info(request.getAbsoluteUrl());

            final Template template1 = velocityEngine.getTemplate("__files/"+ bodyFileName);
             responseDefinition =

            ResponseDefinitionBuilder
                    .like(response).but()
                    .withBodyFile(null)
                    .withBody(transformResponse( template1).getByteBody())
                    .build();
        }
        return responseDefinition;
    }

    public void stuffContextWithVariables(Request request, String bodyFileName){


        Random r = new Random();
        int randomNumber = r.ints(1, 0, 11).findFirst().getAsInt();


        context.put("end", randomNumber);


        final String randomUUID = UUID.randomUUID().toString();
        context.put("id",UUID.nameUUIDFromBytes((randomUUID +"id").getBytes()));
        context.put("fname",UUID.nameUUIDFromBytes((randomUUID +"first").getBytes()));
        context.put("lname",UUID.nameUUIDFromBytes((randomUUID +"last").getBytes()));
        context.put("email",UUID.nameUUIDFromBytes((randomUUID +"email").getBytes()));
        context.put("phone",UUID.nameUUIDFromBytes((randomUUID +"phone").getBytes()));







    }


    /**
     * @param response
     * @return Boolean If the filesource is a template.
     */
    private Boolean templateDeclared(final ResponseDefinition response) {
        Pattern extension = Pattern.compile(".vm$");
        Matcher matcher = extension.matcher(response.getBodyFileName());
        return matcher.find();
    }

    /**
     * Adds the request header information to the Velocity context.
     * @param headers
     */
    private void addHeadersToContext(final HttpHeaders headers) {
        for (HttpHeader header : headers.all()) {
            final String rawKey = header.key();
            final String transformedKey = rawKey.replaceAll("-", "");
            context.put("requestHeader".concat(transformedKey), header.values()
                    .toString());
        }
    }

    /**
     * Adds the request body to the context if one exists.
     * @param body
     */
    private void addBodyToContext(final String body) {
        if (!body.isEmpty() && body != null) {
            context.put("requestBody", body);
        }
    }

    /**
     * Renders the velocity template.
     * @param response
     */
    private ResponseDefinition transformResponse(  Template template) {
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        final byte[] fileBytes = String.valueOf(writer.getBuffer()).getBytes();

        ResponseDefinition responseDefinition = new ResponseDefinition(200, fileBytes);

        return responseDefinition;
    }



    public String getName() {
        return "velocity-transformer";
    }
}