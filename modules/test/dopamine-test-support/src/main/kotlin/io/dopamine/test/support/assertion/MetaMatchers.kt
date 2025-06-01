package io.dopamine.test.support.assertion

/**
 * Asserts that the traceId exists in the meta map and matches the expected value.
 */
infix fun Map<*, *>?.shouldContainTraceId(expectedValue: String) {
    requireNotNull(this) { "meta must not be null" }
    require(this.containsKey("traceId")) { "meta should contain key 'traceId'" }
    require(this["traceId"] == expectedValue) {
        "Expected traceId to be '$expectedValue' but was '${this["traceId"]}'"
    }
}

/**
 * Asserts that the traceId does not exist in the meta map.
 */
infix fun Map<*, *>?.shouldNotContainTraceId(key: String = "traceId") {
    if (this != null && this.containsKey(key)) {
        throw AssertionError("Expected meta to not contain traceId, but found '${this[key]}'")
    }
}
