package net.retorx.glitchvideo.player

import java.awt.image.BufferedImage

trait ImageSource {
    def nextImage: BufferedImage
}