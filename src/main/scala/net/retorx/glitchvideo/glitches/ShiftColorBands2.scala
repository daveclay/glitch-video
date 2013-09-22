package net.retorx.glitchvideo.glitches

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.glitches.colorbands.{Horizontal, ShiftDirection}
import net.retorx.glitchvideo.modulation.ModulatedIntValue
import net.retorx.glitchvideo.util.Color

class ShiftColorBands2(shiftDirection: ShiftDirection = Horizontal,
                       redShiftAmount: ModulatedIntValue,
                       greenShiftAmount: ModulatedIntValue,
                       blueShiftAmount: ModulatedIntValue)
        extends FrameHandler {

    def handleFrame(frameImage: FrameImage) {
        if (redShiftAmount() > 0) {
        }
    }
}
