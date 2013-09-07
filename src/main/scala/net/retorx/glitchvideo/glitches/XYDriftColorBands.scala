package net.retorx.glitchvideo.glitches

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.util.RandomShit
import net.retorx.glitchvideo.util.PixelUtils._

class XYDriftColorBands extends ColorBandHandler with RandomShit {

    var maxShift = 1
    var shift1 = 0
    var shift2 = 0
    var shift3 = 0
    var rx = 0
    var trigger = false

    def notifyOfNextFrame() {
        shift1 = shift1 + 1
        shift2 = shift2 + 2
        shift3 = shift3 + 4
        trigger = random.nextInt(100) > 90
    }

    def reset() {
        shift1 = 0
        shift2 = 0
        shift3 = 0
    }

    def handleBands(r: Int, g: Int, b: Int, x: Int, y: Int, image: BufferedImage): Int = {

        val width = image.getWidth
        val height = image.getHeight
        maxShift = height
        shift1 = wrap(shift1, height)
        shift2 = wrap(shift2, height)
        shift3 = wrap(shift3, height)

        if (trigger) {
            rx = wrap(x + random.nextInt(width), width) // shift x by some random number
        } else {
            rx = x
        }

        var ry = wrap(y + shift1, height)
        var gy = wrap(y + shift2, height)
        var by = wrap(y + shift3, height)

        try {
            val r = image.getRGB(rx, ry) & 0xFF0000
            val g = image.getRGB(x, gy) & 0xFF00
            val b = image.getRGB(x, by) & 0xFF
            r | g | b
        } catch {
            case e:Exception => throw new RuntimeException("Coordinates " + rx + ", " + ry + " or " + gy + " or " + by + " is out ouf bounds for image " + width + "x" + height)
        }
    }
}