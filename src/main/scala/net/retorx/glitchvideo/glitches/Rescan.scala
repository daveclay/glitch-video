package net.retorx.glitchvideo.glitches

import java.awt.image.BufferedImage

class Rescan extends RandomImageGlitcher {

    override def handleImage(image: BufferedImage) {
        val data = image.getData
        val width = data.getWidth
        val height = data.getHeight

        // 640 x 360
        for (x <- 0 to (width - 1)) {
            val tx = random.nextInt(width)
            for (y <- 0 to (height - 1)) {
                val ty = random.nextInt(height)
                val originalPixel = image.getRGB(x, y)
                val pixel = image.getRGB(tx, ty)
                if (random.nextBoolean()) {
                    image.setRGB(x, y, originalPixel | pixel)
                } else {
                    image.setRGB(x, y, pixel)
                }
            }
        }

        updateImage(image)
    }
}