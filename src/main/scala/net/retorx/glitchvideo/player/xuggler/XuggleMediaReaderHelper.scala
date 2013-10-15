package net.retorx.glitchvideo.player.xuggler

import com.xuggle.mediatool.IMediaReader

object XuggleMediaReaderHelper {

    def startReading(mediaReader: IMediaReader) {
        while (mediaReader.readPacket() == null) {}
    }
}