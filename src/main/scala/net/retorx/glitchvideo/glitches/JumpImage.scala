package net.retorx.glitchvideo.glitches

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.util.PixelUtils._

class JumpImage extends RandomImageGlitcher {

    def handleFrameImage(source: BufferedImage, destination: BufferedImage) {
        for (x <- 0 to (width - 1)) {
            for (y <- 0 to (height - 1)) {
                var pixel = source.getRGB(x, y)
                var tx = wrap(x + width / 2, width)
                var ty = wrap(y + height / 2, height)
                destination.setRGB(tx, ty, pixel & 0x92ffff)
            }
        }
    }
}