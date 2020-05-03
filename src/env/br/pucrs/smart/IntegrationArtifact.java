// CArtAgO artifact code for project helloworld_from_jason

package br.pucrs.smart;

import java.util.logging.Logger;

import br.pucrs.smart.integration.MyActionsApp;
import br.pucrs.smart.interfaces.IAgent;
import br.pucrs.smart.models.ResponseDialogflow;
import cartago.*;
import jason.asSyntax.Literal;

public class IntegrationArtifact extends Artifact implements IAgent {
	private Logger logger = Logger.getLogger("ArtefatoIntegracao." + IntegrationArtifact.class.getName());

	MyActionsApp dialog;
	
	void init() {
		RestImpl.setListener(this);
		defineObsProperty("teste", Literal.parseLiteral("Call Jason Agent"));
	}
	
	@INTERNAL_OPERATION
	void defineObsProperty() {
		defineObsProperty("request", Literal.parseLiteral("callJasonAgent"));
	}
	
//	@INTERNAL_OPERATION
//	ObsProperty getObsProperty() {
//		return getObsProperty("response");
//	}
	
	@Override
	public ResponseDialogflow processarIntencao(String sessionId, String request) {
		
		ResponseDialogflow response = new ResponseDialogflow();
		System.out.println("recebido evento: " + sessionId);
		System.out.println("Intenção: " + request);
		if (request != null) {
			switch (request) {
			case "Call Jason Agent":
				execInternalOp("defineObsProperty");
				break;				
			default:
				defineObsProperty("request", Literal.parseLiteral("default"));
				break;
			}
			System.out.println("Definindo propriedade observável");
		} else {
//			execInternalOp("teste", sessionId, request);
			System.out.println("Não foi possível definir propriedade observável");
			response.setFulfillmentText("Intenção não reconhecida");
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		ObsProperty prop = getObsProperty("response");
		if (prop != null) {
			System.out.println("Prop " + prop.stringValue());
			response.setFulfillmentText(prop.stringValue());
		} else {
			System.out.println("Sem prop");
			response.setFulfillmentText("Sem resposta do agente");
		}
		return response;
	}

}
