package net.retorx.glitchvideo.glitches.routing

import java.awt.image.BufferedImage
import scala.collection.immutable._
import net.retorx.glitchvideo.glitches.RandomImageGlitcher

case class GlitchOption(glitcher: RandomImageGlitcher, weight: Int)

class ApplyRandom(options: List[GlitchOption]) extends RandomImageGlitcher {

    def addGlitcherByWeight(glitchers: List[RandomImageGlitcher],
                            option: GlitchOption): List[RandomImageGlitcher] = {
        (0 to option.weight).foldLeft(glitchers)((list, index) => {
            list ++ List(option.glitcher)
        })
    }

    val array = options.foldLeft(List[RandomImageGlitcher]())((glitchers, option) => {
        addGlitcherByWeight(glitchers, option)
    })

    def handleFrameImage(source: BufferedImage, destination: BufferedImage) {
        val idx = random.nextInt(array.size)
        val glitcher = array(idx)
        glitcher.width = width
        glitcher.height = height
        glitcher.handleFrameImage(source, destination)
    }
}