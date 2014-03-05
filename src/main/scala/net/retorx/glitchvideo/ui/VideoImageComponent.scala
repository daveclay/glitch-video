package net.retorx.glitchvideo.ui

import net.retorx.glitchvideo.player.ImageSource
import javax.swing.{Timer, JPanel}
import java.awt.{Graphics, Dimension}
import java.awt.event.{ActionEvent, ActionListener}

class VideoImageComponent(width: Int, height: Int, imageSource: ImageSource) extends JPanel {
    val framesPerSecond = 30
    val delay = 1000 / framesPerSecond
    val timer = new Timer(delay, null)
    var index = 0
    var image = imageSource.getImageAt(index)

    setPreferredSize(new Dimension(width, height))

    def setFrameIndex(index: Int) {
        this.index = index
    }

    def nextFrame() {
        val nextImage = imageSource.getImageAt(index)
        if (nextImage != null) {
            image = nextImage
            setFrameIndex(index + 1)
        }
    }

    def play() {

        timer.addActionListener(new ActionListener {
            def actionPerformed(e: ActionEvent) {
                nextFrame()
                repaint()
            }
        })

        timer.start()
    }

    override def paintComponent(graphics: Graphics) {
        if (image != null) {
            graphics.drawImage(image, 0, 0, null)
        }
    }

}