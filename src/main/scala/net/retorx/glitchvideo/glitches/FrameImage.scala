package net.retorx.glitchvideo.glitches

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.util.{PixelUtils, Color}

trait FrameImage {

    val height: Int
    val width: Int
    def getPixel(x: Int, y: Int): Int
    def setPixel(x: Int, y: Int, pixel: Int)
    def getPixelColor(x: Int, y: Int, color: Color): Byte
    def setPixelColor(x: Int, y: Int, color: Color, pixel: Byte)
    def getColumn(x: Int): Array[Int]
    def setColumn(x: Int, data: Array[Int])
    def getRow(y: Int): Array[Int]
    def setRow(y: Int, data: Array[Int])
    def shiftRow(y: Int, distance: Int)
    def shiftColumn(x: Int, distance: Int)
}

class BufferedFrameImage(image: BufferedImage) extends FrameImage {

    val data = image.getRaster
    val width = data.getWidth
    val height = data.getHeight

    def getPixel(x: Int, y: Int) = image.getRGB(x, y)

    def setPixel(x: Int, y: Int, pixel: Int) {
        image.setRGB(x, y, pixel)
    }

    def getPixelColor(x: Int, y: Int, color: Color) = {
        val pixel = getPixel(x, y)
        color.getColorInPixel(pixel)
    }

    def setPixelColor(x: Int, y: Int, color: Color, value: Byte) {
        val pixel = getPixel(x, y)
        val newPixel = color.set(pixel, value)
        setPixel(x, y, newPixel)
    }

    def getColumn(x: Int) = {
        // Todo: shared array already allocated and re-used
        val y = 0
        val width = 1
        val dest: Array[Int] = null
        data.getPixels(x, y, width, height, dest)
    }

    def setColumn(x: Int, pixels: Array[Int]) {
        val y = 0
        val width = 1
        data.setPixels(x, y, width, height, pixels)
    }

    def getRow(y: Int): Array[Int] = {
        // Todo: shared array already allocated and re-used
        val x = 0
        val height = 1
        val dest: Array[Int] = null
        data.getPixels(x, y, width, height, dest)
    }

    def setRow(y: Int, pixels: Array[Int]) {
        val x = 0
        val height = 1
        data.setPixels(x, y, width, height, pixels)
    }

    def setData(x: Int, y: Int, width: Int, height: Int, pixels: Array[Int]) {
        data.setPixels(x, y, width, height, pixels)
    }

    def getData(x: Int, y: Int, width: Int, height: Int) = {
        val dest: Array[Int] = null
        data.getPixels(x, y, width, height, dest)
    }

    def shiftRow(y: Int, distance: Int) {
        val row = getRow(y)
        val shifted = PixelUtils.shift(row, distance)
        setRow(y, shifted)
    }

    def shiftColumn(x: Int, distance: Int) {}
}

