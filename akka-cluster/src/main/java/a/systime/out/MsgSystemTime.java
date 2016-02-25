package a.systime.out;


import java.net.InetAddress;
import java.time.Instant;


public class MsgSystemTime {
    public final InetAddress inetAddress;
    public final Instant sysTime;

    public MsgSystemTime (InetAddress inetAddress, Instant sysTime) {
        this.inetAddress = inetAddress;
        this.sysTime = sysTime;
    }

    @Override
    public String toString () {
        return "MsgSystemTime{" +
                "inetAddress=" + inetAddress +
                ", sysTime=" + sysTime +
                '}';
    }
}

