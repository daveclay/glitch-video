package net.retorx.glitchvideo.glitches

import com.xuggle.mediatool.MediaToolAdapter
import java.util.{Date, Random}
import com.xuggle.mediatool.event.IVideoPictureEvent

class GlitcherTool extends MediaToolAdapter {

    val random = new Random(new Date().getTime)
    var framesToGlitch = 0
    var frameGlitchCount = 0

    var rowToStartGlitch = 0
    var rowToStopGlitch = 0

    var frameCount = 0

    //val glitcher = new ColorBandSplitter(Array(new ShiftColorBands()))
    val glitcher = new ColorBandSplitter(Array(new XYDriftColorBands(), new NoiseColorBands()))

    override def onVideoPicture(event: IVideoPictureEvent) {
        if (frameGlitchCount >= framesToGlitch) {
            frameGlitchCount = 0
            framesToGlitch = random.nextInt(30 * 2) + 10
            rowToStartGlitch = random.nextInt(300)
            rowToStopGlitch = random.nextInt(300) + rowToStartGlitch
        }

        if (frameGlitchCount > 0 || random.nextInt(100) > 3) {
            val image = event.getImage

            glitcher.handleImage(image)

            frameGlitchCount = frameGlitchCount + 1
        }

        frameCount = frameCount + 1
        if (frameCount % 30 == 0) {
            println("Glitched " + frameCount + " frames")
        }
        super.onVideoPicture(event)
    }
}