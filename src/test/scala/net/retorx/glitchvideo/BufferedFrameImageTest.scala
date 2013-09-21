package net.retorx.glitchvideo

import org.junit.runner.RunWith
import org.specs._
import mock.Mockito
import org.scalatest.{OneInstancePerTest, FlatSpec}
import specification.DefaultExampleExpectationsListener
import java.io.File
import org.scalatest.junit.JUnitRunner
import java.util.Date
import org.mockito.Mockito._
import org.scalatest.matchers.ShouldMatchers
import scala.collection.mutable.ListBuffer
import net.retorx.glitchvideo.glitches.BufferedFrameImage
import javax.imageio.ImageIO
import net.retorx.glitchvideo.util.PixelUtils
import net.retorx.glitchvideo.player.ImageViewerUI

@RunWith(classOf[JUnitRunner])
class BufferedFrameImageTest extends FlatSpec with DefaultExampleExpectationsListener with Mockito with ShouldMatchers with OneInstancePerTest {

    val bufferedImage = ImageIO.read(getClass.getResourceAsStream("/test.jpg"))
    val bufferedFrameImage = new BufferedFrameImage(bufferedImage)

    classOf[BufferedFrameImageTest].getName should "shift row" in {
        bufferedFrameImage.shiftRow(30, 200)
        ImageViewerUI.open(bufferedImage)

        Thread.sleep(10000)
    }
}