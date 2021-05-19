+request(ResponseId, IntentName, Params, Contexts)
	:true
<-
	.print("Request received ",IntentName," of Dialog");
	!responder(ResponseId, IntentName, Params, Contexts);
	.
	
+!responder(ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Call Jason Agent")
<-
	reply("Hello, I am your agent Jason, I can help you?");
	.

	
+!responder(ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Call Intent By Event")
<-
	replyWithEvent("Answering with an event", "testEvent");
	.

+!responder(ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Intent Called By Event")
<-
	reply("Answering to an intention called by an event");
	.
	
+!responder(ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Call With Contexts and Parameters")
<-
	.print("The contexts and parameters will be listed below.");
	!printContexts(Contexts);
	!printParameters(Params);
	reply("Hello, I'm your agent Jason, I received your contexts and parameters");
	.
	
+!responder(ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Call With Contexts")
<-
	.print("The contexts will be listed below.");
	!printContexts(Contexts);
	reply("Hello, I’m your agent Jason, I received your contexts");
	.
	
+!responder(ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Reply With Context")
<-
	.print("The context will be created next.");
	contextBuilder(ResponseId, "test context", "1", Context);
	.print("Context created: ", Context);
	replyWithContext("Hello, I am your agent Jason, and I am responding with context", Context);
	.
	
+!responder(ResponseId, IntentName, Params, Contexts)
	: true
<-
	reply("Sorry, I do not recognize this intention");
	.

+!printContexts([]).
+!printContexts([Context|List])
<-
	.print(Context);
	!printContexts(List);
	.

+!printParameters([]).
+!printParameters([Param|List])
<-
	.print(Param)
	!printParameters(List)
	.
	
+!hello
    : True
<-
    .print("hello world");
    .

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }