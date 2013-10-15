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
import net.retorx.glitchvideo.util.FrameInfo

trait VideoImageHandler extends FrameInfo {
    val modulationSync = new ModulationClock()
    var initialized = false

    def addModulation(modulation: Any) {
        modulationSync.add(modulation.asInstanceOf[Modulator[AnyRef]])
    }
    
    def onImage(image: BufferedImage) {
        if (!initialized) {
            initFrameInfo()
            initialized = true
        }

        handleFrameImage(image)
        updateFrameInfo()
    }
    
    def handleFrameImage(image: BufferedImage)
}

class VideoImageHandlerXuggleAdapter(videoImageHandler: VideoImageHandler) extends MediaToolAdapter {

    override def onVideoPicture(event: IVideoPictureEvent) {
        val image = event.getImage
        videoImageHandler.onImage(image)
        super.onVideoPicture(event)
    }

}

class FrameHandlerTool extends VideoImageHandler {

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



