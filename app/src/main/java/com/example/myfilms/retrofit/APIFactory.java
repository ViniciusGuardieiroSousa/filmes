package com.example.myfilms.retrofit;

public final class APIFactory {
    private static NetworkAPI networkAPI;
    public static NetworkAPI getAPI(){
        if(networkAPI == null)
            networkAPI = new RetrofitCreator();
        return networkAPI;
    }

}
