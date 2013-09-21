package net.retorx.glitchvideo.util

import net.retorx.glitchvideo.util.RandomShit._

trait Color {
    def shiftValueToPixel(value: Byte): Int
    def getColorInPixel(rgb: Int): Byte
    def set(rgb: Int, value: Byte): Int
    def fuck(r: Byte, g: Byte, b: Byte): Int
    def partiallyFuck(r: Byte, g:Byte, b: Byte): Int

    def unapply(color: Color):Color = color
    def apply(color: Color): Color = color
}

object RED extends Color {

    def getColorInPixel(rgb: Int) = (rgb & 0xff).toByte

    def shiftValueToPixel(value: Byte) = value << 16

    def set(rgb: Int, value: Byte) = {
        shiftValueToPixel(value) | (rgb & 0xff00) | (rgb & 0xff)
    }

    def fuck(r: Byte, g: Byte, b: Byte) = {
        shiftValueToPixel(randomColorValue()) | g | b
    }

    def partiallyFuck(r: Byte, g: Byte, b: Byte) = {
        (r | shiftValueToPixel(randomColorValue())) | g | b
    }
}

object GREEN extends Color {

    def getColorInPixel(rgb: Int) = ((rgb & 0xff00) >> 8).toByte

    def shiftValueToPixel(value: Byte) = value << 8

    def set(rgb: Int, value: Byte) = {
        (rgb & 0xff0000) | shiftValueToPixel(value) | (rgb & 0xff)
    }

    def fuck(r: Byte, g: Byte, b: Byte) = {
        r | shiftValueToPixel(randomColorValue())| b
    }

    def partiallyFuck(r: Byte, g: Byte, b: Byte) = {
        r | (g | shiftValueToPixel(randomColorValue()))| b
    }
}

object BLUE extends Color {

    def getColorInPixel(rgb: Int) = ((rgb & 0xff0000) >> 16).toByte

    def shiftValueToPixel(value: Byte) = value

    def set(rgb: Int, value: Byte) = {
        (rgb & 0xff0000) | (rgb & 0xff00) | value
    }

    def fuck(r: Byte, g: Byte, b: Byte) = {
        r | g | randomColorValue()
    }

    def partiallyFuck(r: Byte, g: Byte, b: Byte) = {
        r | g | (b | shiftValueToPixel(randomColorValue()))
    }
}

