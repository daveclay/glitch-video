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
import net.retorx.glitchvideo.util.{GREEN, RED, PixelUtils}
import java.awt.image.{AffineTransformOp, BufferedImage}
import java.awt.geom.AffineTransform
import java.awt.{Image, Rectangle}
import sun.awt.image.ByteInterleavedRaster
import java.io.File
import net.retorx.glitchvideo.ui.ImageViewerUI

@RunWith(classOf[JUnitRunner])
class RasterTest extends FlatSpec with DefaultExampleExpectationsListener with Mockito with ShouldMatchers with OneInstancePerTest {

    val bufferedImage = createImage()
    val bufferedFrameImage = new BufferedFrameImage(bufferedImage, 0)

    classOf[RasterTest].getName should "show how data is stored" in {
        view()
        bufferedFrameImage.shiftColorBand(RED, 0, 1)
        view()
    }

    private def view() {
        ImageViewerUI.open(PixelUtils.scale(bufferedImage, 10, 10), 2000)
    }

    private def createImage() = {
        ImageIO.read(getClass.getResourceAsStream("/fuck-test.jpg"))
        /*
        val source = ImageIO.read(getClass.getResourceAsStream("/9x9.png"))
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

        ImageIO.write(source, "jpeg", new File("/tmp/fuck-test.jpg"))
        source
        */
    }
}