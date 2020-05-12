/*
	Arquivo original: https://github.com/jacamo-lang/jacamo-rest/blob/master/src/main/java/jacamo/rest/RestAppConfig.java
	Alterado por: Débora Engelmann
	03 de Maio de 2020
*/

package br.pucrs.smart;

import java.util.HashMap;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.message.DeflateEncoder;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.EncodingFilter;

@ApplicationPath("/")
public class RestAppConfig extends ResourceConfig {
    public RestAppConfig() {
        // Registering resource classes
        registerClasses(
                RestImpl.class);
        
        // gzip compression
        registerClasses(EncodingFilter.class, GZipEncoder.class, DeflateEncoder.class);
        
        addProperties(new HashMap<String,Object>() {
            private static final long serialVersionUID = 1L;

            { put("jersey.config.server.provider.classnames", "org.glassfish.jersey.media.multipart.MultiPartFeature"); }
        } );        
    }
}
