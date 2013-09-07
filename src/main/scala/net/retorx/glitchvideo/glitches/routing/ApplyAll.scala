package net.retorx.glitchvideo.glitches.routing

import java.awt.image.BufferedImage
import scala.collection.immutable._
import net.retorx.glitchvideo.glitches.RandomImageGlitcher

class ApplyAll(glitchers: List[RandomImageGlitcher]) extends RandomImageGlitcher {

    def handleImage(image: BufferedImage) {
        glitchers.foreach(glitcher => {
            glitcher.width = width
            glitcher.height = height
            glitcher.handleImage(image)
        })
    }
}