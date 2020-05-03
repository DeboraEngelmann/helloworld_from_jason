package br.pucrs.smart.integration;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.pucrs.smart.interfaces.IAgent;

public class MyActionsApp {
	HashMap<String,String> requests = new HashMap<String, String>();
	private static final Logger LOG = LoggerFactory.getLogger(MyActionsApp.class);
	IAgent mas = null;

	public void setListener(IAgent mas) {
		this.mas = mas;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}				
				System.out.println("Inicia Integracao");
				// requests.put("issoeteste", getResponseBuilder(request));
				requests.put("issoeteste", null);
				if (mas != null)
					mas.processarIntencao("issoeteste",null);
			}
		}).start();
	}
	
	public void enviarResposta(String session, int numero){
		Object response = this.requests.get(session);
		LOG.info("enviarResposta");
		if (response == null)
			System.out.println("integracao action is null");
		else
			System.out.println("integracao action isn't null");
//			response.add("uhulll")
	}
}
