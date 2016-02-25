package a;

import a.systime.in.MsgGetSystemTime;
import a.systime.out.MsgSystemTime;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.MediaTypes;
import akka.http.javadsl.server.HttpApp;
import akka.http.javadsl.server.RequestContext;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.server.RouteResult;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

import java.util.concurrent.TimeUnit;

import static a.util.ScalaFunctionAdapter.fCase;
import static a.util.ScalaFunctionAdapter.partFunc;


public class AHttpApp extends HttpApp {
    final ActorSystem system;
    final ExecutionContext ec;

    final ActorRef sysTimeActor;

    public AHttpApp (ActorSystem system, ActorRef sysTimeActor) {
        this.system = system;
        this.ec = system.dispatcher ();
        this.sysTimeActor = sysTimeActor;
    }

    @Override
    public Route createRoute () {
        return route (
                path ("systime").route (
                        get (
                                handleWithAsync (this::onGetSysTime)
                        )
                )
        );
    }

    private Future<RouteResult> onGetSysTime (RequestContext ctx) {
        return Patterns
                .ask (sysTimeActor, MsgGetSystemTime.INSTANCE, Timeout.apply (10, TimeUnit.SECONDS))
                .map (partFunc (fCase (MsgSystemTime.class, x -> ctx.complete (MediaTypes.APPLICATION_JSON.toContentType (), asJson (x)))), ec)
                ;
    }

    private String asJson (MsgSystemTime msg) {
        return "{\"time\": " + msg.sysTime.toEpochMilli () + "}";
    }
}
