package net.retorx.glitchvideo.util

trait FrameInfo {

    var start = 0L
    var frameCount = 0

    def initFrameInfo() {
        start = System.currentTimeMillis()
    }

    def updateFrameInfo() {
        frameCount += 1
        if (frameCount % 30 == 0) {
            val now = System.currentTimeMillis()
            val average = (now - start) / frameCount
            val fps = 1000 / average
            println(frameCount + " frames " + (now - start) + "ms @ " + average + " per frame " + fps + " fps")
        }
    }

}