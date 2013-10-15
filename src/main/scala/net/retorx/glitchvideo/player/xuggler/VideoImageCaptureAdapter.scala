package net.retorx.glitchvideo.player.xuggler

import com.xuggle.mediatool.MediaToolAdapter
import scala.collection.mutable.ListBuffer
import java.awt.image.BufferedImage
import com.xuggle.mediatool.event.IVideoPictureEvent

class VideoImageCaptureAdapter extends MediaToolAdapter {

    var images = new ListBuffer[BufferedImage]()

    override def onVideoPicture(event: IVideoPictureEvent) {
        val image = event.getImage
        images += image
    }
}