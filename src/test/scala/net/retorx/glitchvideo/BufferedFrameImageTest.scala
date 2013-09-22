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
import net.retorx.glitchvideo.util.{GREEN, RED, PixelUtils}
import net.retorx.glitchvideo.player.ImageViewerUI

@RunWith(classOf[JUnitRunner])
class BufferedFrameImageTest extends FlatSpec with DefaultExampleExpectationsListener with Mockito with ShouldMatchers with OneInstancePerTest {

    val bufferedImage = ImageIO.read(getClass.getResourceAsStream("/test.jpg"))
    val bufferedFrameImage = new BufferedFrameImage(bufferedImage)

    classOf[BufferedFrameImageTest].getName should "shift row" in {
        val row = 30
        val originalRow = PixelUtils.copy(bufferedFrameImage.getRow(row))
        bufferedFrameImage.shiftRow(row, 200)
        val shiftedRow = bufferedFrameImage.getRow(row)
        shiftedRow(200) should be (originalRow(0))
    }

    it should "shift a color band" in {
        val distance = bufferedFrameImage.width * (bufferedFrameImage.height / 2)
        bufferedFrameImage.shiftColorBand(RED, distance, 0)
        ImageViewerUI.open(bufferedImage, 10000)
    }
}