package org.daimhim.provider.generate

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment

class CodeAssistantProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
//        environment.logger.info("CodeAssistantProvider create")
        environment.logger.error("CodeAssistantProvider create")
        return CodeAssistantProcessor()
    }
}