package br.pucrs.smart.interfaces;

import br.pucrs.smart.models.ResponseDialogflow;

public interface IAgent {
	public ResponseDialogflow processarIntencao(String sessionId, String request);
}

