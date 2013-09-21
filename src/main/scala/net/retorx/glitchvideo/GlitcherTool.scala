package net.retorx.glitchvideo

import com.xuggle.mediatool.MediaToolAdapter
import net.retorx.glitchvideo.util.PixelUtils._
import com.xuggle.mediatool.event.IVideoPictureEvent
import net.retorx.glitchvideo.glitches.routing.{ApplyAll, GlitchOption, ApplyRandom, ApplyAllColorBands}
import net.retorx.glitchvideo.glitches.colorbands._
import net.retorx.glitchvideo.glitches.routing.GlitchOption
import net.retorx.glitchvideo.glitches.{Rescan, Noise}
import net.retorx.glitchvideo.modulation._
import net.retorx.glitchvideo.glitches.routing.GlitchOption

class GlitcherTool extends MediaToolAdapter {

    var start = 0L
    var frameCount = 0

    val modulationSync = new ModulationSynchronizer()

    val redShiftAmount = new ModulatedIntValue(new StaticIntModulation(10))
    val greenShiftAmount = new ModulatedIntValue(new StaticIntModulation(100))
    val period = new ModulatedIntValue(new StaticIntModulation(30))
    val scale = new ModulatedIntValue(new StaticIntModulation(400))
    val lfo = new LFOIntModulation(period, scale)

    modulationSync.add(lfo.asInstanceOf[Modulation[AnyRef]])

    // Todo: hard-coded height; where can I get this from?
    val blueShiftAmount = new ModulatedIntValue(lfo)
    var shiftColorBands = new ShiftColorBands(Horizontal, redShiftAmount, greenShiftAmount, blueShiftAmount)

    //val glitcher = new ColorBandSplitter(Array(new ShiftColorBands()))
    val applyAllColorBands = new ApplyAllColorBands(Array(
        // new RandomNoiseLinesSingleColorBands(),
        shiftColorBands
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
        if (start == 0) {
            start = System.currentTimeMillis()
        }

        val image = event.getImage
        val data = image.getData

        glitcher.width = data.getWidth
        glitcher.height = data.getHeight

        val destination = copyImage(image)

        /*
            TODO: ticks must be configurable.
            ticks can happen on each frame, each pixel of each frame, maybe arbitrarily?
            Can the tick configuration be modulated? Sure.

            The thing that triggers the ticks - it needs access to each frame and each pixel of each frame.
         */
        modulationSync.tick()

        glitcher.handleFrameImage(image, destination)

        // paint destination on the original
        // TODO: This could just push the dest image to the player, but it'd skip the writing of the file... it could
        // push the dest image and then write the file... but that's allon this thread, meaning this thread is not going
        // to get to the next frame anytime soon.

        val g = image.createGraphics()
        g.drawImage(destination, 0, 0, null)
        frameCount += 1
        if (frameCount % 30 == 0) {
            val now = System.currentTimeMillis()
            val average = ((now - start) / frameCount)
            val fps = 1000 / average
            println(frameCount + " frames " + (now - start) + "ms @ " + average + " per frame " + fps + " fps")
        }

        super.onVideoPicture(event)
    }
}



