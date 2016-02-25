package a;

import java.io.IOException;
import java.util.Collections;

import a.systime.SysTimeActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.routing.ClusterRouterGroup;
import akka.cluster.routing.ClusterRouterGroupSettings;
import akka.japi.Option;
import akka.routing.ConsistentHashingGroup;
import akka.routing.RoundRobinGroup;


public class AClusterMain {
    public static void main (String[] args) throws IOException {
        final String hostname = args[0];
        final int port = 8181;

        System.setProperty ("akka.remote.netty.tcp.hostname", hostname);

        final ActorSystem system = ActorSystem.create ("a-actor-system");

        final ActorRef sysTimeActor = system.actorOf (Props
                .create (SysTimeActor.class, () -> new SysTimeActor ()),
                "SysTime");

        final Iterable<String> routeesPaths = Collections.singletonList(sysTimeActor.path().toStringWithoutAddress());

        final ActorRef sysTimeRouter = system.actorOf(
        	    new ClusterRouterGroup (new RoundRobinGroup (routeesPaths),
        	        new ClusterRouterGroupSettings (100, routeesPaths, true, null)).props(),
                "sysTimeRouter");

        final AHttpApp httpApp = new AHttpApp (system, sysTimeRouter);

        httpApp.bindRoute (hostname, port, system);

        System.out.println ("Started HTTP endpoint: " + hostname + ":" + port);
        System.in.read ();

        system.shutdown ();
    }
}
