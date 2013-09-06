package net.retorx.glitchvideo

import com.xuggle.mediatool.{IMediaReader, MediaToolAdapter, ToolFactory}
import java.awt.image.{Raster, BufferedImage}
import com.xuggle.mediatool.event.IVideoPictureEvent
import java.util.{Date, Random}
import PlayMedia._

object Main {

    def main(args: Array[String]) = {
        glitch("videos/CNNW_20121222_080000_Piers_Morgan_Tonight.mp4?t=119%2F149&ignore=x.mp4", "blah.mov")
    }

    def glitch(inputFilename: String, outputFilename: String) {
        glitchVideo(inputFilename, outputFilename)
        Player.play(outputFilename)
    }

    def glitchVideo(inputFilename: String, outputFilename: String) {
        val mediaReader = ToolFactory.makeReader(inputFilename)
        mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR)

        val mediaWriter = ToolFactory.makeWriter(outputFilename, mediaReader)

        val glitcher = new GlitcherTool()
        mediaReader.addListener(glitcher)
        glitcher.addListener(mediaWriter)

        play(mediaReader)
    }
}

object PlayMedia {

    def play(mediaReader: IMediaReader) {
        while (mediaReader.readPacket() == null) {}
    }
}

class GlitcherTool extends MediaToolAdapter {

    val random = new Random(new Date().getTime)
    var framesToGlitch = 0
    var frameGlitchCount = 0

    var rowToStartGlitch = 0
    var rowToStopGlitch = 0

    var frameCount = 0

    val glitcher = new ColorBand()

    override def onVideoPicture(event: IVideoPictureEvent) {
        if (frameGlitchCount >= framesToGlitch) {
            frameGlitchCount = 0
            framesToGlitch = random.nextInt(30 * 2) + 10
            rowToStartGlitch = random.nextInt(300)
            rowToStopGlitch = random.nextInt(300) + rowToStartGlitch
        }

        if (frameGlitchCount > 0 || random.nextInt(100) > 3) {
            val image = event.getImage

            glitcher.handleImage(image)

            frameGlitchCount = frameGlitchCount + 1
        }

        frameCount = frameCount + 1
        if (frameCount % 30 == 0) {
            println("Glitched " + frameCount + " frames")
        }
        super.onVideoPicture(event)
    }
}

trait RandomImageGlitcher {
    val random = new Random(new Date().getTime)

    def handleImage(image: BufferedImage)

    def updateImage(image: BufferedImage) {
        val g = image.createGraphics()
        g.drawImage(image, 0, 0, null)
    }
}

class Noise extends RandomImageGlitcher {

    override def handleImage(image: BufferedImage) {
        val data = image.getData
        val width = data.getWidth
        val height = data.getHeight

        // 640 x 360
        for (x <- 0 to 500) {
            for (y <- 0 to 300) {
                val pixel = image.getRGB(x, y)
                val r = random.nextInt(255)
                val g = random.nextInt(255)
                val b = random.nextInt(255)
                val color = (r << 16) | (g << 8) | b
                image.setRGB(x, y, pixel | color)
            }
        }

        updateImage(image)
    }
}

class ColorBand extends RandomImageGlitcher {

    override def handleImage(image: BufferedImage) {
        val data = image.getData
        val width = data.getWidth
        val height = data.getHeight

        // 640 x 360

        for (x <- 0 to (width - 1)) {
            for (y <- 0 to (height - 1)) {
                var rx = x + 50
                if (rx > (width - 1)) {
                    rx = rx - width
                }

                var gx = x + 10
                if (gx > (width - 1)) {
                    gx = gx - width
                }

                val r = image.getRGB(rx, y) & 0xFF0000
                val g = image.getRGB(gx, y) & 0xFF00
                val b = image.getRGB(x, y) & 0xFF

                val color = r | g | b

                image.setRGB(x, y, color)
            }
        }

        updateImage(image)
    }
}

class Rescan extends RandomImageGlitcher {

    override def handleImage(image: BufferedImage) {
        val data = image.getData
        val width = data.getWidth
        val height = data.getHeight

        // 640 x 360
        for (x <- 0 to (width - 1)) {
            val tx = random.nextInt(width)
            for (y <- 0 to (height - 1)) {
                val ty = random.nextInt(height)
                val originalPixel = image.getRGB(x, y)
                val pixel = image.getRGB(tx, ty)
                if (random.nextBoolean()) {
                    image.setRGB(x, y, originalPixel | pixel)
                } else {
                    image.setRGB(x, y, pixel)
                }
            }
        }

        updateImage(image)
    }
}

object Player {
    def play(filename: String) {
        playAfter(filename, 0)
    }

    def playAfter(filename: String, seconds: Int) {
        val player = new Player(filename)
        player.playAfterWaiting(seconds)
    }
}

class Player(filename: String) {

    def play() {
        playAfterWaiting(0)
    }

    def playAfterWaiting(seconds: Int) {
        val runnable = new Runnable() {
            def run() {
                Thread.sleep(seconds * 1000)
                playbackVideo(filename)
            }
        }
        val thread = new Thread(runnable)
        thread.start()
    }

    private def playbackVideo(filename: String) {
        val mediaReader = ToolFactory.makeReader(filename)
        mediaReader.addListener(ToolFactory.makeViewer())
        PlayMedia.play(mediaReader)
    }
}

