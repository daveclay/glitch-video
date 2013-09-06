package net.retorx.glitchvideo.util

import java.util.{Date, Random}

trait RandomShit {
    val random = new Random(new Date().getTime)
}