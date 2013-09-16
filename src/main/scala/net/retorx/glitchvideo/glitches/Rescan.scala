package net.retorx.glitchvideo.glitches

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.util.PixelUtils

class Rescan extends RandomImageGlitcher {

    var framesToShift = 0
    var frameCount = 0
    var shiftAmount = 0

    def incrementCounters() {
        if (frameCount == 0) {
            framesToShift = random.nextInt(30)
            shiftAmount = random.nextInt(height)
            frameCount = frameCount + 1
        } else if  (frameCount == framesToShift) {
            frameCount = 0
        } else {
            frameCount = frameCount + 1
        }
    }

    override def handleFrameImage(image: BufferedImage, destination: BufferedImage) {
        incrementCounters()

        val fadeOriginal = random.nextBoolean()

        // 640 x 360
        for (x <- 0 to (width - 1)) {
            for (y <- 0 to (height - 1)) {
                val originalPixel = image.getRGB(x, y)
                val ty = PixelUtils.wrap(shiftAmount + y, height)
                val pixel = image.getRGB(x, ty)
                if (fadeOriginal) {
                    destination.setRGB(x, y, originalPixel | pixel)
                } else {
                    destination.setRGB(x, y, pixel)
                }
            }
        }
    }
}