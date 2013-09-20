package net.retorx.glitchvideo.player

import com.xuggle.mediatool.ToolFactory

class XugglePlayer(filename: String) {

    def play() {
        playAfterWaiting(0)
    }

    def playAfterWaiting(seconds: Int) {
        val runnable = new Runnable() {
            def run() {
                Thread.sleep(seconds * 1000)
                playbackVideo(filename)
            }
        }
        val thread = new Thread(runnable)
        thread.start()
    }

    private def playbackVideo(filename: String) {
        val mediaReader = ToolFactory.makeReader(filename)
        mediaReader.addListener(ToolFactory.makeViewer())
        PlayMedia.play(mediaReader)
    }
}

object XugglePlayer {
    def play(filename: String) {
        playAfter(filename, 0)
    }

    def playAfter(filename: String, seconds: Int) {
        val player = new XugglePlayer(filename)
        player.playAfterWaiting(seconds)
    }
}

