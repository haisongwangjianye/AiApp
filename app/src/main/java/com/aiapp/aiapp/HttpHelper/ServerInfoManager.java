package com.aiapp.aiapp.HttpHelper;

/**
 * Created by wjy on 2018/11/30.
 */

public class ServerInfoManager {
    String mServerIp = "192.168.1.111";
    String mServerPort = "11414";
    private static final ServerInfoManager ourInstance = new ServerInfoManager();

    public static ServerInfoManager getInstance() {
        return ourInstance;
    }

    public String getServerAllIp()
    {
        String strIp = mServerIp+":" +mServerPort;
        return mServerIp+":" +mServerPort;
    }
    public String getmServerIp() {
        return mServerIp;
    }

    public void setmServerIp(String mServerIp) {
        this.mServerIp = mServerIp;
    }

    public String getmServerPort() {
        return mServerPort;
    }

    public void setmServerPort(String mServerPort) {
        this.mServerPort = mServerPort;
    }

    private ServerInfoManager() {

    }
}
