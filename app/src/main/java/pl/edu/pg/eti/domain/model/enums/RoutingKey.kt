package pl.edu.pg.eti.domain.model.enums

enum class RoutingKey(val key: String) {
    TO_SERVER("S.X"),
    TO_CLIENT("X.C"),
    TO_ALL("S.C")
}