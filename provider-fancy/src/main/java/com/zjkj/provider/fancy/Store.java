package com.zjkj.provider.fancy;

import java.util.LinkedHashMap;
import java.util.Map;

public class Store {
    private CacheLinkedHashMap<String, FancyProvider.Factory> mMap = new CacheLinkedHashMap<>(6);

    public void put(String key,FancyProvider.Factory value){
        mMap.put(key, value);
    }

    public FancyProvider.Factory get(String key){
        return mMap.get(key);
    }

    public void clear(){
        mMap.clear();
    }

    static class CacheLinkedHashMap<K,V> extends LinkedHashMap<K, V> {
        private int maxCaches;

        public CacheLinkedHashMap(int maxCaches) {
            this.maxCaches = maxCaches;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size()>maxCaches;
        }
    }
}
