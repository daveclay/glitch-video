package net.retorx.glitchvideo

import com.xuggle.mediatool.{IMediaReader, MediaToolAdapter, ToolFactory}
import java.awt.image.{Raster, BufferedImage}
import com.xuggle.mediatool.event.IVideoPictureEvent
import java.util.{Date, Random}
import net.retorx.glitchvideo.player.xuggler.XuggleMediaReaderHelper
import XuggleMediaReaderHelper._
import net.retorx.glitchvideo.ui.SwingPlayer

object Main {

    def main(args: Array[String]) = {
        //glitch("videos/WHUT_20100602_020000_Tavis_Smiley.mp4?t=1798%2F1828&ignore=x.mp4", "blah.mov")
        //glitch("videos/CSPAN_20120707_212500_Gun_Rights_Case_Oral_Argument.mp4?t=108%2F138&ignore=x.mp4", outputFilename)
        //glitch("videos/CNNW_20121222_080000_Piers_Morgan_Tonight.mp4?t=119%2F149&ignore=x.mp4", outputFilename)

        val random = new Random(new Date().getTime)
        val outputFilename = "/Users/daveclay/SYRIA/blah" + random.nextInt(1000) + ".mov"

        val filename = "videos/CSPAN2_20120828_103000_Book_TV.mp4?t=1795%2F1825&ignore=x.mp4"

        //val player = new XugglerVideoPlayer()
        val player = new SwingPlayer()
        player.play(filename, outputFilename)
    }
}


















