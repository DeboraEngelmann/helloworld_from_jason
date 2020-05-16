package br.pucrs.smart.interfaces;

import java.util.HashMap;
import java.util.List;

import br.pucrs.smart.models.OutputContexts;
import br.pucrs.smart.models.ResponseDialogflow;

public interface IAgent {
	public ResponseDialogflow processarIntencao(String sessionId, String request, HashMap<String, String> parameters, List<OutputContexts> outputContexts);
}

