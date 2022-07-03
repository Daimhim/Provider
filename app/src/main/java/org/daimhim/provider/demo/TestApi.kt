package org.daimhim.provider.demo

import com.zjkj.provider.annotation.FancyApi
import org.daimhim.provider.demo.DefFac

@FancyApi(group = "TestGroup", cla = DefFac::class, alias = "testApi")
interface TestApi {
}