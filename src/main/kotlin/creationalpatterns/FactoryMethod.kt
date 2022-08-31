package creationalpatterns

// Let's discuss how to create objects without using a constructor directly
// The Factory Method Design Pattern

class FactoryMethod {
    fun createPiece(rank: Char): ChessPiece {
        return when(rank) {
            'q' -> Queen(rank)
            'p' -> Pawn(rank)
            else -> throw RuntimeException("Unknown piece: $rank")
        }
    }
}

interface ChessPiece {
    val rank: Char
}

data class Pawn(override val rank: Char): ChessPiece
data class Queen(override val rank: Char): ChessPiece

