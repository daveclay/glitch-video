package net.retorx.glitchvideo.glitches.routing

import net.retorx.glitchvideo.glitches.RandomImageGlitcher
import net.retorx.glitchvideo.util.RandomShit
import java.awt.image.BufferedImage

class ApplyEverySoOften(glitcher: RandomImageGlitcher) extends RandomImageGlitcher with RandomShit {

    var framesToGlitch = 0
    var frameGlitchCount = 0

    var rowToStartGlitch = 0
    var rowToStopGlitch = 0

    var frameCount = 0

    def handleFrameImage(image: BufferedImage, destination: BufferedImage) {
        if (frameGlitchCount >= framesToGlitch) {
            frameGlitchCount = 0
            framesToGlitch = random.nextInt(30 * 2) + 10
            rowToStartGlitch = random.nextInt(300)
            rowToStopGlitch = random.nextInt(300) + rowToStartGlitch
        }

        if (frameGlitchCount > 0 || random.nextInt(100) > 3) {
            val data = image.getData

            glitcher.width = data.getWidth
            glitcher.height = data.getHeight
            glitcher.handleFrameImage(image, destination)

            frameGlitchCount = frameGlitchCount + 1
        }
    }
}