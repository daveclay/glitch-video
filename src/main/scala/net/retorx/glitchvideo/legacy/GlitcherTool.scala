package net.retorx.glitchvideo.legacy

import net.retorx.glitchvideo.modulation.{LFOIntModulator, StaticIntModulator, ModulatedIntValue}
import net.retorx.glitchvideo.glitches.colorbands.{Horizontal, ShiftColorBands}
import net.retorx.glitchvideo.glitches.routing.{ApplyAll, GlitchOption, ApplyRandom, ApplyAllColorBands}
import scala.Array
import java.awt.image.BufferedImage
import net.retorx.glitchvideo.util.PixelUtils._
import net.retorx.glitchvideo.glitches.routing.GlitchOption
import net.retorx.glitchvideo.VideoImageHandler

class GlitcherTool extends VideoImageHandler {

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