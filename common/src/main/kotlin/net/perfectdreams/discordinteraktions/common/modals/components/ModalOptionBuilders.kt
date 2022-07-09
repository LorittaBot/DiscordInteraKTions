package net.perfectdreams.discordinteraktions.common.modals.components

interface ModalComponentBuilder {
    var allowedLength: ClosedRange<Int>?
    var placeholder: String?
    var value: String?
    var required: Boolean?
    var actionRowNumber: Int
}

interface StringModalComponentBuilder : ModalComponentBuilder