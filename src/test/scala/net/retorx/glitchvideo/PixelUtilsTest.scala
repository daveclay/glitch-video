package net.retorx.glitchvideo

import org.junit.runner.RunWith
import org.specs._
import mock.Mockito
import org.scalatest.{OneInstancePerTest, FlatSpec}
import specification.DefaultExampleExpectationsListener
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import net.retorx.glitchvideo.glitches.BufferedFrameImage
import javax.imageio.ImageIO
import net.retorx.glitchvideo.util.PixelUtils

@RunWith(classOf[JUnitRunner])
class PixelUtilsTest extends FlatSpec with DefaultExampleExpectationsListener with Mockito with ShouldMatchers with OneInstancePerTest {

    val src = Array(1, 2, 3, 4, 5, 6, 7, 8, 9)

    classOf[PixelUtilsTest].getName should "shift right" in {
        val shiftedRow = PixelUtils.shift(src, 2)
        shiftedRow should be (Array(8, 9, 1, 2, 3, 4, 5, 6, 7))
    }

    it should "shift left" in {
        val shiftedRow = PixelUtils.shift(src, -2)
        shiftedRow should be (Array(3, 4, 5, 6, 7, 8, 9, 1, 2))
    }
}