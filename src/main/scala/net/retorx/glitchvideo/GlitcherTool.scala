package net.retorx.glitchvideo

import com.xuggle.mediatool.MediaToolAdapter
import net.retorx.glitchvideo.util.PixelUtils._
import com.xuggle.mediatool.event.IVideoPictureEvent
import net.retorx.glitchvideo.glitches.routing.{ApplyAll, GlitchOption, ApplyRandom, ApplyAllColorBands}
import net.retorx.glitchvideo.glitches.colorbands._
import net.retorx.glitchvideo.glitches.routing.GlitchOption
import net.retorx.glitchvideo.glitches._
import net.retorx.glitchvideo.modulation._
import net.retorx.glitchvideo.glitches.routing.GlitchOption
import java.awt.image.BufferedImage
import net.retorx.glitchvideo.glitches.routing.GlitchOption

trait FrameInfo {

    var start = 0L
    var frameCount = 0

    def initFrameInfo() {
        start = System.currentTimeMillis()
    }

    def updateFrameInfo() {
        frameCount += 1
        if (frameCount % 30 == 0) {
            val now = System.currentTimeMillis()
            val average = (now - start) / frameCount
            val fps = 1000 / average
            println(frameCount + " frames " + (now - start) + "ms @ " + average + " per frame " + fps + " fps")
        }
    }

}

trait ModulationTool extends MediaToolAdapter with FrameInfo {
    val modulationSync = new ModulationClock()
    var initialized = false

    def addModulation(modulation: Any) {
        modulationSync.add(modulation.asInstanceOf[Modulator[AnyRef]])
    }

    override def onVideoPicture(event: IVideoPictureEvent) {
        if (!initialized) {
            initFrameInfo()
            initialized = true
        }

        val image = event.getImage
        handleFrameImage(image)

        updateFrameInfo()
        super.onVideoPicture(event)
    }

    def handleFrameImage(image: BufferedImage)
}

class GlitcherTool extends ModulationTool {

    val redShiftAmount = new ModulatedIntValue(new StaticIntModulator(10))
    val greenShiftAmount = new ModulatedIntValue(new StaticIntModulator(100))
    val period = new ModulatedIntValue(new StaticIntModulator(30))
    val scale = new ModulatedIntValue(new StaticIntModulator(400))
    val lfo = new LFOIntModulator(period, scale)

    addModulation(lfo)

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
    def handleFrameImage(image: BufferedImage) {
        val data = image.getData
        glitcher.width = data.getWidth
        glitcher.height = data.getHeight

        val destination = copyImage(image)

        glitcher.handleFrameImage(image, destination)

        // paint destination on the original
        // TODO: This could just push the dest image to the player, but it'd skip the writing of the file... it could
        // push the dest image and then write the file... but that's allon this thread, meaning this thread is not going
        // to get to the next frame anytime soon.
        val g = image.createGraphics()
        g.drawImage(destination, 0, 0, null)

    }
}

class FrameHandlerTool extends ModulationTool {

    val redShiftAmount = new ModulatedIntValue(new StaticIntModulator(10))
    val greenShiftAmount = new ModulatedIntValue(new StaticIntModulator(100))
    val period = new ModulatedIntValue(new StaticIntModulator(30))
    val scale = new ModulatedIntValue(new StaticIntModulator(400))
    val lfo = new LFOIntModulator(period, scale)
    val random = new RandomIntModulator(new ModulatedIntValue(0), new ModulatedIntValue(lfo))
    val blueShiftAmount = new ModulatedIntValue(random)
    addModulation(random)
    addModulation(lfo)

    //var shiftColorBands = new ShiftColorBands2(Horizontal, redShiftAmount, greenShiftAmount, blueShiftAmount)
    var shiftColorBands = new ShiftIndividualScanlines(Horizontal, redShiftAmount, greenShiftAmount, blueShiftAmount)

    val frameHandler = shiftColorBands

    var frameIdx = 0L

    def handleFrameImage(image: BufferedImage) {
        frameHandler.handleFrame(new BufferedFrameImage(image, frameIdx))
        frameIdx += 1
    }
}



