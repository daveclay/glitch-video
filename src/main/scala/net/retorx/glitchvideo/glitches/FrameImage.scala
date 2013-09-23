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
    def shiftRow(row: Int, distance: Int)
    def shiftCol(row: Int, distance: Int)
    def shift(distanceX: Int, distanceY: Int)
    def shiftColorBand(color: Color, distanceX: Int, distanceY: Int)
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

    def shiftRow(row: Int, distance: Int) {
        val data = getRow(row)
        val shifted = PixelUtils.shift(data, distance)
        setRow(row, shifted)
    }

    def shiftCol(col: Int, distance: Int) {
        val data = getColumn(col)
        val shifted = PixelUtils.shift(data, distance)
        setColumn(col, shifted)
    }

    def shift(distanceX: Int, distanceY: Int) {
        val x = 0
        val nullDest: Array[Int] = null
        if (distanceY != 0) {
            val y = 0
            val pixels = raster.getPixels(x, y, width, height, nullDest)
            val shifted = PixelUtils.shift(pixels, width * distanceY * raster.getNumBands)
            raster.setPixels(x, y, width, height, shifted)
        }

        if (distanceX != 0) {
            for (y <- 0 to height - 1) {
                val samplesAtY = new Array[Int](width * raster.getNumBands)
                raster.getPixels(x, y, width, 1, samplesAtY)

                val shifted = PixelUtils.shift(samplesAtY, raster.getNumBands * distanceX)
                raster.setPixels(x, y, width, 1, shifted)
            }
        }
    }

    def shiftColorBand(color: Color, distanceX: Int, distanceY: Int) {
        val x = 0
        val band = color.getBand

        if (distanceY != 0) {
            val nullDest: Array[Int] = null
            val y = 0
            val allSamples = raster.getSamples(x, y, width, height, band, nullDest)
            val shifted = PixelUtils.shift(allSamples, width * distanceY)
            raster.setSamples(x, y, width, height, band, shifted)
        }

        if (distanceX != 0) {
            for (y <- 0 to height - 1) {
                val samplesAtY = new Array[Int](width)
                raster.getSamples(x, y, width, 1, band, samplesAtY)

                val shifted = PixelUtils.shift(samplesAtY, distanceX)
                raster.setSamples(x, y, width, 1, band, shifted)
            }
        }
    }

    def getWritableRaster: WritableRaster = raster
}

/**
 * Low-memory version of shifting Y - it shifts a scanline to its new position, and moves the scanline at that position
 * to its new position, etc, recursively, until it runs into a scanline it has moved. Then it increments to the next
 * scanline it hasn't done, and continues. Uses two scanline's of pixel data at a time, the sanline being shifted,
 * and the scanline that will be shifted next.
 * @param image
 * @param color
 * @param distanceX
 * @param distanceY
 */
class ColorBandShiftOp(image:FrameImage, color: Color, distanceX: Int, distanceY: Int) {
    val raster = image.getWritableRaster
    val band = color.getBand
    val shiftedYs = new scala.collection.mutable.HashSet[Int]()
    val savedSamples = new Array[Int](image.width)
    val samplesToShift = new Array[Int](image.width)
    val x = 0

    def shift() {
        var y = 0
        // I think once we hit y's that we've already shifted, we've recursively shifted all y's. I think. We shouldn't
        // have to go through all y's, just y's up until we've shifted. For example, if distanceY = 1, then this will
        // only loop once: it will recursively shift each line.
        // TODO: Then again, if the image is 5000 pixels high, this will explode on the stack size.
        while (!shiftedYs.contains(y)) {
            raster.getSamples(x, y, image.width, 1, band, samplesToShift)
            setSamplesAtY(y, samplesToShift)
            y += 1
        }
    }

    private def setSamplesAtY(y: Int, samples: Array[Int]) {
        // where are we putting the copied samples?
        val shiftedY = y + distanceY
        if (!shiftedYs.contains(shiftedY)) {
            // copy the samples at y before we overwrite them
            raster.getSamples(x, y, image.width, 1, band, savedSamples)
        }

        // overwrite those samples at y
        raster.setSamples(x, y, image.width, 1, band, samples)
        // remember we already set this y
        shiftedYs += y
        if (!shiftedYs.contains(shiftedY)) {
            // Can't just send in the same reference to the 1 array, because we're going to copy the original contents
            // into that array on this next recursive call. Copy those elements into the second array, which will
            // then be referenced.
            System.arraycopy(savedSamples, 0, samplesToShift, 0, image.width)
            setSamplesAtY(shiftedY, samplesToShift)
        }
    }
}

