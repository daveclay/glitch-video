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
        val syncData = calcSyncData(frameImage)
        // Todo: This will never shift individual pixels randomly.
        if (blueShiftAmount(syncData) > 0) {
            frameImage.shiftColorBand(BLUE, 0, blueShiftAmount(syncData))
        }
        if (redShiftAmount(syncData) > 0) {
            frameImage.shiftColorBand(RED, 0, redShiftAmount(syncData))
        }
        if (greenShiftAmount(syncData) > 0) {
            frameImage.shiftColorBand(GREEN, 0, greenShiftAmount(syncData))
        }
    }
}


