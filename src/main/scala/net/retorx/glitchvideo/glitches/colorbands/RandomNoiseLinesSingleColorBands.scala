package net.retorx.glitchvideo.glitches.colorbands

import net.retorx.glitchvideo.util.{RED, Color, RandomShit}

class RandomNoiseLinesSingleColorBands(colorBand: Color = RED) extends NoiseLinesSingleColorBands(0, colorBand) {

    override def notifyOfNextFrame() {
        super.notifyOfNextFrame()
        thickness = getThickness
    }

    override def getThickness: Int = random.nextInt(100)
}