package net.retorx.glitchvideo.glitches.routing

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.glitches.RandomImageGlitcher

class ApplyAllColorBands(colorBandGlitchers: Array[PixelGlitcher]) extends RandomImageGlitcher {

    def handleFrameImage(source: BufferedImage, destination: BufferedImage) {

        colorBandGlitchers.foreach(glitcher => {
            glitcher.height = height
            glitcher.width = width
            glitcher.notifyOfNextFrame()
        })

        for (x <- 0 to (width - 1)) {
            for (y <- 0 to (height - 1)) {
                var pixel = source.getRGB(x, y)

                colorBandGlitchers.foreach(glitcher => {

                    val r = pixel & 0xFF0000
                    val g = pixel & 0xFF00
                    val b = pixel & 0xFF

                    pixel = glitcher.getFramePixel(r, g, b, x, y, source)

                })
                destination.setRGB(x, y, pixel)
            }
        }
    }
}