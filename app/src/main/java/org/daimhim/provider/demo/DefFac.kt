package org.daimhim.provider.demo

import com.zjkj.provider.fancy.FancyProvider
import java.lang.reflect.Proxy

class DefFac : FancyProvider.Factory {
    override fun <T : Any> create(clazz: Class<T>): T {
        return Proxy.newProxyInstance(clazz.classLoader, arrayOf(clazz)
        ) { proxy, method, args ->

        } as T
    }
}