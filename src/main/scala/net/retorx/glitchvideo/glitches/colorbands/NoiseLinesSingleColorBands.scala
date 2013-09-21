package net.retorx.glitchvideo.glitches.colorbands

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.util.PixelUtils._
import net.retorx.glitchvideo.util.{GREEN, RED, Color, RandomShit}
import net.retorx.glitchvideo.glitches.routing.PixelGlitcher

class NoiseLinesSingleColorBands(lineThickness: Int = 1, colorBand: Color = RED) extends PixelGlitcher with RandomShit {

    var line = 0
    var thickness = 0

    def notifyOfNextFrame() {
        line += 1
        thickness = getThickness
    }

    def getFramePixel(r: Int, g: Int, b: Int, x: Int, y: Int, image: BufferedImage): Int = {
        if (line == image.getHeight) {
            line = 0
        }

        if (line >= y && line <= (y + thickness)) {
            colorBand.partiallyFuck(r.toByte, g.toByte, b.toByte)
        } else {
            r | g | b
        }
    }

    def getThickness = thickness
}