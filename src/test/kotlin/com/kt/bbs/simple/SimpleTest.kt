package com.kt.bbs.simple

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class SimpleTest : FunSpec({
    test("simple test") {
        1 shouldBe 1
    }
})
