package net.retorx.glitchvideo.glitches.routing

import java.awt.image.BufferedImage
import scala.collection.immutable._
import net.retorx.glitchvideo.glitches.RandomImageGlitcher

class ApplyAll(glitchers: List[RandomImageGlitcher]) extends RandomImageGlitcher {

    def handleFrameImage(source: BufferedImage, destination: BufferedImage) {
        glitchers.foreach(glitcher => {
            glitcher.width = width
            glitcher.height = height
            glitcher.handleFrameImage(source, destination)
        })
    }
}