package a;

import a.systime.SysTimeActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.io.IOException;


public class AClusterMain {
    public static void main (String[] args) throws IOException {
        final ActorSystem system = ActorSystem.create ();

        final ActorRef sysTimeActor = system.actorOf (Props
                .create (SysTimeActor.class, () -> new SysTimeActor ()),
                "SysTime");

        final AHttpApp httpApp = new AHttpApp (system, sysTimeActor);
        httpApp.bindRoute ("localhost", 8080, system);

        System.out.println ("Started HTTP endpoints");
        System.in.read ();

        system.shutdown ();
    }
}
