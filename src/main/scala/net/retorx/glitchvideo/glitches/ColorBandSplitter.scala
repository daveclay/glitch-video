package net.retorx.glitchvideo.glitches

import java.awt.image.BufferedImage

class ColorBandSplitter(colorBandGlitchers: Array[ColorBandHandler]) extends RandomImageGlitcher {

    override def handleImage(image: BufferedImage) {
        val data = image.getData
        val width = data.getWidth
        val height = data.getHeight

        for (x <- 0 to (width - 1)) {
            for (y <- 0 to (height - 1)) {
                var pixel = image.getRGB(x, y)

                colorBandGlitchers.foreach(glitcher => {
                    val r = pixel & 0xFF0000
                    val g = pixel & 0xFF00
                    val b = pixel & 0xFF

                    pixel = glitcher.handleBands(r, g, b, x, y, image)
                })

                image.setRGB(x, y, pixel)
            }
        }

        updateImage(image)
        colorBandGlitchers.foreach(glitcher => {
            glitcher.notifyOfNextFrame()
        })
    }

    def split(pixel: Int) {

    }
}