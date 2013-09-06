package net.retorx.glitchvideo.util

import java.awt.image.BufferedImage

object PixelUtils {

    def main(args: Array[String]) {
        println(wrap(360, 359))
        println(wrap(360, 360))
        println(wrap(360, 361))
    }

    def wrap(value: Int, max: Int) = {
        if (value >= max) {
            value - max
        } else {
            value
        }
    }

    def copy(image:BufferedImage) = {
        val cm = image.getColorModel()
        val isAlphaPremultiplied = cm.isAlphaPremultiplied()
        val raster = image.copyData(null)
        new BufferedImage(cm, raster, isAlphaPremultiplied, null)
    }
}