package io.dopamine.auth.common.authorization

/**
 * Marks a method parameter as the target resource ID for permission checks.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class TargetId
