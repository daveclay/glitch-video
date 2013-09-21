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
        new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR)
        //val cm = image.getColorModel()
        //val isAlphaPremultiplied = cm.isAlphaPremultiplied()
        //val raster = image.copyData(null)
        // new BufferedImage(cm, raster, isAlphaPremultiplied, null)
    }

    def copy(src: Array[Int]) = {
        val copy = new Array[Int](src.length)
        System.arraycopy(src, 0, copy, 0, src.length)
        copy
    }

    def shift(src: Array[Int], distance: Int) = {
        val shifted = new Array[Int](src.length)
        val length = src.length - distance
        System.arraycopy(src, 0, shifted, distance, length)
        System.arraycopy(src, length, shifted, 0, distance)
        shifted
    }
}