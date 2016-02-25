package a.systime.out;


import java.io.Serializable;
import java.net.InetAddress;
import java.time.Instant;


public class MsgSystemTime implements Serializable {
    public final String hostname;
    public final Instant sysTime;

    public MsgSystemTime (String hostname, Instant sysTime) {
        this.hostname = hostname;
        this.sysTime = sysTime;
    }

    @Override
    public String toString () {
        return "MsgSystemTime{" +
                "hostname='" + hostname + '\'' +
                ", sysTime=" + sysTime +
                '}';
    }
}

