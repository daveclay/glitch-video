package net.retorx.glitchvideo.player.xuggler

import com.xuggle.mediatool.ToolFactory
import java.awt.image.BufferedImage
import scala.collection.mutable.ListBuffer
import net.retorx.glitchvideo.player.VideoImageData

class XugglerVideoImageCapturer(inputFilename: String) {

    val mediaReader = ToolFactory.makeReader(inputFilename)
    mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR)

    val videoImageCaptureAdapter = new VideoImageCaptureAdapter()
    mediaReader.addListener(videoImageCaptureAdapter)

    def capture() {
        XuggleMediaReaderHelper.startReading(mediaReader)
    }

    def getVideoImageData = new VideoImageData(getImages)

    private def getImages: ListBuffer[BufferedImage] = {
        videoImageCaptureAdapter.images
    }
}