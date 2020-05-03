// CArtAgO artifact code for project helloworld_from_jason

package br.pucrs.smart;

import java.util.logging.Logger;

import br.pucrs.smart.interfaces.IAgent;
import br.pucrs.smart.models.ResponseDialogflow;
import cartago.*;
import jason.asSyntax.Literal;

public class IntegrationArtifact extends Artifact implements IAgent {
	private Logger logger = Logger.getLogger("ArtefatoIntegracao." + IntegrationArtifact.class.getName());
	String jasonResponse = null;

	void init() {
		RestImpl.setListener(this);
		defineObsProperty("teste", Literal.parseLiteral("Call Jason Agent"));
	}

	@INTERNAL_OPERATION
	void defineRequest(String obsProperty) {
		defineObsProperty("request", Literal.parseLiteral(obsProperty));
	}

	@OPERATION
	void reply(String response) {
		this.jasonResponse = response;
	}

	@Override
	public ResponseDialogflow processarIntencao(String sessionId, String request) {

		ResponseDialogflow response = new ResponseDialogflow();
		System.out.println("recebido evento: " + sessionId);
		System.out.println("Intenção: " + request);
		if (request != null) {
			switch (request) {
			case "Call Jason Agent":
				execInternalOp("defineRequest", "callJasonAgent");
				break;
			default:
				execInternalOp("defineRequest", "default");
				break;
			}
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

}
