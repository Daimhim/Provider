package org.daimhim.provider.generate

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.zjkj.provider.annotation.FancyApi
import com.zjkj.provider.annotation.FancyApis


/**
 * author : Zhangx
 * date : 2021/6/16 11:22
 * description :
 */
class CodeAssistantProcessor : SymbolProcessor {
    companion object{
        val FANCY_API_NAME = requireNotNull(FancyApi::class.qualifiedName)
        val FANCY_APIS_NAME = requireNotNull(FancyApis::class.qualifiedName)
    }
    override fun finish() {}
    override fun onError() {}
    override fun process(resolver: Resolver): List<KSAnnotated> {
        println("111122 process")
        val symbolsWithAnnotation = resolver.getSymbolsWithAnnotation(FANCY_API_NAME)
        symbolsWithAnnotation.forEach {
            it.annotations.forEach {
                println("111122 ${it.shortName.getShortName()}")
            }
        }
        return mutableListOf()
    }
}