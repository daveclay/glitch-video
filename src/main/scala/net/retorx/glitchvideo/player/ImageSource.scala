package net.retorx.glitchvideo.player

import java.awt.image.BufferedImage

trait ImageSource {
    def getImageAt(index: Int): BufferedImage
}