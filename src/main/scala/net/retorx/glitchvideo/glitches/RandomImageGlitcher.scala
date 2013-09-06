package net.retorx.glitchvideo.glitches

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.util.RandomShit

trait RandomImageGlitcher extends RandomShit {

    def handleImage(image: BufferedImage)

    def updateImage(image: BufferedImage) {
        val g = image.createGraphics()
        g.drawImage(image, 0, 0, null)
    }
}