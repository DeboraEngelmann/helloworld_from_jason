package br.pucrs.smart.models;

import java.util.ArrayList;
import java.util.List;

public class ResponseDialogflow {
	
public ResponseDialogflow() {
	this.outputContexts = new ArrayList<String>();
}
	private String fulfillmentText;
	
	private List<String> outputContexts;

	public String getFulfillmentText() {
		return fulfillmentText;
	}

	public void setFulfillmentText(String fulfillmentText) {
		this.fulfillmentText = fulfillmentText;
	}

	public List<String> getOutputContexts() {
		return outputContexts;
	}

	public void setOutputContexts(List<String> outputContexts) {
		this.outputContexts = outputContexts;
	}

	public void addOutputContexts(String ouString) {
		this.outputContexts.add(ouString);
	}
	
}
