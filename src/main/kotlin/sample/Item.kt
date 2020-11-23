package sample

interface Item {
    val title: String
}

data class SubItem(override val title: String) : Item
