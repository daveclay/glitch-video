package net.retorx.glitchvideo.player

import com.xuggle.mediatool.ToolFactory

class Player(filename: String) {

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

object Player {
    def play(filename: String) {
        playAfter(filename, 0)
    }

    def playAfter(filename: String, seconds: Int) {
        val player = new Player(filename)
        player.playAfterWaiting(seconds)
    }
}

