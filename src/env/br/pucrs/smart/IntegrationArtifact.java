// CArtAgO artifact code for project helloworld_from_jason

package br.pucrs.smart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gson.JsonObject;

import br.pucrs.smart.interfaces.IAgent;
import br.pucrs.smart.models.OutputContexts;
import br.pucrs.smart.models.ResponseDialogflow;
import cartago.*;
import jason.asSyntax.Literal;

public class IntegrationArtifact extends Artifact implements IAgent {
	private Logger logger = Logger.getLogger("ArtefatoIntegracao." + IntegrationArtifact.class.getName());
	String jasonResponse = null;

	void init() {
		RestImpl.setListener(this);
	}

	@INTERNAL_OPERATION
	void defineRequest(String obsProperty) {
		defineObsProperty("request", obsProperty);
	}

	@OPERATION
	void reply(String response) {
		this.jasonResponse = response;
	}

	@Override
	public ResponseDialogflow processarIntencao(String sessionId, String request, HashMap<String, String> parameters, List<OutputContexts> outputContexts) {

		ResponseDialogflow response = new ResponseDialogflow();
		System.out.println("recebido evento: " + sessionId);
		System.out.println("Intenção: " + request);
		if (request != null) {
			
			for(Map.Entry<String, String> entry : parameters.entrySet()) {
			    String key = entry.getKey();
			    String value = entry.getValue();

				System.out.println("parameters: " + key + " : " + value);

			}
			for (OutputContexts outputContext : outputContexts) {
				System.out.println("OutputContexts name: " + outputContext.getName());
				System.out.println("OutputContexts lifespanCount: " + outputContext.getLifespanCount());
				System.out.println("OutputContexts parameters: ");
				for(Map.Entry<String, String> entry : parameters.entrySet()) {
				    String key = entry.getKey();
				    String value = entry.getValue();
					System.out.println(key + " : " + value);
				}
				
			}
			
			
			
			
			
			
			execInternalOp("defineRequest", request);
			System.out.println("Definindo propriedade observável");
		} else {
			System.out.println("Não foi possível definir a propriedade observável");
			response.setFulfillmentText("Intenção não reconhecida");
		}
		int i = 0;
		while (this.jasonResponse == null && i <= 200) {
			try {
				Thread.sleep(10);
				i++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (this.jasonResponse != null) {
			System.out.println("jasonResponse " + this.jasonResponse);
			response.setFulfillmentText(this.jasonResponse);
			this.jasonResponse = null;
		} else {
			System.out.println("Sem jasonResponse");
			response.setFulfillmentText("Sem resposta do agente");
		}
		return response;
	}
	

//	public void simStartMessage(JsonObject starMessage) {
//		List<String> filter = Arrays.asList("id", "map");
//		JsonObject config = starMessage.get("agent_percepts").getAsJsonObject();
//		JsonObject map = starMessage.get("map_percepts").getAsJsonObject();
//		// we need to ensure the token will be an atom
//		String token = config.get("token").getAsString();
//		config.remove("token");
//		config.addProperty("token", "\'"+token+"\'");
//		filter.forEach(f -> map.remove(f));
//
//		try {
//			List<Percept> p = new ArrayList<Percept>();
//			p.addAll(Translator.entryToPercept(config.entrySet()));
//			p.addAll(Translator.entryToPercept(map.entrySet()));
//
//			execInternalOp("updatePerceptions", null, p, null);
//		} catch (ParseException e) {
//			logger.info("failed to parse initial percetions: " + e.getMessage());
//		}
//	}
//	
//	@INTERNAL_OPERATION
//	private void updatePerceptions(Collection<Percept> previousPercepts, Collection<Percept> percepts,
//			List<String> orderPercept) {
//		if (previousPercepts == null) {// should add all new perceptions
//			for (Percept percept : percepts) {
//				try {
//					Literal literal = Translator.perceptToLiteral(percept);
//					defineObsProperty(literal.getFunctor(), (Object[]) literal.getTermsArray());
//				} catch (JasonException e) {
//					logger.info("Failed to parse percept to literal: " + e.getMessage());
//				}
//			}
//		}
//	}

}
