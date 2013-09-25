package net.retorx.glitchvideo.modulation

import java.util.Random
import scala.collection.mutable

/**
 * Todo:  When adding a glitch effect, the effect defines the available tick options to this thing. This thing
 * then makes the option available for select by the user (or a modulated selector value).
 *
 * options:
 * per color band in each pixel
 * per pixel
 * per column
 * per row
 * per section of frame (as defined by the effect's configuration, or some other frame sectioning device?)
 * per frame
 * arbitrary time period
 * random time period
 *
 * Todo: Do two effects wired to the same modulator get the same modulated value for a given option? How would that work?
 * It'd have to be more like the old api: each effect is called on a given pixel, on a given frame, etc.
 *
 * Todo: using a shared modulator, you could add in things that modify that modulator value (invert an lfo, for example?)
 */
class ModulationClock {

    var modulators = List[Modulator[AnyRef]]()

    def add(modulation: Modulator[AnyRef]) = {
        modulators = modulators ++ List(modulation)
        modulation
    }

    // Todo: the tick might need to take in the pixel info or frame image info in order to base any modulation
    // values from the context of the video (like width and height). Or a separate initialization, or FrameContext
    // update call.
    def tick(syncData: SyncData) {
        modulators.foreach(modulator => {
        })
    }
}

object RandomTest {
    val nums = 480 * 640 * 3 //  10000000

    def main(args: Array[String]) {
        testRepeating()
        //testTiming()
    }

    def testRepeating() {
        val seedRng = new Random(1234)
        for (i <- 0 to nums) {
            for (r <- 0 to 10) {
                var seed = seedRng.nextInt(1235208)
                val a = new Random(seed).nextInt(255)
                val b = new Random(seed).nextInt(255)
                if (a != b) {
                    throw new RuntimeException(" shit, " + a + " != " + b)
                }
            }
        }
    }

    def testTiming() {

        for (i <- 0 to 10) {
            testNewRandom()
            testSet()
            println("\n")
        }

    }

    def testSet() {
        var start = System.currentTimeMillis()
        val random = new Random()
        val used = new mutable.HashSet[Int]()
        for (i <- 0 to nums) {
            var hi = 0
            do {
                hi = random.nextInt(255)
            } while (used.contains(hi))
        }
        println("set: " + (System.currentTimeMillis() - start) + "ms")

    }

    def testNewRandom() {
        var start = System.currentTimeMillis()
        for (i <- 0 to nums) {
            var hi = new Random(i).nextInt(255)
        }

        println("rng: " + (System.currentTimeMillis() - start) + "ms")
    }
}