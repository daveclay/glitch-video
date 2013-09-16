package net.retorx.glitchvideo.glitches.colorbands

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.util.RandomShit
import net.retorx.glitchvideo.util.PixelUtils._
import net.retorx.glitchvideo.glitches.routing.PixelGlitcher

trait ShiftDirection
object Vertical extends ShiftDirection
object Horizontal extends ShiftDirection

abstract class AbstractShiftColorBands(shiftDirection: ShiftDirection = Horizontal) extends PixelGlitcher with RandomShit {

    var maxShift = 1

    var shift1 = 0
    var shift2 = 0
    var shift3 = 0
    notifyOfNextFrame()

    def getFramePixel(r: Int, g: Int, b: Int, x: Int, y: Int, image: BufferedImage): Int = {
        notifyOfNextPixel()
        maxShift = height - 1
        if (shiftDirection == Horizontal) {
            var ry = wrap(y + shift1, height)
            var gy = wrap(y + shift2, height)
            var by = wrap(y + shift3, height)

            (image.getRGB(x, ry) & 0xFF0000) | (image.getRGB(x, gy) & 0xFF00) | (image.getRGB(x, by) & 0xFF)
        } else {
            var rx = wrap(x + shift1, width)
            var gx = wrap(x + shift2, width)
            var bx = wrap(x + shift3, width)
            (image.getRGB(rx, y) & 0xFF0000) | (image.getRGB(gx, y) & 0xFF00) | (image.getRGB(bx, y) & 0xFF)
        }
    }

    def notifyOfNextPixel() { }
}

class PeriodicallyRandomlyShiftFrameColorBands(shiftDirection: ShiftDirection = Horizontal) extends AbstractShiftColorBands(shiftDirection) with RandomShit {

    def randomShift() {
        shift1 = random.nextInt(maxShift)
        shift2 = random.nextInt(maxShift)
        shift3 = random.nextInt(maxShift)
    }

    def notifyOfNextFrame() {
        if (random.nextInt(100) > 92) {
            randomShift()
        }
    }
}

/**
 * weight: 99900: roughly 1 to 10 pixel wide strips
 * weight: 99990: roughly 10 to 100 pixel wide strips
 * @param weight
 * @param shiftDirection
 */
class RandomlyShiftColorBands(weight:Int = 99900, shiftDirection: ShiftDirection = Horizontal) extends PeriodicallyRandomlyShiftFrameColorBands(shiftDirection) with RandomShit {

    override def notifyOfNextPixel() {
        if (random.nextInt(100000) > weight) {
            randomShift()
        }
    }
}

class ProgressivelyShiftColorBands extends AbstractShiftColorBands with RandomShit {

    def progressiveShift() {
        shift1 += 1
        shift2 += 1
        shift3 += 1
    }

    def notifyOfNextFrame() {
        progressiveShift()
    }
}


