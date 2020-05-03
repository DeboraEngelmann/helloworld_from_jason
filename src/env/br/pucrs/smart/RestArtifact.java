package br.pucrs.smart;


import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;

import javax.ws.rs.core.UriBuilder;

import org.apache.commons.io.FileUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.zookeeper.server.NIOServerCnxnFactory;
import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import jacamo.platform.DefaultPlatformImpl;

public class RestArtifact extends DefaultPlatformImpl {

    public static String JaCaMoZKAgNodeId = "/jacamo/agents";
    public static String JaCaMoZKDFNodeId = "/jacamo/df";
    
    protected HttpServer restHttpServer = null;

    protected static URI restServerURI = null;
    
    protected ServerCnxnFactory zkFactory = null;
    protected static String zkHost = null;
    protected static CuratorFramework zkClient;
    
    static public String getRestHost() {
        if (restServerURI == null)
            return null;
        else
            return restServerURI.toString();
    }
    
    static public String getZKHost() {
        return zkHost;
    }
    
    static {
        confLog4j();
    }
    
    @Override
    public void init(String[] args) throws Exception {
        
        // change the runtimeservices
//        RuntimeServicesFactory.set( new JCMRuntimeServices() );
                
        int restPort = 3280;
        int zkPort   = 2181;
        boolean useZK = false;

        // Used when deploying on heroku
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            restPort = 8080;
        } else {
            restPort = Integer.parseInt(webPort);
        }
        
        if (args.length > 0) {
            String la = "";
            for (String a: args[0].split(" ")) {
                if (la.equals("--restPort"))
                    try {
                        restPort = Integer.parseInt(a);
                    } catch (Exception e) {
                        System.err.println("The argument for restPort is not a number.");
                    }

                if (a.equals("--main")) {
                    useZK = true;
                }
                if (la.equals("--main"))
                    try {
                        zkPort = Integer.parseInt(a);
                    } catch (Exception e) {
                        System.err.println("The argument for restPort is not a number.");
                    }

                if (la.equals("--connect")) {
                    zkHost = a;
                    useZK = true;
                }
                la = a;
            }           
        }
        
        restHttpServer = startRestServer(restPort);
        
        if (useZK) {
            if (zkHost == null) {
                zkFactory  = startZookeeper(zkPort);
                System.out.println("Platform (zookeeper) started on "+zkHost);
            } else {
                System.out.println("Platform (zookeeper) running on "+zkHost);
            }
        }
        
    }

   
    @Override
    public void stop() {
        if (restHttpServer != null)
            try {
                restHttpServer.shutdown();
            } catch (Exception e) {}
        restHttpServer = null;

        if (zkFactory != null)
            try {
                zkFactory.shutdown();
            } catch (Exception e) {}
        zkFactory = null;

        if (zkClient != null)
            zkClient.close();
        
        /*if (zkTmpDir != null) {
            try {
                FileUtils.deleteDirectory(zkTmpDir);
            } catch (IOException e) {
            }
            zkTmpDir = null;
        }*/
    }
    
    static void confLog4j() {
        try {
            ConsoleAppender console = new ConsoleAppender(); //create appender
            //configure the appender
            String PATTERN = "%d [%p|%c|%C{1}] %m%n";
            console.setLayout(new PatternLayout(PATTERN)); 
            console.setThreshold(Level.WARN);
            console.activateOptions();
            //add appender to any Logger (here is root)
            Logger.getRootLogger().addAppender(console);

            FileAppender fa = new FileAppender();
            fa.setName("FileLogger");
            fa.setFile("log/zk.log");
            fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
            fa.setThreshold(Level.WARN);
            fa.setAppend(true);
            fa.activateOptions();
            
            //add appender to any Logger (here is root)
            Logger.getRootLogger().addAppender(fa);
            //repeat with all other desired appenders
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public HttpServer startRestServer(int port) {
        try {
            restServerURI = UriBuilder.fromUri("http://"+InetAddress.getLocalHost().getHostAddress()+"/").port(port).build();
            
            RestAppConfig rc = new RestAppConfig();
            
            // get a server from factory
            HttpServer s = GrizzlyHttpServerFactory.createHttpServer(restServerURI, rc);
            
            System.out.println("JaCaMo Rest API is running on "+restServerURI);
            return s;
        } catch (javax.ws.rs.ProcessingException e) {           
            System.out.println("trying next port for rest server "+(port+1)+". e="+e);
            return startRestServer(port+1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    File zkTmpDir = null;
    static String zkTmpFileName = "jcm-zookeeper";
    
    public ServerCnxnFactory startZookeeper(int port) {
        int numConnections = 500;
        int tickTime = 2000;

        try {
            cleanZKFiles();
            
            zkHost = InetAddress.getLocalHost().getHostAddress()+":"+port;

            zkTmpDir = Files.createTempDirectory(zkTmpFileName).toFile(); 
            //System.out.println("ZK data at "+zkTmpDir);
            ZooKeeperServer server = new ZooKeeperServer(zkTmpDir, zkTmpDir, tickTime);
            server.setMaxSessionTimeout(4000);
            
            ServerCnxnFactory factory = new NIOServerCnxnFactory();
            factory.configure(new InetSocketAddress(port), numConnections);
            factory.startup(server); // start the server.   

            // create main nodes
            //client.delete().deletingChildrenIfNeeded().forPath("/jacamo");
            //client.create().forPath("/jacamo");
            getZKClient().create().creatingParentsIfNeeded().forPath(JaCaMoZKAgNodeId);
            getZKClient().create().creatingParentsIfNeeded().forPath(JaCaMoZKDFNodeId);
            //client.close();
            return factory;
        } catch (java.net.BindException e) {
            System.err.println("Cannot start zookeeper, port "+port+" already used!");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    void cleanZKFiles() {
        for (File f: FileUtils.getTempDirectory().listFiles()) {
            if (f.getName().startsWith(zkTmpFileName)) {
                try {
                    FileUtils.deleteDirectory(f);
                } catch (IOException e) {
                }
            }
        }
    }

    public static CuratorFramework getZKClient() {
        if (zkClient == null) {
            zkClient = CuratorFrameworkFactory.newClient(getZKHost(), new ExponentialBackoffRetry(1000, 3));
            zkClient.start();
        }
        return zkClient;
    }
    
//    class JCMRuntimeServices extends DelegatedRuntimeServices {
//        public JCMRuntimeServices() {
//            super(RuntimeServicesFactory.get());
//        }
//
//        @Override
//        public void dfRegister(String agName, String service, String type) {
//            try {
//                if (type == null) type = "no-type";
//                String node = RestArtifact.JaCaMoZKDFNodeId+"/"+service+"/"+agName;
//                if (getZKClient().checkExists().forPath(node) == null) {
//                    getZKClient().create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(node, type.getBytes());
//                } else {
//                    getZKClient().setData().forPath(node, type.getBytes());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        
//        @Override
//        public void dfDeRegister(String agName, String service, String type) {
//            try {
//                getZKClient().delete().forPath(RestArtifact.JaCaMoZKDFNodeId+"/"+service+"/"+agName);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        
//        @Override
//        public Collection<String> dfSearch(String service, String type) {
//            Set<String> ags = new HashSet<>();
//            try {
//                if (getZKClient().checkExists().forPath(RestArtifact.JaCaMoZKDFNodeId+"/"+service) != null) {
//                    for (String r : getZKClient().getChildren().forPath(RestArtifact.JaCaMoZKDFNodeId+"/"+service)) {
//                        ags.add(r);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return ags;
//        }
//        
//        @Override
//        public void dfSubscribe(String agName, String service, String type) {
//            try {
//                RestAgArch arch = getRestAgArch(agName); 
//                arch.getAsyncCurator()
//                    .with(WatchMode.successOnly).watched().getChildren().forPath(RestArtifact.JaCaMoZKDFNodeId+"/"+service).event().thenAccept(event -> {
//                        try {
//                            //System.out.println("something changed...."+event.getType()+"/"+event.getState());
//                            // stupid implementation: send them all again and
//                            dfSubscribe(agName, service, type); // keep watching
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    });
//
//                // update providers
//                Term s = new Atom("df");
//                Literal l = ASSyntax.createLiteral("provider", new UnnamedVar(), new StringTermImpl(service));
//                l.addSource(s);
//                arch.getTS().getAg().abolish(l, new Unifier());
//                for (String a: dfSearch(service, type)) {
//                    l = ASSyntax.createLiteral("provider", new Atom(a), new StringTermImpl(service));
//                    l.addSource(s);
//                    arch.getTS().getAg().addBel(l);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        
//        
//        @Override
//        public Collection<String> getAgentsNames() {
//            // use ZK WP
//            try {
//                List<String> all = new ArrayList<>();
//                for (String ag : getZKClient().getChildren().forPath(RestArtifact.JaCaMoZKAgNodeId)) {
//                    all.add(ag);
//                }
//                return all;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return super.getAgentsNames();
//            }
//        }
//        
//        @Override
//        public Map<String, Set<String>> getDF() {
//            if (getZKHost() == null) {
//                return super.getDF();
//            } else {
//                try {
//                    Map<String, Set<String>> commonDF = new HashMap<String, Set<String>>();
//
//                    for (String s : getZKClient().getChildren().forPath(RestArtifact.JaCaMoZKDFNodeId)) {
//                        for (String a : getZKClient().getChildren().forPath(RestArtifact.JaCaMoZKDFNodeId + "/" + s)) {
//                            commonDF.computeIfAbsent(a, k -> new HashSet<>()).add(s);
//                        }
//                    }
//                    return commonDF;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return null;
//                }
//            }
//        }                   
//    }

}
