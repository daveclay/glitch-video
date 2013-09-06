package net.retorx.glitchvideo.util

object PixelUtils {

    def wrap(x: Int, width: Int) = {
        if (x >= width) {
            x - width
        } else {
            x
        }
    }

}