package net.retorx.glitchvideo.glitches

import java.awt.image.{WritableRaster, BufferedImage}
import net.retorx.glitchvideo.util.{PixelUtils, Color}
import net.retorx.glitchvideo.glitches.colorbands.ShiftDirection
import java.awt.geom.AffineTransform

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
    def getData(x: Int, y: Int, width: Int, height: Int): Array[Int]
    def setData(x: Int, y: Int, width: Int, height: Int, pixels: Array[Int])
    def getWritableRaster: WritableRaster
}

class BufferedFrameImage(image: BufferedImage) extends FrameImage {

    val raster = image.getRaster
    val width = raster.getWidth
    val height = raster.getHeight

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
        raster.getPixels(x, y, width, height, dest)
    }

    def setColumn(x: Int, pixels: Array[Int]) {
        val y = 0
        val width = 1
        raster.setPixels(x, y, width, height, pixels)
    }

    def getRow(y: Int): Array[Int] = {
        // Todo: shared array already allocated and re-used
        val x = 0
        val height = 1
        val dest: Array[Int] = null
        raster.getPixels(x, y, width, height, dest)
    }

    def setRow(y: Int, pixels: Array[Int]) {
        val x = 0
        val height = 1
        raster.setPixels(x, y, width, height, pixels)
    }

    def setData(x: Int, y: Int, width: Int, height: Int, pixels: Array[Int]) {
        raster.setPixels(x, y, width, height, pixels)
    }

    def getData(x: Int, y: Int, width: Int, height: Int) = {
        val dest: Array[Int] = null
        raster.getPixels(x, y, width, height, dest)
    }

    def shiftRow(y: Int, distance: Int) {
        val row = getRow(y)
        val shifted = PixelUtils.shift(row, distance)
        // Note: this does not work, so we can't just modify the array in place.
        // System.arraycopy(shifted, 0, row, 0, shifted.length)
        setRow(y, shifted)
    }

    def shiftColumn(x: Int, distance: Int) {
        val col = getColumn(x)
        val shifted = PixelUtils.shift(col, distance)
        setColumn(x, shifted)
    }

    def shiftColorBand(color: Color, distanceX: Int, distanceY: Int) {
        val x = 0
        val y = 0
        val band = color.getBand
        val nullDest: Array[Int] = null
        // hrm, weird, this is the entire image, shifted on the x axis. I'd have to re-sort the samples to do the y axis.
        val samples = raster.getSamples(x, y, width, height, band, nullDest)
        // val samples = data.getSampleModel.getSamples(x, y, width, height, band, nullDest, data.getDataBuffer)
        val dest = PixelUtils.shift(samples, distanceX)
        raster.setSamples(x, y, width, height, band, dest)
    }

    def getWritableRaster: WritableRaster = raster
}

