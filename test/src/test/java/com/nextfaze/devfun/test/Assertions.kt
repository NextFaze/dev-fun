package com.nextfaze.devfun.test

import android.os.Build
import com.nextfaze.devfun.core.FunctionDefinition
import com.nextfaze.devfun.core.FunctionItem
import kotlin.test.assertTrue
import kotlin.test.expect
import kotlin.test.fail

annotation class Assertions

typealias Assertable = (receiver: FunctionDefinition, items: List<FunctionItem>) -> Any?

data class TestableTest(private inline val test: Assertable) : Assertable {
    override fun invoke(receiver: FunctionDefinition, items: List<FunctionItem>): Any? {
        return test(receiver, items)
    }
}

data class ExpectedItemCount(val expectedItemCount: Int) : Assertable {
    override fun invoke(receiver: FunctionDefinition, items: List<FunctionItem>): Any? {
        expect(expectedItemCount, "Unexpected item count") { items.size }
        return "Definition $receiver generated $expectedItemCount item(s)"
    }
}

data class SingleItemExpectedNameTest(val expectedItemName: String) : Assertable {
    override fun invoke(receiver: FunctionDefinition, items: List<FunctionItem>): Any? {
        expect(expectedItemName, "Item name did not match") { items.single().name }
        return "Definition with method name '${receiver.method.name}' had item name '$expectedItemName'"
    }
}

data class ExpectedNamesTest(val expectedItemNames: List<String>) : Assertable {
    override fun invoke(receiver: FunctionDefinition, items: List<FunctionItem>): Any? {
        expectedItemNames.forEach { expectedName ->
            expect(expectedName, "Item name did not match") { items.singleOrNull { it.name == expectedName }?.name }
        }
        return "Definition with method name '${receiver.method.name}' had ${expectedItemNames.size} item name(s) '$expectedItemNames'"
    }
}

data class SingleItemRequiresApiTest(val requiresApi: Int?) : Assertable {
    override fun invoke(receiver: FunctionDefinition, items: List<FunctionItem>): Any? {
        if (items.isEmpty()) {
            assertTrue("Fun with requiresApi $requiresApi was not visible but should have been. SDK_INT=${Build.VERSION.SDK_INT}") {
                requiresApi != null && requiresApi > Build.VERSION.SDK_INT
            }
            return "Definition with requiresApi $requiresApi did not have an item due to SDK_INT of ${Build.VERSION.SDK_INT}"
        } else {
            expect(1, "Only single item expected") { items.size }
            assertTrue("Fun with requiresApi $requiresApi was unexpected. SDK_INT=${Build.VERSION.SDK_INT}") {
                requiresApi == null || requiresApi <= 0 || requiresApi <= Build.VERSION.SDK_INT
            }
            return "Definition with requiresApi $requiresApi had an item due to SDK_INT of ${Build.VERSION.SDK_INT}"
        }
    }
}

data class ExpectedArgs<out T>(val args: List<Pair<T, T>>) : Assertable {
    override fun invoke(receiver: FunctionDefinition, items: List<FunctionItem>): Any? {
        args.forEachIndexed { i, pair ->
            expect(pair.second, "Arg index $i did not match item args") { pair.first }
        }
        return "Item was supplied with ${args.map { it.second }}"
    }
}

data class ExpectedCategoryName(val expectedCategoryName: String) : Assertable {
    override fun invoke(receiver: FunctionDefinition, items: List<FunctionItem>): Any? {
        expect(expectedCategoryName, "Item category name did not match") { items.single().category.name }
        return "Definition with method name '${receiver.method.name}' had category name '$expectedCategoryName'"
    }
}

data class ExpectedCategoryOrder(val expectedCategoryOrder: Int?) : Assertable {
    override fun invoke(receiver: FunctionDefinition, items: List<FunctionItem>): Any? {
        expect(expectedCategoryOrder, "Item category order did not match") { items.single().category.order }
        return "Definition with method name '${receiver.method.name}' had category order '$expectedCategoryOrder'"
    }
}

data class ExpectedItemGroup(val expectedItemGroup: String?) : Assertable {
    override fun invoke(receiver: FunctionDefinition, items: List<FunctionItem>): Any? {
        expect(expectedItemGroup, "Item group did not match") { items.single().group }
        return "Definition with method name '${receiver.method.name}' had group '$expectedItemGroup'"
    }
}

data class NeverRun(val message: String = "") : Assertable {
    override fun invoke(receiver: FunctionDefinition, items: List<FunctionItem>): Any? {
        fail("$message :: This should not be reached! receiver=$receiver, items=$items")
    }
}
