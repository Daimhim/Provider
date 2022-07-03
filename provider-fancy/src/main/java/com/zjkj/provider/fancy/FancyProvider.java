package com.zjkj.provider.fancy;

public class FancyProvider {
    private final Store mStore = new Store();
    private final String DEFAULT_KEY = "DEFAULT_KEY";

    public interface Factory {
        <T> T create(Class<T> clazz);

        default String getId() {
            return String.valueOf(hashCode());
        }
    }

    public  <T>T get( Class<T> clazz,Factory factory) {
        String key = String.format("%s%s%s",DEFAULT_KEY,clazz.getCanonicalName(),factory.getId());
        return get(clazz,key,factory);
    }

    public <T> T get(Class<T> clazz,String key, Factory factory)  {
        Factory olfactory = mStore.get(key);
        if (olfactory == null) {
            mStore.put(key, factory);
            olfactory = factory;
        }
        return olfactory.create(clazz);
    }

    public <T> T get(Class<T> clazz,String key, Class<? extends Factory> factory) {
        Factory olfactory = mStore.get(key);
        if (olfactory == null) {
            try {
                olfactory = factory.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            mStore.put(key, olfactory);
        }
        if (olfactory == null){
            throw new NullPointerException("olfactory Can't be empty");
        }
        return olfactory.create(clazz);
    }

    public void clear(){
        mStore.clear();
    }
}
