package org.daimhim.provider.generate

import com.squareup.kotlinpoet.*
import com.zjkj.provider.annotation.FancyApi
import com.zjkj.provider.annotation.FancyApis
import com.zjkj.provider.fancy.FancyHelper
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.tools.Diagnostic


/**
 * author : Zhangx
 * date : 2021/6/16 11:22
 * description :
 *
 */
class CodeAssistantProcessor : AbstractProcessor() {
    //    String group() default "";  0
//    Class<? extends FancyProvider.Factory> cla(); 1
//    String alias() default ""; 2
//    String path() default "com.zjkj.provider"; 3
    override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {
        val mutableMapOf = mutableMapOf<String, TypeSpec.Builder>()
        val callback: (Element, FancyApi) -> Unit = { eIt, annotation ->
            val alias = annotation.alias
            val keyStr = "${annotation.path}.${annotation.group}"
            val groupCls = ClassName.bestGuess(keyStr)
            val returnType = ClassName.bestGuess(eIt.toString())
            val factoryCla:String = try {
                annotation.cla.toString()
            }catch (e: MirroredTypeException){
                e.typeMirror.toString()
            }
            if (mutableMapOf.containsKey(keyStr)) {
                mutableMapOf[keyStr]!!
            } else {
                TypeSpec.classBuilder(groupCls.simpleName)
            }
                .addFunction(
                    FunSpec
                        .builder(alias)
                        .returns(returnType)
                        .addStatement(
                            "return %T.getInstance().create(%T::class.java,\"${groupCls.simpleName}:${alias}\",%T::class.java)",
                            ClassName.bestGuess(FancyHelper::class.java.name),
                            returnType,
                            ClassName.bestGuess(factoryCla)
                        )
                        .build()
                )
                .let {
                    mutableMapOf[keyStr] = it
                }
        }
        p1
            ?.getElementsAnnotatedWith(FancyApis::class.java)
            ?.forEach { eIt ->
                eIt.getAnnotation(FancyApis::class.java)
                    .value
                    .forEach { annotation ->
                        callback(eIt, annotation)
                    }
            }
        p1
            ?.getElementsAnnotatedWith(FancyApi::class.java)
            ?.forEach { eIt ->
                callback(eIt, eIt.getAnnotation(FancyApi::class.java))
            }
        mutableMapOf.forEach { (s, builder) ->
            val groupCls = ClassName.bestGuess(s)
            val fileSpec = FileSpec
                .builder(groupCls.packageName, groupCls.simpleName)
                .addType(
                    builder
                        .addType(
                            TypeSpec
                                .companionObjectBuilder()
                                .addProperty(
                                    PropertySpec
                                        .builder("instance", groupCls.copy(nullable = true))
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
                                        .addStatement("  instance = %T()", groupCls)
                                        .addStatement("}")
                                        .addStatement("return instance!!")
                                        .returns(groupCls)
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .build()
            filer?.let {
                fileSpec.writeTo(it)
            }
        }
        return true
    }
    private var filer: Filer? = null
    private var messager: Messager? = null
    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        filer = processingEnv?.filer
        messager = processingEnv?.messager
    }

    fun println(text:String){
        messager?.printMessage(Diagnostic.Kind.WARNING,text)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val mutableSetOf = mutableSetOf<String>()
        mutableSetOf.add(FancyApi::class.java.name)
        mutableSetOf.add(FancyApis::class.java.name)
        return mutableSetOf
    }


}