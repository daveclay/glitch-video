package net.retorx.glitchvideo.glitches.colorbands

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.util.{RED, Color, RandomShit}
import net.retorx.glitchvideo.glitches.routing.PixelGlitcher

class RandomNoiseLinesSingleColorBands(thickness: Int = 1, colorBand: Color = RED) extends NoiseLinesSingleColorBands {

    override def getFramePixel(r: Int, g: Int, b: Int, x: Int, y: Int, image: BufferedImage): Int = {
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