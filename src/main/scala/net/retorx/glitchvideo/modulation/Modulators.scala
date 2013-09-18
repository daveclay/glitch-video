package net.retorx.glitchvideo.modulation

import net.retorx.glitchvideo.util.SineWave._
import net.retorx.glitchvideo.util.RandomShit

class ModulationSynchronizer {

    var modulators = List[Modulation[AnyRef]]()

    def add(modulation: Modulation[AnyRef]) = {
        modulators = modulators ++ List(modulation)
        modulation
    }

    // Todo: the tick might need to take in the pixel info or frame image info in order to base any modulation
    // values from the context of the video (like width and height)
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
trait Modulation[T] extends ValueSource[T] {
    def tick()
}

/**
 * A Modulation that does nothing on a tick notification
 */
trait StaticValue[T] extends Modulation[T] {
    def tick() {}
}

/**
 * An Int value that does not change (no modulation)
 */
class StaticIntModulation(value: Int) extends StaticValue[Int] {
    def currentValue() = value
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

class RandomBooleanModulation() extends Modulation[Boolean] with RandomShit {

    var value = false

    def tick() {
        value = random.nextBoolean()
    }

    def currentValue() = value
}

class OccasionallyRandomBooleanModulation(percentageOfTime: PercentageOfTime) extends Modulation[Boolean] with RandomShit {

    var value = false

    def tick() {
        if (percentageOfTime()) {
            value = random.nextBoolean()
        }
    }

    def currentValue() = value
}

/**
 * A Modulation that occasionally changes values from some other ModulatedIntSource
 * @param percentageOfTime
 * @param value
 */
class OccasionallyRandomIntModulation(percentageOfTime: PercentageOfTime,
                                      value: ModulatedValue[Int]) extends Modulation[Int] with RandomShit {
    var theValue = 0

    def tick() {
        if (percentageOfTime()) {
            // Todo: do I tick the ModulatedIntSource as well? OR is that taken care of? If someone else
            // does the ticking, then the ticks will continue regardless of whether I use the value, resulting
            // in the values jumping more than I want. Imight want to only tick when occasionally picking the
            // next value. But that means I have to change how ticks are handled
            theValue = value()
        }
    }

    def currentValue() = theValue
}

class RandomIntModulation(lowest: ModulatedValue[Int],
                          highest: ModulatedValue[Int]) extends Modulation[Int] with RandomShit {

    def this(lowest: Int, highest: Int) {
        this(new ModulatedIntValue(lowest), new ModulatedIntValue(highest))
    }

    var value = 0

    def tick() {
        val low = lowest()
        val high = highest()
        value = low + random.nextInt(high - low)
    }

    def currentValue() = value
}

class LFOIntModulation(period: ModulatedValue[Int],
                       scale: ModulatedValue[Int]) extends Modulation[Int] {

    def this(period: Int, scale: Int) {
        this(new ModulatedIntValue(period), new ModulatedIntValue(scale))
    }

    var count = 0
    var value = 0

    def currentValue() = value

    def tick() {
        value = getValue(count, period(), scale())
        count += 1
        if (count >= period()) count = 0
    }
}

