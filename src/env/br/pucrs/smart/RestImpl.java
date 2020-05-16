/*
	Arquivo original: https://github.com/jacamo-lang/jacamo-rest/blob/master/src/main/java/jacamo/rest/RestImpl.java
	Alterado por: Débora Engelmann
	03 de Maio de 2020
*/

package br.pucrs.smart;

import javax.inject.Singleton;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.internal.inject.AbstractBinder;

import com.google.gson.Gson;

import br.pucrs.smart.interfaces.IAgent;
import br.pucrs.smart.models.RequestDialogflow;
import br.pucrs.smart.models.ResponseDialogflow;


@Singleton
@Path("/")
public class RestImpl extends AbstractBinder {
	 static IAgent mas = null;
	 private Gson gson = new Gson();
	 
	 public static void setListener(IAgent agent) {
		 mas = agent;
		 System.out.println("Passou aqui");
	 }
	 
	@Override
	protected void configure() {
		bind(new RestImpl()).to(RestImpl.class);
	}
	
	@Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewAgent(String request) {
        try {
        	RequestDialogflow requestDialogflow = gson.fromJson(request, RequestDialogflow.class);
        	System.out.println("Agente comunicado: " +  gson.toJson(requestDialogflow)); 
        	if (mas != null) {
        		ResponseDialogflow ResponseDialogflow = mas.processarIntencao(requestDialogflow.getResponseId(),
        																	  requestDialogflow.getQueryResult().getIntent().getDisplayName(),
        																	  requestDialogflow.getQueryResult().getParameters(),
        																	  requestDialogflow.getQueryResult().getOutputContexts());
                return Response.ok(gson.toJson(ResponseDialogflow)).build();
        	} else {
        		ResponseDialogflow ResponseDialogflow = new ResponseDialogflow();   
            	ResponseDialogflow.setFulfillmentText("Desculpe, Não foi possível encontrar o agente Jason");
            return Response.ok(gson.toJson(ResponseDialogflow)).build();
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(500).build();
    }

}
