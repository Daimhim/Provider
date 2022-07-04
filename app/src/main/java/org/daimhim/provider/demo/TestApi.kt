package org.daimhim.provider.demo

import com.zjkj.provider.annotation.FancyApi
import org.daimhim.provider.demo.DefFac

@FancyApi(
    group = "TestGroup",
    cla = DefFac::class,
    alias = "testApi",
    path = "org.daimhim.provider.api"
)
@FancyApi(
    group = "TestGroup2",
    cla = DefFac::class,
    alias = "testApi3",
    path = "org.daimhim.provider.api"
)
interface TestApi {
}