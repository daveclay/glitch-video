package net.retorx.glitchvideo.ui

import java.awt.{Graphics, Dimension, Image}
import javax.swing.JPanel

class ImageComponent(image: Image) extends JPanel {
     private val iWidth = image.getWidth(null)
     private val iHeight = image.getHeight(null)
     setPreferredSize(new Dimension(iWidth, iHeight))

     override def paintComponent(graphics: Graphics) {
         graphics.drawImage(image, 0, 0, null)
     }
 }