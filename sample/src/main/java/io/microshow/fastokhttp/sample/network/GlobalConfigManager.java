package io.microshow.fastokhttp.sample.network;

/**
 *
 */
public class GlobalConfigManager {

    private static GlobalConfigManager instance;

    /**
     * 服务器时间
     */
    private long serverTime;

    private String jwtIss = "vvic.com";

    private String jwtSecret = "LQt4eDVhGG734CpHs0LtIApxfrwbtEPo";

    private String apiDomain = "https://app.vvic.com";

    private GlobalConfigManager() {

    }

    /**
     * 单例获取
     */
    public static GlobalConfigManager getInstance() {
        if (instance == null) {
            synchronized (GlobalConfigManager.class) {
                if (instance == null) {
                    instance = new GlobalConfigManager();
                }
            }
        }
        return instance;
    }


    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public void setJwt (String jwtIss, String jwtSecret) {
        this.jwtIss = jwtIss;
        this.jwtSecret = jwtSecret;
    }

    public String getJwtIss() {
        return jwtIss;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public String getApiDomain() {
        return apiDomain;
    }

    public void setApiDomain(String apiDomain) {
        this.apiDomain = apiDomain;
    }
}
