package net.retorx.glitchvideo.glitches.colorbands

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.util.PixelUtils._
import net.retorx.glitchvideo.glitches.routing.PixelGlitcher
import net.retorx.glitchvideo.modulation.{SyncData, ModulatedIntValue}

trait ShiftDirection
object Vertical extends ShiftDirection
object Horizontal extends ShiftDirection

/**
 * A PixelGlitcher that shifts the red, green, and blue pixels in each frame by some distance. By modulating the
 * distance, you can shift either entire frames or individual pixels, causing different types effects.
 *
 * @param shiftDirection
 * @param redShiftAmount
 * @param greenShiftAmount
 * @param blueShiftAmount
 */
class ShiftColorBands(shiftDirection: ShiftDirection = Horizontal,
                      redShiftAmount: ModulatedIntValue,
                      greenShiftAmount: ModulatedIntValue,
                      blueShiftAmount: ModulatedIntValue)
                      extends PixelGlitcher {

    def getFramePixel(r: Int, g: Int, b: Int, x: Int, y: Int, image: BufferedImage): Int = {
        val syncData = new SyncData(0, 0, 0, null.asInstanceOf[Map[String, Long]])
        if (shiftDirection == Horizontal) {
            var ry = wrap(y + redShiftAmount(syncData), height)
            var gy = wrap(y + greenShiftAmount(syncData), height)
            var by = wrap(y + blueShiftAmount(syncData), height)

            (image.getRGB(x, ry) & 0xFF0000) | (image.getRGB(x, gy) & 0xFF00) | (image.getRGB(x, by) & 0xFF)
        } else {
            var rx = wrap(x + redShiftAmount(syncData), width)
            var gx = wrap(x + greenShiftAmount(syncData), width)
            var bx = wrap(x + blueShiftAmount(syncData), width)
            (image.getRGB(rx, y) & 0xFF0000) | (image.getRGB(gx, y) & 0xFF00) | (image.getRGB(bx, y) & 0xFF)
        }
    }

    def notifyOfNextPixel() {}
    def notifyOfNextFrame() {}

}


