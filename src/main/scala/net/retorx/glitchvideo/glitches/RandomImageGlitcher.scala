package net.retorx.glitchvideo.glitches

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.util.RandomShit

trait RandomImageGlitcher extends RandomShit {

    // updated by the system, so you don't have to.
    var width = 0
    var height = 0

    def handleFrameImage(source: BufferedImage, destination: BufferedImage)
}