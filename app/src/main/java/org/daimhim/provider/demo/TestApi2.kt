package org.daimhim.provider.demo

import com.zjkj.provider.annotation.FancyApi

@FancyApi(
    group = "TestGroup2",
    cla = DefFac::class,
    alias = "testApi4",
    path = "org.daimhim.provider.api"
)
interface TestApi2 {
}