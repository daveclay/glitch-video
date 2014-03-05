package net.retorx.glitchvideo.ui

import java.awt.{Image, Graphics, Dimension, Component}
import javax.swing.{JPanel, Timer, JFrame, JApplet}
import net.retorx.glitchvideo.player.{ImageSource, GlitchPlayer, ImageSourceAdapter}

class SwingPlayer() {

    def play(filename: String, outputFilename: String) {
        val imageSource = new ImageSourceAdapter()
        val videoComponent = new VideoImageComponent(649, 480, imageSource)

        SwingUIHelper.openInFrame(videoComponent)
        videoComponent.play()

        val glitchPlayer = new GlitchPlayer(imageSource, outputFilename = outputFilename)
        glitchPlayer.glitch(filename)
    }
}

object SwingUIHelper {
    def openInFrame(panel: JPanel) {
        val jframe = new JFrame("Player")
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        jframe.setContentPane(panel)
        jframe.pack()
        jframe.setVisible(true)
    }
}

object ImageViewerUI {
    def open(image:Image) {
        open(image, 0)
    }

    def open(image:Image, time:Int) {
        SwingUIHelper.openInFrame(new ImageComponent(image))
        Thread.sleep(time)
    }
}






