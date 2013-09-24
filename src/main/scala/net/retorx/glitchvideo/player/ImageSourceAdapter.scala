package net.retorx.glitchvideo.player

import com.xuggle.mediatool.MediaToolAdapter
import java.awt.image.BufferedImage
import com.xuggle.mediatool.event.IVideoPictureEvent
import scala.collection.mutable

class ImageSourceAdapter extends MediaToolAdapter with ImageSource {

    val imageQueue = new mutable.Queue[BufferedImage]()
    var lastImage: BufferedImage = null

    def nextImage: BufferedImage = {
        if (imageQueue.size > 0) {
            lastImage = imageQueue.dequeue()
        }
        lastImage
    }

    override def onVideoPicture(event: IVideoPictureEvent) {
        imageQueue += event.getImage
        super.onVideoPicture(event)
    }
}