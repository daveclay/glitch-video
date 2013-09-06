package net.retorx.glitchvideo.player

import com.xuggle.mediatool.IMediaReader

object PlayMedia {

    def play(mediaReader: IMediaReader) {
        while (mediaReader.readPacket() == null) {}
    }
}