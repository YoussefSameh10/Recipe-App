package com.example.recipeapp.domain.util

interface DomainMapper<Entity, DomainModel> {
    fun mapToDomainModel(entity: Entity): DomainModel
    fun mapFromDomainModel(domainModel: DomainModel): Entity
}