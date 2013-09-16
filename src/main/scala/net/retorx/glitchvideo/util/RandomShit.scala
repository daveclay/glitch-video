package net.retorx.glitchvideo.util

import java.util.{Date, Random}

object RandomShit extends RandomShit {
    def randomColorValue() = random.nextInt(255)
}

trait RandomShit {
    val random = new Random(new Date().getTime)
}