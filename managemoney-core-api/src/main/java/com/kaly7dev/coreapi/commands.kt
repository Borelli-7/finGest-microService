package com.kaly7dev.coreapi

import org.axonframework.modelling.command.TargetAggregateIdentifier


abstract class BaseCommand <T>(
    @TargetAggregateIdentifier
    open val id: T
        )
data class CreateTransactionCommand(
    override val id: String,
    val desc: String,
    val amount: Double,
    val month: String,
    val week: String
):BaseCommand<String>(id)

data class UpdateTransactionCommand(
    override val id: String,
    val desc: String,
    val amount: Double,
    val month: String,
    val week: String
):BaseCommand<String>(id)