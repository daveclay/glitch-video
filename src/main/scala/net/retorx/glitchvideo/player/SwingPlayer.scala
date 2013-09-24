package net.retorx.glitchvideo.player

import java.awt.{Image, Graphics, Dimension, Component}
import java.awt.image.BufferedImage
import javax.swing.{JPanel, Timer, JFrame, JApplet}
import java.awt.event.{ActionEvent, ActionListener, WindowEvent, WindowAdapter}
import java.net.{MalformedURLException, URL}
import java.io.File
import com.xuggle.mediatool.{ToolFactory, MediaToolAdapter}
import com.xuggle.mediatool.event.IVideoPictureEvent
import net.retorx.glitchvideo.{FrameHandlerTool, GlitcherTool}
import java.util.{Date, Random}

class SwingPlayer() {

    def play(filename: String, outputFilename: String) {
        val imageSource = new ImageSourceAdapter()
        val videoComponent = new VideoImageComponent(649, 480, imageSource)

        SwingUIHelper.openInFrame(videoComponent)
        videoComponent.startPollingForFrames()

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

class ImageComponent(image: Image) extends JPanel {
    private val iWidth = image.getWidth(null)
    private val iHeight = image.getHeight(null)
    setPreferredSize(new Dimension(iWidth, iHeight))

    override def paintComponent(graphics: Graphics) {
        graphics.drawImage(image, 0, 0, null)
    }
}

class VideoImageComponent(width: Int, height: Int, imageSource: ImageSource) extends JPanel {

    setPreferredSize(new Dimension(width, height))

    def startPollingForFrames() {
        val framesPerSecond = 30
        val delay = 1000 / framesPerSecond
        val timer = new Timer(delay, null)

        timer.addActionListener(new ActionListener {
            def actionPerformed(e: ActionEvent) {
                repaint()
            }
        })

        timer.start()
    }

    override def paintComponent(graphics: Graphics) {
        val image = imageSource.nextImage
        if (image != null) {
            graphics.drawImage(image, 0, 0, null)
        }
    }

}






