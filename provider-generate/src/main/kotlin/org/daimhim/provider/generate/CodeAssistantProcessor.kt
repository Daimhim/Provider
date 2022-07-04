package org.daimhim.provider.generate

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toClassName
import com.zjkj.provider.annotation.FancyApi
import com.zjkj.provider.annotation.FancyApis
import com.zjkj.provider.fancy.FancyHelper
import kotlin.concurrent.thread


/**
 * author : Zhangx
 * date : 2021/6/16 11:22
 * description :
 */
class CodeAssistantProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    companion object {
        val FANCY_API_NAME = requireNotNull(FancyApi::class.qualifiedName)
    }

    override fun finish() {}
    override fun onError() {}
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(FANCY_API_NAME)
        val ret = symbols.filter { !it.validate() }.toList()
        val mutableMapOf = mutableMapOf<String, TypeSpec.Builder>()
        symbols
            .filter { it is KSClassDeclaration && it.validate() }
            .also {
                if (it.count() == 0){
                    return mutableListOf()
                }
            }
            .forEach { ksAnnotated ->
                val rootClassName = (ksAnnotated as KSClassDeclaration).toClassName()
                ksAnnotated.annotations.forEach { annotationIt ->
                    val alias = "${annotationIt.arguments[2].value}"
                    val keyStr = "${annotationIt.arguments[3].value}.${annotationIt.arguments[0].value}"
                    val groupCls = ClassName.bestGuess(keyStr)
                    val returnType = annotationIt.arguments[1].value as KSType
                    if (mutableMapOf.containsKey(keyStr)) {
                        mutableMapOf[keyStr]!!
                    } else {
                        TypeSpec.classBuilder(groupCls.simpleName)
                    }
                        .addFunction(
                            FunSpec
                                .builder(alias)
                                .returns(rootClassName)
                                .addStatement(
                                    "return %T.getInstance().create(%T::class.java,\"${groupCls.simpleName}:${alias}\",%T::class.java)",
                                    ClassName.bestGuess(FancyHelper::class.java.name),
                                    rootClassName,
                                    returnType.toClassName()
                                )
                                .build()
                        )
                        .let {
                            mutableMapOf[keyStr] = it
                        }
                }
            }
        val dependencies = Dependencies(true)
        thread(true) {
            mutableMapOf.forEach { (s, builder) ->
                val groupCls = ClassName.bestGuess(s)
                val fileSpec = FileSpec
                    .builder(groupCls.packageName, groupCls.simpleName)
                    .addType(builder
                        .addType(
                            TypeSpec
                                .companionObjectBuilder()
                                .addProperty(
                                    PropertySpec
                                        .builder("instance",groupCls.copy(nullable = true))
                                        .mutable()
                                        .addModifiers(KModifier.PRIVATE)
                                        .initializer("null")
                                        .build()
                                )
                                .addFunction(
                                    FunSpec
                                        .builder("getInstance")
                                        .addAnnotation(JvmStatic::class)
                                        .addStatement("if (instance == null) {")
                                        .addStatement("  instance = %T()",groupCls)
                                        .addStatement("}")
                                        .addStatement("return instance!!")
                                        .returns(groupCls)
                                        .build()
                                )
                                .build()
                        )
                        .build())
                    .build()
                environment
                    .codeGenerator
                    .createNewFile(dependencies, groupCls.packageName, groupCls.simpleName, "kt")
                    .bufferedWriter()
                    .use {
                        fileSpec.writeTo(it)
                    }
            }
        }
        return ret
    }

    private fun println(text: String) {
        environment.logger.warn(text)
    }

}