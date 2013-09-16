package net.retorx.glitchvideo.glitches.colorbands

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.util.{RED, Color, RandomShit}
import net.retorx.glitchvideo.glitches.routing.PixelGlitcher

class RandomNoiseLinesSingleColorBands(colorBand: Color = RED) extends NoiseLinesSingleColorBands(0, colorBand) {

    override def notifyOfNextFrame() {
        super.notifyOfNextFrame()
        thickness = getThickness
    }

    override def getThickness: Int = random.nextInt(100)
}