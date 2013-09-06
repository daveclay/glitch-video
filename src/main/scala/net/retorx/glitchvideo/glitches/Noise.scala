package net.retorx.glitchvideo.glitches

import java.awt.image.BufferedImage

class Noise extends RandomImageGlitcher {

    override def handleImage(image: BufferedImage) {
        val data = image.getData
        val width = data.getWidth
        val height = data.getHeight

        // 640 x 360
        for (x <- 0 to 500) {
            for (y <- 0 to 300) {
                val pixel = image.getRGB(x, y)
                val r = random.nextInt(255)
                val g = random.nextInt(255)
                val b = random.nextInt(255)
                val color = (r << 16) | (g << 8) | b
                image.setRGB(x, y, pixel | color)
            }
        }

        updateImage(image)
    }
}