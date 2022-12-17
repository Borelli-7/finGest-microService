package com.kaly7dev.coreapi

abstract class BaseEvent <T>(
    open val id: T
        )

data class TransactionCreatedEvent(
    override val id: String,
    val desc: String,
    val amount: Double,
    val month: String,
    val week: String
):BaseEvent<String>(id)

data class TransactionUpdatedEvent(
    override val id: String,
    val desc: String,
    val amount: Double,
    val month: String,
    val week: String
):BaseEvent<String>(id)