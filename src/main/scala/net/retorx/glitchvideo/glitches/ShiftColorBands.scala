package net.retorx.glitchvideo.glitches

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.util.RandomShit
import net.retorx.glitchvideo.util.PixelUtils._

class ShiftColorBands extends ColorBandHandler with RandomShit {

    var maxShift = 1

    var shift1 = 0
    var shift2 = 0
    var shift3 = 0
    randomShift()

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

    def handleBands(r: Int, g: Int, b: Int, x: Int, y: Int, image: BufferedImage): Int = {

        val width = image.getWidth
        val height = image.getHeight
        maxShift = height - 1

        var rx = wrap(x + shift1, width)
        var gx = wrap(x + shift2, width)
        var bx = wrap(x + shift3, width)

        var ry = wrap(y + shift1, height)
        var gy = wrap(y + shift2, height)
        var by = wrap(y + shift3, height)

        //(image.getRGB(rx, y) & 0xFF0000) | (image.getRGB(gx, y) & 0xFF00) | (image.getRGB(bx, y) & 0xFF)
        (image.getRGB(x, ry) & 0xFF0000) | (image.getRGB(x, gy) & 0xFF00) | (image.getRGB(x, by) & 0xFF)
    }
}