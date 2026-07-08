package com.org.gestor_tareas.navigation

sealed class Rutas(val ruta: String) {
    object Inicio : Rutas("inicio")
    object Login : Rutas("login")
    object Registro : Rutas("registro")
    object Olvidaste : Rutas("olvidaste")
    object Dashboard : Rutas("dashboard")
    object Espera : Rutas("espera")
    object AsignarTrabajo : Rutas("asignar_trabajo")
    object AsignarOrden : Rutas("asignar_orden")
    object ListaTecnicos : Rutas("lista_tecnicos")
    object OrdenesTrabajo : Rutas("ordenes_trabajo")
    object EjecucionTarea : Rutas("ejecucion_tarea/{ordenId}") {
        fun createRoute(ordenId: String) = "ejecucion_tarea/$ordenId"
    }
    object SubirEvidencia : Rutas("subir_evidencia/{tareaId}") {
        fun createRoute(tareaId: String) = "subir_evidencia/$tareaId"
    }
    object InicioTecnico : Rutas("inicio_tecnico")
}