package net.retorx.glitchvideo.glitches.routing

import java.awt.image.BufferedImage

trait ColorBandHandler {

    var width: Int = 0
    var height: Int = 0

    def notifyOfNextFrame()

    def handleBands(r: Int, g: Int, b: Int, x: Int, y: Int, image: BufferedImage): Int
}