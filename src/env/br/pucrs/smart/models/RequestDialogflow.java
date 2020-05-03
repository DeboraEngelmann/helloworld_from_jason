package br.pucrs.smart.models;

public class RequestDialogflow {
	 private String responseId;
	 QueryResult queryResult;
	 WebhookStatus webhookStatus;
	


	 // Getter Methods 

	 public String getResponseId() {
	  return responseId;
	 }

	 public QueryResult getQueryResult() {
	  return queryResult;
	 }

	 public WebhookStatus getWebhookStatus() {
	  return webhookStatus;
	 }

	 // Setter Methods 

	 public void setResponseId(String responseId) {
	  this.responseId = responseId;
	 }

	 public void setQueryResult(QueryResult queryResultObject) {
	  this.queryResult = queryResultObject;
	 }

	 public void setWebhookStatus(WebhookStatus webhookStatusObject) {
	  this.webhookStatus = webhookStatusObject;
	 }
	}
	
	
	
	
	