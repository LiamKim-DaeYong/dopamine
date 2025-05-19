package io.dopamine.test.support

import org.springframework.core.MethodParameter
import org.springframework.http.ResponseEntity

class DummyMethodParameter : MethodParameter(DummyMethodParameter::class.java.getDeclaredMethod("dummy"), -1) {
    companion object {
        @JvmStatic fun dummy() {
            /** MethodParameter 생성용 dummy 메서드. 호출되지 않음. */
        }
    }
}

class ResponseEntityMethodParameter :
    MethodParameter(
        DummyMethodParameter::class.java.getDeclaredMethod("dummy"),
        -1,
    ) {
    override fun getParameterType(): Class<*> = ResponseEntity::class.java

    companion object {
        @JvmStatic fun dummy() {
            /** MethodParameter 생성용 dummy 메서드. 호출되지 않음. */
        }
    }
}

class StringMethodParameter :
    MethodParameter(
        DummyMethodParameter::class.java.getDeclaredMethod("dummy"),
        -1,
    ) {
    override fun getParameterType(): Class<*> = String::class.java

    companion object {
        @JvmStatic fun dummy() {
            /** MethodParameter 생성용 dummy 메서드. 호출되지 않음. */
        }
    }
}
