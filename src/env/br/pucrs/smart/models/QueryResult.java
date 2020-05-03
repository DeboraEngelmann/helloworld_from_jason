package br.pucrs.smart.models;

import java.util.ArrayList;
import java.util.HashMap;

public class QueryResult {
	 private String queryText;
	 private HashMap<String, String> parameters;
	 private boolean allRequiredParamsPresent;
	 private String fulfillmentText;
	 ArrayList<Object> fulfillmentMessages = new ArrayList<Object> ();
	 Intent intent;
	 private float intentDetectionConfidence;
	 DiagnosticInfo diagnosticInfo;
	 private String languageCode;


	 // Getter Methods 

	 public String getQueryText() {
	  return queryText;
	 }

	 

	 public boolean getAllRequiredParamsPresent() {
	  return allRequiredParamsPresent;
	 }

	 public String getFulfillmentText() {
	  return fulfillmentText;
	 }

	 public Intent getIntent() {
	  return intent;
	 }

	 public float getIntentDetectionConfidence() {
	  return intentDetectionConfidence;
	 }

	 public DiagnosticInfo getDiagnosticInfo() {
	  return diagnosticInfo;
	 }

	 public String getLanguageCode() {
	  return languageCode;
	 }

	 // Setter Methods 

	 public void setQueryText(String queryText) {
	  this.queryText = queryText;
	 }

	

	 public void setAllRequiredParamsPresent(boolean allRequiredParamsPresent) {
	  this.allRequiredParamsPresent = allRequiredParamsPresent;
	 }

	 public void setFulfillmentText(String fulfillmentText) {
	  this.fulfillmentText = fulfillmentText;
	 }

	 public void setIntent(Intent intentObject) {
	  this.intent = intentObject;
	 }

	 public void setIntentDetectionConfidence(float intentDetectionConfidence) {
	  this.intentDetectionConfidence = intentDetectionConfidence;
	 }

	 public void setDiagnosticInfo(DiagnosticInfo diagnosticInfoObject) {
	  this.diagnosticInfo = diagnosticInfoObject;
	 }

	 public void setLanguageCode(String languageCode) {
	  this.languageCode = languageCode;
	 }



	public HashMap<String, String> getParameters() {
		return parameters;
	}



	public void setParameters(HashMap<String, String> parameters) {
		this.parameters = parameters;
	}
	}