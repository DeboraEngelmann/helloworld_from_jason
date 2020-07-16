package br.pucrs.smart.interfaces;

import java.util.HashMap;
import java.util.List;

import br.pucrs.smart.models.OutputContexts;
import br.pucrs.smart.models.ResponseDialogflow;

public interface IAgent {
	public ResponseDialogflow processarIntencao(String responseId, String intentName, HashMap<String, Object> parameters, List<OutputContexts> outputContexts, String session);
}

