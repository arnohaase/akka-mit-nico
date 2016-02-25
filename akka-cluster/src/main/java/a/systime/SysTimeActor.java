package a.systime;

import a.systime.in.MsgGetSystemTime;
import a.systime.out.MsgSystemTime;
import akka.actor.UntypedActor;
import scala.PartialFunction;
import scala.Unit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;

import static a.util.ScalaFunctionAdapter.*;


public class SysTimeActor extends UntypedActor {
    final PartialFunction<Object, Unit> handler = partFunc (
            voidCase (MsgGetSystemTime.class, this::onGetSystemTime)
    );

    @Override
    public void onReceive (Object message) throws Exception {
        handler.apply (message);
    }

    private void onGetSystemTime (MsgGetSystemTime msg) throws UnknownHostException {
        System.err.println ("******************** getting sys time");
        sender ().tell (new MsgSystemTime (InetAddress.getLocalHost ().getHostName (), Instant.now ()), self ());
        System.err.println ("... and done");
    }
}
