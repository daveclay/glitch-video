package net.retorx.glitchvideo.player

import com.xuggle.mediatool.{MediaToolAdapter, ToolFactory}
import java.awt.image.BufferedImage
import net.retorx.glitchvideo.FrameHandlerTool

class GlitchPlayer(imageSourceAdapter: ImageSourceAdapter = null,
                   outputFilename: String = null,
                   useXugglerViewer: Boolean = false) {

    def glitch(inputFilename: String) {
        val mediaReader = ToolFactory.makeReader(inputFilename)
        mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR)

        val glitcher = new FrameHandlerTool()
        mediaReader.addListener(glitcher)

        if (imageSourceAdapter != null) {
            glitcher.addListener(imageSourceAdapter)
        }

        if (outputFilename != null) {
            val fileWriter = ToolFactory.makeWriter(outputFilename, mediaReader)
            glitcher.addListener(fileWriter)
        }

        if (useXugglerViewer) {
            glitcher.addListener(ToolFactory.makeViewer())
        }

        XuggleMediaReaderHelper.startReading(mediaReader)
    }
}

