// CArtAgO artifact code for project helloworld_from_jason

package br.pucrs.smart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import br.pucrs.smart.interfaces.IAgent;
import br.pucrs.smart.models.OutputContexts;
import br.pucrs.smart.models.ResponseDialogflow;
import cartago.*;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.ListTerm;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public class IntegrationArtifact extends Artifact implements IAgent {
	private Logger logger = Logger.getLogger("ArtefatoIntegracao." + IntegrationArtifact.class.getName());
	String jasonResponse = null;

	void init() {
		RestImpl.setListener(this);
	}

	@OPERATION
	void reply(String response) {
		this.jasonResponse = response;
	}

	@Override
	public ResponseDialogflow processarIntencao(String responseId, String intentName, HashMap<String, Object> parameters, List<OutputContexts> outputContexts) {

		ResponseDialogflow response = new ResponseDialogflow();
		if (intentName != null) {
			execInternalOp("createRequestBelief", responseId, intentName, parameters, outputContexts);
			System.out.println("Definindo propriedade observavel");
		} else {
			System.out.println("Não foi possível definir a propriedade observavel");
			response.setFulfillmentText("Intensão não reconhecida");
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
	
	// return a list of param(Key1, Value1)
	ListTerm createParamBelief(HashMap<String, Object> parameters) {
		Collection<Term> terms = new LinkedList<Term>();
		for(Map.Entry<String, Object> entry : parameters.entrySet()) {
		    String key = entry.getKey();
		    Object value = entry.getValue();
		    Literal l = ASSyntax.createLiteral("param", ASSyntax.createString(key));
		    if (value instanceof String) {
		    	l.addTerm(ASSyntax.createString(value));
		    	terms.add(l);
		    } else if (value instanceof ArrayList){
		    	ArrayList<String> valueArr = (ArrayList<String>) value;
		    	Collection<Term> valuesInTerms = new LinkedList<Term>();
		    	for (String element : valueArr) {
		    		valuesInTerms.add(ASSyntax.createString(element));
				}
		    	l.addTerm(ASSyntax.createList(valuesInTerms));
		    	terms.add(l);
		    } else if (value instanceof Integer){
		    	Integer valueInt = (Integer) value;
		    	l.addTerm(ASSyntax.createNumber(valueInt));
		    	terms.add(l);
		    } else if (value instanceof Double){
		    	Double valueDoub = (Double) value;
		    	l.addTerm(ASSyntax.createNumber(valueDoub));
		    	terms.add(l);
		    } else {
		    	
		    	System.out.println("Valor do parâmetro " + key + " informados em formato desconhecido" + value.getClass());
		    }
		}
		return ASSyntax.createList(terms);
	}
	
	
	//return a list of context(Name, LifespanCount, [param(Key2, Value2), param(Key3, Value3)])
	ListTerm createContextBelief(List<OutputContexts> outputContexts) {
		Collection<Term> terms = new LinkedList<Term>();
		for (OutputContexts outputContext : outputContexts) {
			Literal l = ASSyntax.createLiteral("context", ASSyntax.createString(getContextName(outputContext.getName())));
			l.addTerm(ASSyntax.createString(outputContext.getLifespanCount()));
			ListTerm parametersList = createParamBelief(outputContext.getParameters());
			l.addTerm(parametersList);
			terms.add(l);
		}
		
		return ASSyntax.createList(terms);			
	}
	
	String getContextName(String context) {
            String contextName = context.substring(context.indexOf("/contexts/")+10, context.length());
            return contextName;
    }
	
	//add to belief base a request(ResponseId, IntentName, [param(Key, Value), param(Key1, Value1)], [context(Name, LifespanCount, [param(Key2, Value2), param(Key3, Value3)])])
	@INTERNAL_OPERATION
	void createRequestBelief(String responseId, String intentName, HashMap<String, Object> parameters, List<OutputContexts> outputContexts) {
		ListTerm contextsList = null;
		if (outputContexts != null) {
			contextsList = createContextBelief(outputContexts);
		}
		defineObsProperty("request", ASSyntax.createString(responseId), ASSyntax.createString(intentName), createParamBelief(parameters), contextsList);
	}
}
