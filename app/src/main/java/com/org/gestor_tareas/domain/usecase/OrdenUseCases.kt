package com.org.gestor_tareas.domain.usecase

data class OrdenUseCases(
    val getOrdenes: GetOrdenesUseCase,
    val addOrden: AddOrdenUseCase,
    val updateOrden: UpdateOrdenUseCase,
    val deleteOrden: DeleteOrdenUseCase
)
