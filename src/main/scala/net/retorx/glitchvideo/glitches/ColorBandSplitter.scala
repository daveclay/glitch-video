package net.retorx.glitchvideo.glitches

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.util.PixelUtils

class ColorBandSplitter(colorBandGlitchers: Array[ColorBandHandler]) extends RandomImageGlitcher {

    override def handleImage(image: BufferedImage) {
        val data = image.getData
        val width = data.getWidth
        val height = data.getHeight

        val dup = PixelUtils.copy(image)

        // TODO: fuck up the entire frame, jump it some place as an entire frame image.

        for (x <- 0 to (width - 1)) {
            for (y <- 0 to (height - 1)) {
                var pixel = image.getRGB(x, y)

                colorBandGlitchers.foreach(glitcher => {
                    val r = pixel & 0xFF0000
                    val g = pixel & 0xFF00
                    val b = pixel & 0xFF

                    pixel = glitcher.handleBands(r, g, b, x, y, image)
                })
                dup.setRGB(x, y, pixel)
            }
        }

        // updateImage(image) // do this if you want that nutty logarithmic effect... though it does end catastrophically
        colorBandGlitchers.foreach(glitcher => {
            glitcher.notifyOfNextFrame()
        })

        val g = image.createGraphics()
        g.drawImage(dup, 0, 0, null)

    }

    def split(pixel: Int) {

    }
}