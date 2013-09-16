package net.retorx.glitchvideo.glitches.routing

import java.awt.image.BufferedImage

trait PixelGlitcher {

    var width: Int = 0
    var height: Int = 0

    def notifyOfNextFrame()

    def getFramePixel(r: Int, g: Int, b: Int, x: Int, y: Int, image: BufferedImage): Int
}