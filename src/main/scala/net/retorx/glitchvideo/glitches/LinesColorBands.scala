package net.retorx.glitchvideo.glitches

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.util.RandomShit

class LinesColorBands extends ColorBandHandler with RandomShit {

    var line = 0;

    def fucking() = {
        random.nextInt(100) > 80
    }

    def notifyOfNextFrame() {
        line += 1
    }

    def handleBands(r: Int, g: Int, b: Int, x: Int, y: Int, image: BufferedImage): Int = {
        if (line == image.getHeight) {
            line = 0
        }
        if (line == y) {
            val fuckedR = r | (random.nextInt(255) << 16)
            fuckedR | g | b
        } else {
            r | g | b
        }
    }
}