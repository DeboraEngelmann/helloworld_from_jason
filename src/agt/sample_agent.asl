+request(Req)
	:true
<-
	.print("Recebido request ",Req," do Dialog");
	!responder(Req);
	.
	
+!responder(Req)
	: (Req == "callJasonAgent")
<-
	+response("Ol�, eu sou seu agente Jason, em que posso lhe ajudar?");
	.
+!responder(Req)
	: true
<-
	+response("Desculpe, n�o reconhe�o essa inten��o");
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