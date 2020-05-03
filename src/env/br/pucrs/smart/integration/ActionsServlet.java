package br.pucrs.smart.integration;

//
//import java.io.IOException;
//import java.util.stream.Collectors;

//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

//import jacamo.infra.JaCaMoLauncher;
//import jason.JasonException;

//@WebServlet(name = "ActionsServlet", urlPatterns = {"hello"}, loadOnStartup = 1) 
public class ActionsServlet {//extends HttpServlet {
	
//	public ActionsServlet() {
//		try {
//			JaCaMoLauncher.main(new String[] { "helloworld_from_jason.jcm" });
//			System.out.println("Artefato criado");
//		} catch (JasonException e) {
//			System.out.println("Ocorreu um erro ao iniciar o JCM. Verifique os logs da aplicação. Exception: " + e.getMessage());
//			e.printStackTrace();
//		}
//	}
//	@Override
//	protected void doGet(HttpServletRequest request, HttpServletResponse response)
//        throws ServletException, IOException {
//        response.getWriter().print("Hello, World!");  
//    }

//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//        throws ServletException, IOException {
//        String name = request.getParameter("name");
//        if (name == null) name = "World";
//        request.setAttribute("user", name);
//        request.getRequestDispatcher("response.jsp").forward(request, response); 
//    }
    
//	 @Override
//	  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
//	    String body = req.getReader().lines().collect(Collectors.joining());
////	    LOG.info("doPost, body = {}", body);
//	    System.out.println("Req: " + req);
//	    res.setContentType("application/json");
//	    writeResponse(res, "{resp: \"Chegou aqui\"}");
////	    try {
////	    	if(actionsApp == null) {
////	    		LOG.info("actionsApp Nullo");
////	    		res.setContentType("application/json");
////	    	    writeResponse(res, "{resp: \"está nullo\"}");
////	    	} else {
////	      String jsonResponse = actionsApp.handleRequest(body, getHeadersMap(req)).get();
////	      LOG.info("Generated json = {}", jsonResponse);
////	      res.setContentType("application/json");
////	      writeResponse(res, jsonResponse);
////	      }
////	    } catch (InterruptedException e) {
////	      handleError(res, e);
////	    } 
////	    catch (ExecutionException e) {
////	      handleError(res, e);
////	    }
//	  }
//	 
//	  private void writeResponse(HttpServletResponse res, String asJson) {
//	    try {
//	      res.getWriter().write(asJson);
//	    } catch (IOException e) {
//	      e.printStackTrace();
//	    }
//	  }
//}

//import java.io.IOException;
////import java.util.Enumeration;
////import java.util.HashMap;
////import java.util.Map;
////import java.util.concurrent.ExecutionException;
//import java.util.stream.Collectors;
//
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@WebServlet(name = "actions", value = "/")
//public class ActionsServlet extends HttpServlet {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//	
//	private static final Logger LOG = LoggerFactory.getLogger(ActionsServlet.class);
////	private MyActionsApp actionsApp = null;
//	
////	public void setDialog(MyActionsApp dialog) {
////		  actionsApp = dialog;
////	  }
//	public ActionsServlet(MyActionsApp dialog) {
//		System.out.println("ActionsServlet criado. Recebeu instância de MyActionsApp");
//	}
//	
//	 @Override
//	  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
//	    String body = req.getReader().lines().collect(Collectors.joining());
//	    LOG.info("doPost, body = {}", body);
//	    System.out.println("Req: " + req);
//	    res.setContentType("application/json");
//	    writeResponse(res, "{resp: \"Chegou aqui\"}");
////	    try {
////	    	if(actionsApp == null) {
////	    		LOG.info("actionsApp Nullo");
////	    		res.setContentType("application/json");
////	    	    writeResponse(res, "{resp: \"está nullo\"}");
////	    	} else {
////	      String jsonResponse = actionsApp.handleRequest(body, getHeadersMap(req)).get();
////	      LOG.info("Generated json = {}", jsonResponse);
////	      res.setContentType("application/json");
////	      writeResponse(res, jsonResponse);
////	      }
////	    } catch (InterruptedException e) {
////	      handleError(res, e);
////	    } 
////	    catch (ExecutionException e) {
////	      handleError(res, e);
////	    }
//	  }
//
//	  @Override
//	  protected void doGet(HttpServletRequest request, HttpServletResponse response)
//	      throws IOException {
//	    response.setContentType("text/plain");
//	    response
//	        .getWriter()
//	        .println(
//	            "ActionsServlet is listening but requires valid POST request to respond with Action response.");
//	  }
//
//	  private void writeResponse(HttpServletResponse res, String asJson) {
//	    try {
//	      res.getWriter().write(asJson);
//	    } catch (IOException e) {
//	      e.printStackTrace();
//	    }
//	  }
//
////	  private void handleError(HttpServletResponse res, Throwable throwable) {
////	    try {
////	      throwable.printStackTrace();
////	      LOG.error("Error in App.handleRequest ", throwable);
////	      res.getWriter().write("Error handling the intent - " + throwable.getMessage());
////	    } catch (IOException e) {
////	      e.printStackTrace();
////	    }
////	  }
////
////	  private Map<String, String> getHeadersMap(HttpServletRequest request) {
////	    Map<String, String> map = new HashMap();
////
////	    Enumeration headerNames = request.getHeaderNames();
////	    while (headerNames.hasMoreElements()) {
////	      String key = (String) headerNames.nextElement();
////	      String value = request.getHeader(key);
////	      map.put(key, value);
////	    }
////	    return map;
////	  }
//
}
