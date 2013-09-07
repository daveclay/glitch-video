package net.retorx.glitchvideo

import com.xuggle.mediatool.{IMediaReader, MediaToolAdapter, ToolFactory}
import java.awt.image.{Raster, BufferedImage}
import com.xuggle.mediatool.event.IVideoPictureEvent
import java.util.{Date, Random}
import net.retorx.glitchvideo.player.{PlayMedia, Player}
import PlayMedia._
import net.retorx.glitchvideo.player.Player

object Main {

    def main(args: Array[String]) = {
        val random = new Random(new Date().getTime)
        val outputFilename = "/Users/daveclay/SYRIA/blah" + random.nextInt(1000) + ".mov"
        //glitch("videos/WHUT_20100602_020000_Tavis_Smiley.mp4?t=1798%2F1828&ignore=x.mp4", "blah.mov")
        //glitch("videos/CSPAN_20120707_212500_Gun_Rights_Case_Oral_Argument.mp4?t=108%2F138&ignore=x.mp4", outputFilename)
        glitch("videos/CSPAN2_20120828_103000_Book_TV.mp4?t=1795%2F1825&ignore=x.mp4", outputFilename)
        //glitch("videos/CNNW_20121222_080000_Piers_Morgan_Tonight.mp4?t=119%2F149&ignore=x.mp4", outputFilename)
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


















