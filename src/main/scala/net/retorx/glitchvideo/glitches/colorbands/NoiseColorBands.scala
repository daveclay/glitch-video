package net.retorx.glitchvideo.glitches.colorbands

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.util.RandomShit
import net.retorx.glitchvideo.util.PixelUtils._
import net.retorx.glitchvideo.glitches.routing.PixelGlitcher

class NoiseColorBands extends PixelGlitcher with RandomShit {

    def fucking() = {
        random.nextInt(100) > 80
    }

    def notifyOfNextFrame() {

    }

    def getFramePixel(r: Int, g: Int, b: Int, x: Int, y: Int, image: BufferedImage): Int = {
        if (fucking()) {
            val fuckedR = r | (random.nextInt(255) << 16)
            fuckedR | g | b
        } else {
            r | g | b
        }
    }
}