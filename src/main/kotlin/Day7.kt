import java.lang.IllegalArgumentException

private data class CamelCards(val hand: String, val bid: Int, val type: TYPES = TYPES.Unknown)
private val myCards = mutableListOf<CamelCards>()
private enum class TYPES {
    FiveOfAKind, FourOfAKind, FullHouse, ThreeOfAKind, TwoPair, OnePair, HighCard, Unknown
}

class Day7 {

    fun solve(considerJokers: Boolean): Int {
        execFileByLine("./src/main/kotlin/input7.txt") {
            val split = it.split(" ")
            myCards.add(
                CamelCards(
                    split.first(),
                    split.last().toInt(),
                    split.first().toType(considerJokers)
                )
            )
        }
        return myCards
            .sortedWith(higherFirstCardComparator(considerJokers))
            .sortedBy { it.type }
            .reversed()
            .mapIndexed { index, camelCards ->
                camelCards.bid * (index + 1)
            }
            .sum()
    }

    private fun String.toType(considerJokers: Boolean = false): TYPES {
        val groups = this.toCharArray().groupBy { it }
        val amountPerCard = mutableMapOf<Char, Int>()
        groups.map {
            amountPerCard[it.key] = it.value.size
        }
        if (considerJokers) {
            replaceJokersForHighestAmountOfOtherCard(amountPerCard)
        }

        return when (amountPerCard.size) {
            1 -> TYPES.FiveOfAKind
            2 -> {
                if (amountPerCard.values.any { it == 1 }) {
                    return TYPES.FourOfAKind
                }
                return TYPES.FullHouse
            }
            3 -> {
                if (amountPerCard.values.any { it == 3 }) {
                    return TYPES.ThreeOfAKind
                }
                return TYPES.TwoPair
            }
            4 -> TYPES.OnePair
            else -> TYPES.HighCard
        }
    }

    private fun replaceJokersForHighestAmountOfOtherCard(amountPerCard: MutableMap<Char, Int>) {
        val numberOfJ = amountPerCard['J']
        if (numberOfJ != null && numberOfJ != 5) {
            val maxNonJEntry = amountPerCard
                .filter { it.key != 'J' }
                .maxBy { it.value }
            amountPerCard[maxNonJEntry.key] = amountPerCard[maxNonJEntry.key]!! + numberOfJ
            amountPerCard.remove('J')
        }
    }

    private fun higherFirstCardComparator(considerJokers: Boolean = false) =
        Comparator<CamelCards> { a, b ->
            val arrA = a.hand.toCharArray()
            val arrB = b.hand.toCharArray()
            var idx = -1

            // get first non-matching card index
            for (i in 0..arrA.lastIndex) {
                if (arrA[i] != arrB[i]) {
                    idx = i
                    break
                }
            }

            if (arrA[idx].card2Rank(considerJokers) < arrB[idx].card2Rank(considerJokers)) {
                -1
            } else {
                1
            }
        }

    private fun Char.card2Rank(considerJokers: Boolean = false): Int {
        return when (this) {
            'A' -> 0
            'K' -> 1
            'Q' -> 3
            'J' -> {
                if (!considerJokers) 4 else 14
            }
            'T' -> 5
            '9' -> 6
            '8' -> 7
            '7' -> 8
            '6' -> 9
            '5' -> 10
            '4' -> 11
            '3' -> 12
            '2' -> 13
            else -> {
                throw IllegalArgumentException("Not possible")
            }
        }
    }
}