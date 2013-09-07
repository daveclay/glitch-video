package net.retorx.glitchvideo.glitches.routing

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.util.PixelUtils._
import net.retorx.glitchvideo.util.PixelUtils
import net.retorx.glitchvideo.glitches.{RandomImageGlitcher}

class ApplyAllColorBands(colorBandGlitchers: Array[ColorBandHandler]) extends RandomImageGlitcher {

    override def handleImage(image: BufferedImage) {
        val dup = PixelUtils.copy(image)

        if (random.nextInt(1000) > 950) {
            jump(image, dup)
        } else {
            fuckColor(image, dup)
        }

        // updateImage(image) // do this if you want that nutty logarithmic effect... though it does end catastrophically
        colorBandGlitchers.foreach(glitcher => {
            glitcher.notifyOfNextFrame()
        })

        val g = image.createGraphics()
        g.drawImage(dup, 0, 0, null)

    }

    def jump(image:BufferedImage, dup: BufferedImage) = {
        for (x <- 0 to (width - 1)) {
            for (y <- 0 to (height - 1)) {
                var pixel = image.getRGB(x, y)
                var tx = wrap(x + width / 2, width)
                var ty = wrap(y + height / 2, height)
                dup.setRGB(tx, ty, pixel & 0x92ffff)
            }
        }
    }

    def fuckColor(image:BufferedImage, dup: BufferedImage) = {

        for (x <- 0 to (width - 1)) {
            for (y <- 0 to (height - 1)) {
                var pixel = image.getRGB(x, y)

                colorBandGlitchers.foreach(glitcher => {
                    glitcher.height = height
                    glitcher.width = width

                    val r = pixel & 0xFF0000
                    val g = pixel & 0xFF00
                    val b = pixel & 0xFF

                    pixel = glitcher.handleBands(r, g, b, x, y, image)

                })
                dup.setRGB(x, y, pixel)
            }
        }

    }

    def split(pixel: Int) {

    }
}