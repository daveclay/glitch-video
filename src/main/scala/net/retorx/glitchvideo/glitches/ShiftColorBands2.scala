package net.retorx.glitchvideo.glitches

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.glitches.colorbands.{Horizontal, ShiftDirection}
import net.retorx.glitchvideo.modulation.ModulatedIntValue
import net.retorx.glitchvideo.util.{GREEN, BLUE, RED, Color}

class ShiftColorBands2(shiftDirection: ShiftDirection = Horizontal,
                       redShiftAmount: ModulatedIntValue,
                       greenShiftAmount: ModulatedIntValue,
                       blueShiftAmount: ModulatedIntValue)
        extends FrameHandler {

    def handleFrame(frameImage: FrameImage) {
        // Todo: This will never shift individual pixels randomly.
        if (blueShiftAmount() > 0) {
            frameImage.shiftColorBand(BLUE, 0, blueShiftAmount())
        }
        if (redShiftAmount() > 0) {
            frameImage.shiftColorBand(RED, 0, redShiftAmount())
        }
        if (greenShiftAmount() > 0) {
            frameImage.shiftColorBand(GREEN, 0, greenShiftAmount())
        }
    }
}


