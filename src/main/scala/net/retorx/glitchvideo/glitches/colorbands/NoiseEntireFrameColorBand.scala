package net.retorx.glitchvideo.glitches.colorbands

import java.awt.image.BufferedImage
import net.retorx.glitchvideo.util.RandomShit
import net.retorx.glitchvideo.glitches.routing.ColorBandHandler

trait NoiseOption
object RANDOM_NOISE_ALL_BANDS extends NoiseOption
case class RANDOM_NOISE_SINGLE_BAND(band: Int) extends NoiseOption

class NoiseEntireFrameColorBand(option: NoiseOption, weight: Int) extends ColorBandHandler with RandomShit {

    var band = 0
    var blendCount = 0
    var blending = false

    def notifyOfNextFrame() {
        if ( ! blending) {
            if (random.nextInt(100) > weight) {
                if (option == RANDOM_NOISE_ALL_BANDS) {
                    band = random.nextInt(3)
                } else {
                    band = option.asInstanceOf[RANDOM_NOISE_SINGLE_BAND].band
                }
                blending = true
            }
        } else if (blendCount > 30) {
            blendCount = 0
            blending = false
        }
    }
    
    def handleBands(r: Int, g: Int, b: Int, x: Int, y: Int, image: BufferedImage): Int = {
        var tr = r
        var tg = g
        var tb = b
        if (blending) {
            if (band == 0) {
                tr = r | (screwBand(r) << 16)
            } else if (band == 1) {
                tg = g | (screwBand(g) << 8)
            } else {
                tb = b | screwBand(b)
            }
        }
        tr | tg | tb
    }
    
    def screwBand(value: Int) = {
        random.nextInt(255)
    }
}