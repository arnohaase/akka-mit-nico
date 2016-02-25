package a;

import a.systime.SysTimeActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.io.IOException;


public class AClusterMain {
    public static void main (String[] args) throws IOException {
        final String hostname = args[0];
        final int port = 8181;

        final ActorSystem system = ActorSystem.create ();

        final ActorRef sysTimeActor = system.actorOf (Props
                .create (SysTimeActor.class, () -> new SysTimeActor ()),
                "SysTime");

        final AHttpApp httpApp = new AHttpApp (system, sysTimeActor);

        httpApp.bindRoute (hostname, port, system);

        System.out.println ("Started HTTP endpoint: " + hostname + ":" + port);
        System.in.read ();

        system.shutdown ();
    }
}
