package net.retorx.glitchvideo.glitches.colorbands

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.util.PixelUtils._
import net.retorx.glitchvideo.util.{GREEN, RED, Color, RandomShit}
import net.retorx.glitchvideo.glitches.routing.PixelGlitcher

class NoiseLinesSingleColorBands(thickness: Int = 1, colorBand: Color = RED) extends PixelGlitcher with RandomShit {

    var line = 0

    def notifyOfNextFrame() {
        line += 1
    }

    def getFramePixel(r: Int, g: Int, b: Int, x: Int, y: Int, image: BufferedImage): Int = {
        if (line == image.getHeight) {
            line = 0
        }

        if (line >= y && line <= (y + thickness)) {
            colorBand.partiallyFuck(r, g, b)
        } else {
            r | g | b
        }
    }
}