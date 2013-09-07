package net.retorx.glitchvideo

import com.xuggle.mediatool.MediaToolAdapter
import java.util.{Date, Random}
import com.xuggle.mediatool.event.IVideoPictureEvent
import net.retorx.glitchvideo.glitches.routing.{ApplyAll, GlitchOption, ApplyRandom, ApplyAllColorBands}
import net.retorx.glitchvideo.glitches.colorbands._
import net.retorx.glitchvideo.glitches.{Rescan, Noise}
import net.retorx.glitchvideo.glitches.routing.GlitchOption

class GlitcherTool extends MediaToolAdapter {

    val random = new Random(new Date().getTime)
    var framesToGlitch = 0
    var frameGlitchCount = 0

    var rowToStartGlitch = 0
    var rowToStopGlitch = 0

    var frameCount = 0

    //val glitcher = new ColorBandSplitter(Array(new ShiftColorBands()))
    val applyAllColorBands = new ApplyAllColorBands(Array(
        new DriftColorBands(),
        new NoiseLinesSingleColorBands()
    ))

    val applyRandom = new ApplyRandom(List(
        GlitchOption(applyAllColorBands, 100),
        GlitchOption(new Noise(), 20)
    ))

    val applyAll = new ApplyAll(List(
        new Rescan(),
        applyRandom
    ))

    val glitcher = applyAll // applyRandom // new Rescan()

    override def onVideoPicture(event: IVideoPictureEvent) {
        if (frameGlitchCount >= framesToGlitch) {
            frameGlitchCount = 0
            framesToGlitch = random.nextInt(30 * 2) + 10
            rowToStartGlitch = random.nextInt(300)
            rowToStopGlitch = random.nextInt(300) + rowToStartGlitch
        }

        if (frameGlitchCount > 0 || random.nextInt(100) > 3) {
            val image = event.getImage
            val data = image.getData

            glitcher.width = data.getWidth
            glitcher.height = data.getHeight
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