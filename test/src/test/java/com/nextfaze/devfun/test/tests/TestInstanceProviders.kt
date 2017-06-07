package com.nextfaze.devfun.test.tests

import com.nextfaze.devfun.inject.KObjectInstanceProvider
import org.testng.annotations.Test
import tested.instance_providers.internalObject
import tested.instance_providers.privateObject
import tested.instance_providers.publicObject
import kotlin.test.assertNotNull

@Test(groups = arrayOf("instanceProvider", "supported"))
class TestInstanceProviders {

    fun testKObjectVisibilities() {
        val kObjectProvider = KObjectInstanceProvider()

        val publicObjInst = kObjectProvider[publicObject]
        assertNotNull(publicObjInst) { "Public KObject was null!" }

        val internalObjInst = kObjectProvider[internalObject]
        assertNotNull(internalObjInst) { "Internal KObject was null!" }

        val privateObjInst = kObjectProvider[privateObject]
        assertNotNull(privateObjInst) { "Private KObject was null!" }
    }
}
