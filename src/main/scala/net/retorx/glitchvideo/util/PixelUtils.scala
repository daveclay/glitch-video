package net.retorx.glitchvideo.util

import java.awt.image.BufferedImage

object PixelUtils {

    def main(args: Array[String]) {
        println(wrap(359, 360))
        println(wrap(360, 360))
        println(wrap(361, 360))
        println(wrap(0, 300))
        println(wrap(1, 300))
    }

    def wrap(value: Int, max: Int) = {
        if (value >= max) {
            value - max
        } else {
            value
        }
    }

    def copyImage(image:BufferedImage) = {
        val cm = image.getColorModel()
        val isAlphaPremultiplied = cm.isAlphaPremultiplied()
        val raster = image.copyData(null)
        new BufferedImage(cm, raster, isAlphaPremultiplied, null)
    }
}