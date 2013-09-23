package net.retorx.glitchvideo.glitches

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.glitches.colorbands.{Horizontal, ShiftDirection}
import net.retorx.glitchvideo.modulation.ModulatedIntValue
import net.retorx.glitchvideo.util.{RED, Color}

class ShiftColorBands2(shiftDirection: ShiftDirection = Horizontal,
                       redShiftAmount: ModulatedIntValue,
                       greenShiftAmount: ModulatedIntValue,
                       blueShiftAmount: ModulatedIntValue)
        extends FrameHandler {

    def handleFrame(frameImage: FrameImage) {
        if (blueShiftAmount() > 0) {
            frameImage.shiftColorBand(RED, 0, blueShiftAmount())
        }
    }
}

