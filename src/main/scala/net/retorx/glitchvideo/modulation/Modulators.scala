package net.retorx.glitchvideo.modulation

import net.retorx.glitchvideo.util.SineWave._
import net.retorx.glitchvideo.util.RandomShit

class ModulationSynchronizer {

    var modulators = List[Modulator[AnyRef]]()

    def add(modulation: Modulator[AnyRef]) = {
        modulators = modulators ++ List(modulation)
        modulation
    }

    // Todo: the tick might need to take in the pixel info or frame image info in order to base any modulation
    // values from the context of the video (like width and height). Or a separate initialization, or FrameContext
    // update call.
    def tick() {
        modulators.foreach(modulator => {
            modulator.tick()
        })
    }
}

/**
 * A ValueSource is a variable
 */
trait ValueSource[T] {
    def currentValue(): T
}

/**
 * A Modulation is a ValueSource that receives a tick notification to change
 */
abstract class Modulator[T] extends ValueSource[T] {

    var tickEveryValue: ModulatedBooleanValue = null.asInstanceOf[ModulatedBooleanValue]
    var value:T = null.asInstanceOf[T]

    def tick() {
        calculateValue()
    }

    def currentValue():T = {
        if (tickEveryValue != null && tickEveryValue()) {
            tick()
        }
        value
    }

    def calculateValue()
}

class StaticBooleanModulator(staticValue: Boolean) extends Modulator[Boolean] {
    value = staticValue
    def calculateValue() {}
}
/**
 * An Int value that does not change (no modulation)
 */
class StaticIntModulator(staticValue: Int) extends Modulator[Int] {
    value = staticValue
    def calculateValue() {}
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

    def apply() = get()

    def get() = {
        number() > random.nextInt(outOfTotal())
    }
}

class RandomBooleanModulator extends Modulator[Boolean] with RandomShit {
    value = false

    def calculateValue() {
        value = random.nextBoolean()
    }
}

class OccasionallyRandomBooleanModulator(percentageOfTime: PercentageOfTime) extends Modulator[Boolean] with RandomShit {
    value = false

    def calculateValue() {
        if (percentageOfTime()) {
            value = random.nextBoolean()
        }
    }
}

/**
 * A Modulation that occasionally changes values from some other ModulatedIntSource
 * @param percentageOfTime
 * @param modulatedValue
 */
class OccasionallyRandomIntModulator(percentageOfTime: PercentageOfTime,
                                     modulatedValue: ModulatedValue[Int]) extends Modulator[Int] with RandomShit {
    def calculateValue() {
        if (percentageOfTime()) {
            // Todo: do I tick the ModulatedIntSource as well? OR is that taken care of? If someone else
            // does the ticking, then the ticks will continue regardless of whether I use the value, resulting
            // in the values jumping more than I want. I might want to only tick when occasionally picking the
            // next value. But that means I have to change how ticks are handled
            value = modulatedValue()
        }
    }
}

class RandomIntModulator(lowest: ModulatedValue[Int],
                         highest: ModulatedValue[Int]) extends Modulator[Int] with RandomShit {

    def this(lowest: Int, highest: Int) {
        this(new ModulatedIntValue(lowest), new ModulatedIntValue(highest))
    }

    value = 0

    def calculateValue() {
        val low = lowest()
        val high = highest()
        val maxRandom = high - low
        if (maxRandom == 0) {
            value = 0
        } else {
            value = low + random.nextInt(maxRandom)
        }
    }
}

class LFOIntModulator(period: ModulatedValue[Int],
                       scale: ModulatedValue[Int]) extends Modulator[Int] {

    def this(period: Int, scale: Int) {
        this(new ModulatedIntValue(period), new ModulatedIntValue(scale))
    }

    var count = 0
    value = 0

    def calculateValue() {
        value = getValue(count, period(), scale())
        count += 1
        if (count >= period()) count = 0
    }
}

