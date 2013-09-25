package net.retorx.glitchvideo.glitches

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.util.RandomShit
import net.retorx.glitchvideo.modulation.SyncData

trait RandomImageGlitcher extends RandomShit {

    // updated by the system, so you don't have to.
    var width = 0
    var height = 0

    def handleFrameImage(source: BufferedImage, destination: BufferedImage)
}

trait FrameHandler {
    def calcSyncData(frameImage: FrameImage) = SyncData(frameImage.frameId, 0, 0, null.asInstanceOf[Map[String, Long]])
    def handleFrame(frameImage: FrameImage)
}