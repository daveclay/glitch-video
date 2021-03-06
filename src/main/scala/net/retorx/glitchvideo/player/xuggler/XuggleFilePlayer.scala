package net.retorx.glitchvideo.player.xuggler

import com.xuggle.mediatool.ToolFactory
import net.retorx.glitchvideo.player.GlitchPlayer

class XuggleFilePlayer(filename: String) {

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
        XuggleMediaReaderHelper.startReading(mediaReader)
    }
}

object XuggleFilePlayer {
    def play(filename: String) {
        playAfter(filename, 0)
    }

    def playAfter(filename: String, seconds: Int) {
        val player = new XuggleFilePlayer(filename)
        player.playAfterWaiting(seconds)
    }
}

class XugglerVideoPlayer {

    def play(filename: String, outputFilename: String) {
        val glitchPlayer = new GlitchPlayer(outputFilename = outputFilename, useXugglerViewer = true)
        glitchPlayer.glitch(filename)
    }
}

