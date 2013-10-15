package net.retorx.glitchvideo.player

import com.xuggle.mediatool.{MediaToolAdapter, ToolFactory}
import java.awt.image.BufferedImage
import net.retorx.glitchvideo.{VideoImageHandlerXuggleAdapter, FrameHandlerTool}
import com.xuggle.mediatool.event.IVideoPictureEvent
import scala.collection.mutable.ListBuffer
import net.retorx.glitchvideo.player.xuggler.{XugglerVideoImageCapturer, XuggleMediaReaderHelper}

class GlitchPlayer(imageSourceAdapter: ImageSourceAdapter = null,
                   outputFilename: String = null,
                   useXugglerViewer: Boolean = false) {

    def glitch(inputFilename: String) {
        val mediaReader = ToolFactory.makeReader(inputFilename)
        mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR)

        val glitcher = new FrameHandlerTool()
        val adapter = new VideoImageHandlerXuggleAdapter(glitcher)
        mediaReader.addListener(adapter)

        if (imageSourceAdapter != null) {
            adapter.addListener(imageSourceAdapter)
        }

        if (outputFilename != null) {
            val fileWriter = ToolFactory.makeWriter(outputFilename, mediaReader)
            adapter.addListener(fileWriter)
        }

        if (useXugglerViewer) {
            adapter.addListener(ToolFactory.makeViewer())
        }

        XuggleMediaReaderHelper.startReading(mediaReader)
    }
}

object LetsSeeHowMuchMemoryThisTakes {
    def main(args: Array[String]) {
        val capturer = new XugglerVideoImageCapturer(args(0))
        
        capturer.capture()
        val videoImageData = capturer.getVideoImageData

        Thread.sleep(10000000)
    }

}

class VideoImageData(images: ListBuffer[BufferedImage]) {

}





