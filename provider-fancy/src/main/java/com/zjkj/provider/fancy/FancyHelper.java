package com.zjkj.provider.fancy;

public class FancyHelper {
    private final FancyProvider fancyProvider = new FancyProvider();

    public <T> T create(Class<T> cla, FancyProvider.Factory factory) {
        return fancyProvider.get(cla, factory);
    }

    public <T> T create(Class<T> clazz, String key, Class<? extends FancyProvider.Factory> factory) {
        return fancyProvider.get(clazz, key, factory);
    }

    public void clear() {
        fancyProvider.clear();
    }

    private static FancyHelper instance;
    private FancyHelper (){
    }
    public static synchronized FancyHelper getInstance() {
        if (instance == null) {
            instance = new FancyHelper();
        }
        return instance;
    }
}
