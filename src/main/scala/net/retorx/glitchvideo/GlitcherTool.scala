package net.retorx.glitchvideo

import com.xuggle.mediatool.MediaToolAdapter
import net.retorx.glitchvideo.util.PixelUtils._
import com.xuggle.mediatool.event.IVideoPictureEvent
import net.retorx.glitchvideo.glitches.routing.{ApplyAll, GlitchOption, ApplyRandom, ApplyAllColorBands}
import net.retorx.glitchvideo.glitches.colorbands._
import net.retorx.glitchvideo.glitches.routing.GlitchOption
import net.retorx.glitchvideo.glitches.{Rescan, Noise}

class GlitcherTool extends MediaToolAdapter {

    var frameCount = 0

    //val glitcher = new ColorBandSplitter(Array(new ShiftColorBands()))
    val applyAllColorBands = new ApplyAllColorBands(Array(
        // new RandomNoiseLinesSingleColorBands(),
        new OscillatePxelationOfColorBands()
    ))

    val applyRandom = new ApplyRandom(List(
        //GlitchOption(new Noise(), 20),
        GlitchOption(applyAllColorBands, 100)
    ))

    val applyAll = new ApplyAll(List(
        //new Rescan(),
        applyRandom
    ))

    val glitcher = applyAll // applyRandom // new Rescan()

    override def onVideoPicture(event: IVideoPictureEvent) {
        val image = event.getImage
        val data = image.getData

        glitcher.width = data.getWidth
        glitcher.height = data.getHeight

        val destination = copyImage(image)
        glitcher.handleFrameImage(image, destination)

        // paint destination on the original
        val g = image.createGraphics()
        g.drawImage(destination, 0, 0, null)

        frameCount = frameCount + 1
        if (frameCount % 30 == 0) {
            println("Glitched " + frameCount + " frames")
        }

        super.onVideoPicture(event)
    }
}



