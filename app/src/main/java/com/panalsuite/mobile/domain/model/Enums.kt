package com.panalsuite.mobile.domain.model

enum class Role(val label: String) {
    REQUESTER("Solicitante"),
    RESOLVER("Resolutor"),
    SUPERVISOR("Supervisor")
}

enum class TicketStatus(val label: String) {
    OPEN("Abierto"),
    IN_PROGRESS("En curso"),
    ON_HOLD("En espera"),
    RESOLVED("Resuelto"),
    CLOSED("Cerrado")
}

enum class Priority(val label: String) {
    LOW("Baja"),
    MEDIUM("Media"),
    HIGH("Alta"),
    CRITICAL("Crítica")
}

enum class TicketCategory(val label: String) {
    STORE("Tienda"),
    PHARMACY("Farmacia"),
    SUPPLIER("Proveedor"),
    INFRASTRUCTURE("Infraestructura"),
    INTERNAL_SUPPORT("Soporte interno")
}