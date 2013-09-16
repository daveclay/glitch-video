package net.retorx.glitchvideo.util

import net.retorx.glitchvideo.util.RandomShit._

trait Color {
    def shift(value: Int): Int
    def fuck(r: Int, g: Int, b: Int): Int
    def partiallyFuck(r: Int, g:Int, b: Int): Int

    def unapply(color: Color):Color = color
    def apply(color: Color): Color = color
}

object RED extends Color {
    def shift(value: Int) = value << 16
    def fuck(r: Int, g: Int, b: Int) = {
        shift(randomColorValue()) | g | b
    }

    def partiallyFuck(r: Int, g: Int, b: Int) = {
        (r | shift(randomColorValue())) | g | b
    }
}

object GREEN extends Color {
    def shift(value: Int) = value << 8
    def fuck(r: Int, g: Int, b: Int) = {
        r | shift(randomColorValue())| b
    }

    def partiallyFuck(r: Int, g: Int, b: Int) = {
        r | (g | shift(randomColorValue()))| b
    }
}

object BLUE extends Color {
    def shift(value: Int) = value
    def fuck(r: Int, g: Int, b: Int) = {
        r | g | randomColorValue()
    }

    def partiallyFuck(r: Int, g: Int, b: Int) = {
        r | g | (b | shift(randomColorValue()))
    }
}

