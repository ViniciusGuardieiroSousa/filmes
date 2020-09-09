package com.example.myfilms.repository.retrofit;

public final class APIFactory {
    private static NetworkAPI networkAPI;
    public static NetworkAPI getAPI(){
        if(networkAPI == null)
            networkAPI = new RetrofitCreator();
        return networkAPI;
    }

}
