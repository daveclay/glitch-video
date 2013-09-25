package net.retorx.glitchvideo.modulation

import net.retorx.glitchvideo.util.SineWave._
import net.retorx.glitchvideo.util.{SineWave, RandomShit}
import java.util.Random

case class SyncData(frameId: Long, pixelId: Long, colorBandId: Long, arbitraryData: Map[String, Long])

/**
 * A ValueSource is a variable; this interface is exposed to the effects that have "inputs" or required values
 * to apply the effect. A separate interface ensures effect code adheres to a simple value API, rather than being
 * exposed to the internal modulation APIs.
 */
trait ValueSource[T] {
    def getValue(syncData: SyncData): T
}

trait SyncType

/**
 * Sync based on every X frames
 */
object FrameSync extends SyncType

/**
 * Sync based on every X pixels
 */
object PixelSync extends SyncType

/**
 * Sync based on every X color bands
 */
object ColorBandSync extends SyncType

/**
 * Sync based on a free-running timer
 */
object FreeTimeSync extends SyncType

/**
 * Sync defined by an effect (e.g. the effect defines regions of the video frame, and can be synced based on every
 * X region). This would use the arbitrary data Map in the SyncData, provided by the effect.
 */
object EffectSync extends SyncType

/**
 * Information about the sync type
 */
trait SyncConfig {
    def getSyncType: SyncType
}

/**
 * A Modulation is a ValueSource that receives a tick notification to change
 */
abstract class Modulator[T] extends ValueSource[T] {
    var syncType: SyncType = null

    def getSyncType = syncType

    def setSyncType(syncType: SyncType) {
        this.syncType = syncType
    }

    def rngWithSeed(seed: Long) = new Random(seed)

    def hi(syncData: SyncData) {
        syncType match {
            case FrameSync => rngWithSeed(syncData.frameId << 64)
            case PixelSync => rngWithSeed(syncData.frameId << 64 | syncData.pixelId << 64)
            case ColorBandSync =>
            case FreeTimeSync =>
            case EffectSync =>
        }
    }

    def getValue(syncData: SyncData):T
}

class StaticBooleanModulator(staticValue: Boolean) extends Modulator[Boolean] {
    def getValue(syncData: SyncData): Boolean = staticValue
}
/**
 * An Int value that does not change (no modulation)
 */
class StaticIntModulator(staticValue: Int) extends Modulator[Int] {
    def getValue(syncData: SyncData): Int = staticValue
}

class PercentageOfTime(initialNumber: ModulatedValue[Int],
                       initialOutOfTotal: ModulatedValue[Int]) extends RandomShit {

    // 999,999 out of 1,000,000
    var number = initialNumber
    var outOfTotal = initialOutOfTotal

    def setOutOfTotal(total: ModulatedValue[Int]) {
        outOfTotal = total
    }

    def setNumber(number: ModulatedValue[Int]) {
        this.number = number
    }

    def apply(syncData: SyncData) = get(syncData)

    def get(syncData: SyncData) = {
        number(syncData) > random.nextInt(outOfTotal(syncData))
    }
}

class RandomBooleanModulator extends Modulator[Boolean] {

    def getValue(syncData: SyncData): Boolean = false

    def calculateValue() {
        // value = random.nextBoolean()
    }
}

class OccasionallyRandomBooleanModulator(percentageOfTime: PercentageOfTime) extends Modulator[Boolean] with RandomShit {

    def getValue(syncData: SyncData): Boolean = {
        if (percentageOfTime(syncData)) {
            random.nextBoolean()
        }
        false
    }
}

/**
 * A Modulation that occasionally changes values from some other ModulatedIntSource
 * @param percentageOfTime
 * @param modulatedValue
 */
class OccasionallyRandomIntModulator(percentageOfTime: PercentageOfTime,
                                     modulatedValue: ModulatedIntValue) extends Modulator[Int] with RandomShit {

    def getValue(syncData: SyncData) = {
        if (percentageOfTime(syncData)) {
            // Todo: do I tick the ModulatedIntSource as well? OR is that taken care of? If someone else
            // does the ticking, then the ticks will continue regardless of whether I use the value, resulting
            // in the values jumping more than I want. I might want to only tick when occasionally picking the
            // next value. But that means I have to change how ticks are handled
            modulatedValue(syncData)
        }
        0
    }

}

class RandomIntModulator(lowest: ModulatedIntValue,
                         highest: ModulatedIntValue) extends Modulator[Int] with RandomShit {

    def this(lowest: Int, highest: Int) {
        this(new ModulatedIntValue(lowest), new ModulatedIntValue(highest))
    }

    def getValue(syncData: SyncData): Int = {
        val low = lowest(syncData)
        val high = highest(syncData)
        val maxRandom = high - low
        if (maxRandom == 0) {
            0
        } else {
            low + random.nextInt(maxRandom)
        }
    }
}

class LFOIntModulator(period: ModulatedIntValue,
                       scale: ModulatedIntValue) extends Modulator[Int] {

    def this(period: Int, scale: Int) {
        this(new ModulatedIntValue(period), new ModulatedIntValue(scale))
    }

    var count = 0

    def getValue(syncData: SyncData): Int = {
        val next = SineWave.getValue(count, period(syncData), scale(syncData))
        count += 1
        if (count >= period(syncData)) count = 0
        next
    }
}

