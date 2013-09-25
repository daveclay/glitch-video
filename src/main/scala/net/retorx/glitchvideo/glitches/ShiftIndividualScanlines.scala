package net.retorx.glitchvideo.glitches

import net.retorx.glitchvideo.glitches.colorbands.{Horizontal, ShiftDirection}
import net.retorx.glitchvideo.modulation.{SyncData, ModulatedIntValue}
import net.retorx.glitchvideo.util._

class ShiftIndividualScanlines(shiftDirection: ShiftDirection = Horizontal,
                       redShiftAmount: ModulatedIntValue,
                       greenShiftAmount: ModulatedIntValue,
                       blueShiftAmount: ModulatedIntValue)
        extends FrameHandler {

    def handleFrame(frameImage: FrameImage) {
        new ShiftBandsAlgorithm(calcSyncData(frameImage), frameImage, BLUE, 0, blueShiftAmount).shift()
    }
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
class ShiftBandsAlgorithm(syncData: SyncData,
                          image: FrameImage,
                          color: Color,
                          distanceX: Int,
                          distanceY: ModulatedIntValue) {

    val raster = image.getWritableRaster
    val band = color.getBand
    val shiftedYs = new scala.collection.mutable.HashSet[Int]()
    val savedSamples = new Array[Int](image.width)
    val samplesToShift = new Array[Int](image.width)
    val x = 0

    def shift() {
        var y = 0
        var nextY = 0
        raster.getSamples(x, y, image.width, 1, band, samplesToShift)
        while (shiftedYs.size != raster.getHeight) {
            // where are we putting the copied samples?
            val shiftedY = PixelUtils.wrap(nextY + distanceY(syncData), image.height)
            // have we already shifted those samples?
            if (!shiftedYs.contains(shiftedY)) {
                // copy the samples at the shiftedY location before we overwrite them
                raster.getSamples(x, shiftedY, image.width, 1, band, savedSamples)
                // overwrite those samples at shiftedY
                raster.setSamples(x, shiftedY, image.width, 1, band, samplesToShift)
                // remember we already set this y
                shiftedYs += shiftedY
                // set the next samples to shift to the ones that were at shiftedY, those will be the next samples
                // that we shift out
                System.arraycopy(savedSamples, 0, samplesToShift, 0, image.width)
                nextY = shiftedY
            } else {
                // increment.
                y += 1
                nextY = y
            }
        }
    }
}


