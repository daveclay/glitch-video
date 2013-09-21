package net.retorx.glitchvideo.player

import java.awt.{Graphics, Dimension, Component}
import java.awt.image.BufferedImage
import javax.swing.{JPanel, Timer, JFrame, JApplet}
import java.awt.event.{ActionEvent, ActionListener, WindowEvent, WindowAdapter}
import java.net.{MalformedURLException, URL}
import java.io.File
import com.xuggle.mediatool.{ToolFactory, MediaToolAdapter}
import com.xuggle.mediatool.event.IVideoPictureEvent
import net.retorx.glitchvideo.GlitcherTool
import java.util.{Date, Random}

class SwingPlayer(filename: String) {

    val random = new Random(new Date().getTime)
    val outputFilename = "/Users/daveclay/SYRIA/blah" + random.nextInt(1000) + ".mov"

    def play() {
        val imageSource = new ImageSourceAdapter()
        val videoComponent = new VideoImageComponent(649, 480, imageSource)

        SwingUIHelper.openInFrame(videoComponent)
        videoComponent.animate()

        val glitchPlayer = new GlitchPlayer(imageSource)
        glitchPlayer.glitch(filename, outputFilename)
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
    def open(image:BufferedImage) {
        SwingUIHelper.openInFrame(new ImageComponent(image))
    }
}

class ImageComponent(image: BufferedImage) extends JPanel {
    val data = image.getRaster
    private val iWidth = data.getWidth
    private val iHeight = data.getHeight
    setPreferredSize(new Dimension(iWidth, iHeight))

    override def paintComponent(graphics: Graphics) {
        graphics.drawImage(image, 0, 0, null)
    }
}

class VideoImageComponent(width: Int, height: Int, imageSource: ImageSource) extends JPanel {

    setPreferredSize(new Dimension(width, height))

    def animate() {
        val framesPerSecond = 50
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
        val image = imageSource.getImage
        if (image != null) {
            graphics.drawImage(image, 0, 0, null)
        }
    }

}

trait ImageSource {
    def getImage: BufferedImage
}

class ImageSourceAdapter extends MediaToolAdapter with ImageSource {

    var image: BufferedImage = null

    def getImage: BufferedImage = image

    override def onVideoPicture(event: IVideoPictureEvent) {
        image = event.getImage
        super.onVideoPicture(event)
    }
}

class GlitchPlayer(imageSourceAdapter: ImageSourceAdapter) {

    def glitch(inputFilename: String, outputFilename: String) {
        val mediaReader = ToolFactory.makeReader(inputFilename)
        mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR)

        val mediaWriter = ToolFactory.makeWriter(outputFilename, mediaReader)

        val glitcher = new GlitcherTool()
        mediaReader.addListener(glitcher)
        glitcher.addListener(imageSourceAdapter)
        imageSourceAdapter.addListener(mediaWriter)
        PlayMedia.play(mediaReader)
    }
}
