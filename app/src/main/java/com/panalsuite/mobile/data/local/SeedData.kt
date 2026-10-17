package com.panalsuite.mobile.data.local

import com.panalsuite.mobile.data.local.entity.CommentEntity
import com.panalsuite.mobile.data.local.entity.TicketEntity
import com.panalsuite.mobile.data.local.entity.TicketHistoryEntity
import com.panalsuite.mobile.data.local.entity.UserEntity
import com.panalsuite.mobile.domain.model.Priority
import com.panalsuite.mobile.domain.model.Role
import com.panalsuite.mobile.domain.model.TicketCategory
import com.panalsuite.mobile.domain.model.TicketStatus
import com.panalsuite.mobile.util.PasswordHasher


object SeedData {
    private const val DEMO_PASSWORD = "Demo1234"
    private const val HOUR_MS = 60L * 60 * 1000
    private const val DAY_MS = 24L * HOUR_MS
    private const val TEAM_SUPPORT = "Soporte TI"
    private const val TEAM_INFRASTRUCTURE = "Infraestructura"

    fun users(): List<UserEntity> {
        val passwordHash = PasswordHasher.hash(DEMO_PASSWORD)
        return listOf(
            UserEntity(
                id = 1,
                fullName = "Camila Rojas",
                email = "camila.rojas@panalsuite.test",
                passwordHash = passwordHash,
                role = Role.REQUESTER,
                team = "Tiendas",
            ),
            UserEntity(
                id = 2,
                fullName = "Javiera Mora",
                email = "javiera.mora@panalsuite.test",
                passwordHash = passwordHash,
                role = Role.REQUESTER,
                team = "Farmacia"
            ),
            UserEntity(
                id = 3,
                fullName = "Diego Fuentes",
                email = "diego.fuentes@panalsuite.test",
                passwordHash = passwordHash,
                role = Role.RESOLVER,
                team = TEAM_SUPPORT
            ),
            UserEntity(
                id = 4,
                fullName = "Valentina Soto",
                email = "valentina.soto@panalsuite.test",
                passwordHash = passwordHash,
                role = Role.RESOLVER,
                team = TEAM_INFRASTRUCTURE
            ),
            UserEntity(
                id = 5,
                fullName = "Matías Pizarro",
                email = "matias.pizarro@panalsuite.test",
                passwordHash = passwordHash,
                role = Role.SUPERVISOR,
                team = "Operaciones"
            )
        )
    }

    fun tickets(now: Long): List<TicketEntity> = listOf(
        TicketEntity(
            id = 1,
            title = "POS de la caja 3 no enciende",
            description = "La caja 3 no responde al presionar el botón de encendido desde la apertura.",
            category = TicketCategory.STORE,
            priority = Priority.HIGH,
            status = TicketStatus.IN_PROGRESS,
            branch = "Tienda Maipú",
            requesterId = 1,
            assigneeId = 3,
            team = TEAM_SUPPORT,
            createdAt = now - 2 * DAY_MS,
            updatedAt = now - DAY_MS
        ),
        TicketEntity(
            id = 2,
            title = "Sin conexión a internet en la sucursal",
            description = "Desde las 9:00 no hay red Wi-Fi ni cableada y no se pueden validar recetas.",
            category = TicketCategory.INFRASTRUCTURE,
            priority = Priority.CRITICAL,
            status = TicketStatus.OPEN,
            branch = "Farmacia Providencia",
            requesterId = 2,
            assigneeId = null,
            team = TEAM_INFRASTRUCTURE,
            createdAt = now - 3 * HOUR_MS,
            updatedAt = now - 3 * HOUR_MS
        ),
        TicketEntity(
            id = 3,
            title = "Error al emitir boleta electrónica",
            description = "Al cerrar la venta aparece un error de conexión con el servicio de boletas.",
            category = TicketCategory.STORE,
            priority = Priority.MEDIUM,
            status = TicketStatus.ON_HOLD,
            branch = "Tienda Maipú",
            requesterId = 1,
            assigneeId = 3,
            team = TEAM_SUPPORT,
            createdAt = now - 4 * DAY_MS,
            updatedAt = now - 2 * DAY_MS
        ),
        TicketEntity(
            id = 4,
            title = "Pedido de proveedor sin confirmar",
            description = "El pedido de reposición figura pendiente hace tres días sin respuesta.",
            category = TicketCategory.SUPPLIER,
            priority = Priority.LOW,
            status = TicketStatus.RESOLVED,
            branch = "Bodega Quilicura",
            requesterId = 2,
            assigneeId = 3,
            team = TEAM_SUPPORT,
            createdAt = now - 5 * DAY_MS,
            updatedAt = now - DAY_MS
        ),
        TicketEntity(
            id = 5,
            title = "Refrigerador de farmacia con falla de temperatura",
            description = "El refrigerador de medicamentos marca sobre 10 grados desde la mañana.",
            category = TicketCategory.PHARMACY,
            priority = Priority.CRITICAL,
            status = TicketStatus.IN_PROGRESS,
            branch = "Farmacia Providencia",
            requesterId = 2,
            assigneeId = 4,
            team = TEAM_INFRASTRUCTURE,
            createdAt = now - DAY_MS,
            updatedAt = now - 5 * HOUR_MS
        ),
        TicketEntity(
            id = 6,
            title = "Solicitud de acceso al sistema de inventario",
            description = "Se requiere acceso de consulta al inventario para una nueva jefa de turno.",
            category = TicketCategory.INTERNAL_SUPPORT,
            priority = Priority.LOW,
            status = TicketStatus.OPEN,
            branch = "Oficina Central",
            requesterId = 1,
            assigneeId = null,
            team = TEAM_SUPPORT,
            createdAt = now - DAY_MS,
            updatedAt = now - DAY_MS
        ),
        TicketEntity(
            id = 7,
            title = "Impresora de etiquetas sin tinta",
            description = "La impresora de etiquetas de precios dejó de imprimir.",
            category = TicketCategory.STORE,
            priority = Priority.MEDIUM,
            status = TicketStatus.CLOSED,
            branch = "Tienda Maipú",
            requesterId = 1,
            assigneeId = 3,
            team = TEAM_SUPPORT,
            createdAt = now - 7 * DAY_MS,
            updatedAt = now - 5 * DAY_MS
        )
    )

    fun comments(now: Long): List<CommentEntity> = listOf(
        CommentEntity(
            ticketId = 1,
            authorId = 3,
            message = "Revisando el cable de poder y la fuente de la caja.",
            createdAt = now - DAY_MS - 2 * HOUR_MS
        ),
        CommentEntity(
            ticketId = 1,
            authorId = 1,
            message = "Gracias, la caja sigue apagada. Hoy atendemos con las otras dos.",
            createdAt = now - DAY_MS
        ),
        CommentEntity(
            ticketId = 3,
            authorId = 3,
            message = "Esperando respuesta del proveedor del servicio de boletas.",
            createdAt = now - 2 * DAY_MS
        ),
        CommentEntity(
            ticketId = 4,
            authorId = 3,
            message = "El proveedor confirmó el pedido por correo.",
            createdAt = now - DAY_MS
        ),
        CommentEntity(
            ticketId = 5,
            authorId = 4,
            message = "Técnico en camino a la farmacia para revisar el compresor.",
            createdAt = now - 5 * HOUR_MS
        ),
        CommentEntity(
            ticketId = 7,
            authorId = 3,
            message = "Se reemplazó el rollo de etiquetas y la impresora funciona.",
            createdAt = now - 5 * DAY_MS
        )
    )

    fun history(now: Long): List<TicketHistoryEntity> = listOf(
        TicketHistoryEntity(
            ticketId = 1,
            authorId = 1,
            description = "Ticket creado",
            createdAt = now - 2 * DAY_MS
        ),
        TicketHistoryEntity(
            ticketId = 1,
            authorId = 3,
            description = "Estado cambiado de Abierto a En curso",
            createdAt = now - DAY_MS
        ),
        TicketHistoryEntity(
            ticketId = 3,
            authorId = 3,
            description = "Estado cambiado de En curso a En espera",
            createdAt = now - 2 * DAY_MS
        ),
        TicketHistoryEntity(
            ticketId = 4,
            authorId = 3,
            description = "Estado cambiado de En curso a Resuelto",
            createdAt = now - DAY_MS
        ),
        TicketHistoryEntity(
            ticketId = 5,
            authorId = 5,
            description = "Responsable asignado: Valentina Soto",
            createdAt = now - DAY_MS
        ),
        TicketHistoryEntity(
            ticketId = 7,
            authorId = 3,
            description = "Estado cambiado de Resuelto a Cerrado",
            createdAt = now - 5 * DAY_MS
        )
    )
}