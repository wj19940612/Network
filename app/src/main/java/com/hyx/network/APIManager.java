package com.hyx.network;

/**
 * created by ${nishuideyu}
 * 2019/9/4
 * 仅只是示例  建议每一个模块创建一个{@link NetworkManager}的子类  和一个类似于{@link NetworkApi}的类
 */
public class APIManager extends NetworkManager<NetworkApi> {

    private static APIManager sApiManager;

    private NetworkApi mNetworkApi;

    public synchronized static APIManager getInstance() {
        if (sApiManager == null) {
            sApiManager = new APIManager();
        }
        return sApiManager;
    }

    private APIManager() {
    }

    @Override
    public void reset() {
        // TODO: 2019/9/4
        mNetworkApi = createNetworkApi("");
    }

    @Override
    protected Class<NetworkApi> getApiClass() {
        return NetworkApi.class;
    }

    public NetworkApi getNetworkApi() {
        return mNetworkApi;
    }
}
