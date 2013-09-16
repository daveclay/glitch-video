package net.retorx.glitchvideo.glitches

import java.awt.image.BufferedImage

class Noise extends RandomImageGlitcher {

    override def handleFrameImage(image: BufferedImage, destination: BufferedImage) {
        // 640 x 360
        for (x <- 1 to (width - 1)) {
            for (y <- 1 to (height - 1)) {
                val pixel = image.getRGB(x, y)
                val r = random.nextInt(255)
                val g = random.nextInt(255)
                val b = random.nextInt(255)
                val color = (r << 16) | (g << 8) | b
                destination.setRGB(x, y, pixel | color)
            }
        }
    }
}