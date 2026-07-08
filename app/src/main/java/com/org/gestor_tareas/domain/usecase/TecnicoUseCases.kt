package com.org.gestor_tareas.domain.usecase

data class TecnicoUseCases(
    val getTecnicos: GetTecnicosUseCase,
    val addTecnico: AddTecnicoUseCase,
    val updateTecnico: UpdateTecnicoUseCase,
    val deleteTecnico: DeleteTecnicoUseCase
)