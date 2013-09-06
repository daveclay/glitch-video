package net.retorx.glitchvideo.glitches

import java.awt.image.BufferedImage

trait ColorBandHandler {
    def notifyOfNextFrame()
    def handleBands(r: Int, g: Int, b: Int, x: Int, y: Int, image: BufferedImage): Int
}