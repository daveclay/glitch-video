package net.retorx.glitchvideo.util

object SineWave {

    /**
     * Just scale a sine wave function to the period and scale desired
     * @param count an incrementing counter
     * @param period the length of the oscillation
     * @param scale the maximum value
     * @return a value between 0 and the provided maximum based on the count in the period.
     */
    def getValue(count: Int, period: Int, scale: Int) = {
        (Math.sin(count * 2 * Math.PI / period) * (scale / 2) + (scale / 2)).toInt
    }

}