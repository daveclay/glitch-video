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
import net.retorx.glitchvideo.util.{BLUE, GREEN, RED, PixelUtils}
import java.awt.image.BufferedImage
import net.retorx.glitchvideo.ui.ImageViewerUI

@RunWith(classOf[JUnitRunner])
class BufferedFrameImageTest extends FlatSpec with DefaultExampleExpectationsListener with Mockito with ShouldMatchers with OneInstancePerTest {

    // val bufferedImage = ImageIO.read(getClass.getResourceAsStream("/test.jpg"))
    // val bufferedFrameImage = new BufferedFrameImage(bufferedImage)

    val bufferedImage = createTestImage()
    val bufferedFrameImage = new BufferedFrameImage(bufferedImage, 0)

    classOf[BufferedFrameImageTest].getName should "shift row" in {
        val row = 30
        val originalRow = PixelUtils.copy(bufferedFrameImage.getRow(row))
        bufferedFrameImage.shiftRow(row, 200)
        val shiftedRow = bufferedFrameImage.getRow(row)
        shiftedRow(200) should be (originalRow(0))
    }

    it should "shift the entire image" in {
        bufferedFrameImage.shift(100, 400)
        showScaledImage()
    }

    it should "shift a color band horizontally" in {
        bufferedFrameImage.shiftColorBand(RED, 200, 0)
        showScaledImage()
    }

    it should "shift a color band vertically" in {
        bufferedFrameImage.shiftColorBand(RED, 0, 10)
        showScaledImage()
    }

    private def showScaledImage() {
        ImageViewerUI.open(createTestImage(), 1000)
        ImageViewerUI.open(bufferedImage, 1000)
        //ImageViewerUI.open(PixelUtils.scale(bufferedImage, 10, 10), 2000)
    }

    private def createTestImage() = {
        val source = ImageIO.read(getClass.getResourceAsStream("/image.png"))
        //val source = ImageIO.read(getClass.getResourceAsStream("/9x9.png"))
        //renderTestImage(source
        source
    }

    private def renderTestImage(source: BufferedImage) {
        val raster = source.getRaster
        for (x <- 0 to raster.getWidth - 1) {
            for (y <- 0 to raster.getHeight - 1) {
                val pixel = y % 3 match {
                    case 0 => 0xff0000
                    case 1 => 0x00ff00
                    case 2 => 0x0000ff
                }
                if (x == 0) {
                    source.setRGB(x, y, pixel)
                } else {
                    source.setRGB(x, y, 0)
                }
            }
        }
    }
}