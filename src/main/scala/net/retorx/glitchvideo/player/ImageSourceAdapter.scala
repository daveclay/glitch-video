package net.retorx.glitchvideo.player

import com.xuggle.mediatool.MediaToolAdapter
import java.awt.image.BufferedImage
import com.xuggle.mediatool.event.{IAddStreamEvent, IVideoPictureEvent}
import scala.collection.mutable

class ImageSourceAdapter extends MediaToolAdapter with ImageSource {

    val frameImages = new mutable.ListBuffer[BufferedImage]()

    def getImageAt(index: Int): BufferedImage = {
        if (frameImages.size > index) {
            frameImages(index)
        } else {
            null
        }
    }

    override def onAddStream(event: IAddStreamEvent) {
        event.getSource.getContainer.getDuration
        super.onAddStream(event)
    }

    override def onVideoPicture(event: IVideoPictureEvent) {
        frameImages += event.getImage
        super.onVideoPicture(event)
    }
}