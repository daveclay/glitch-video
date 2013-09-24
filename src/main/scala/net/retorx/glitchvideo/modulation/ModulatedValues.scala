package net.retorx.glitchvideo.modulation

/**
 * A ModulatedValue represents a variable that has an associated ValueSource (typically a Modulation) that can
 * be changed. Glitch functions that require variables that change their behavior use these rather than primitive values
 * allowing their ValueSources (and thus the specific variable values) to be modulated without having to modify
 * the functions themselves.
 *
 * So, while these may be stupidly simple, they provide a mechanism for modulating variables without having to write
 * modulation logic in each glitch function. These abstract away the modulation from the glitch through the variables
 * the glitch functions define and require.
 *
 * This can be evidenced by the refactoring of the set of ShiftColorBands classes. These all inhereted from an abstract
 * superclass that performed the shifting of each r, g, and b pixels by some distance. By shifting them by some static
 * distance, then we get just a shifting of each frame. Another class added 1 to each distance each frame, causing
 * a sort of drift effect of the color band. Another class shifted the pixels by some random amount each frame, which
 * causes the color bands to jump around. And finally, other class randomly shifted the pixel distance for every
 * pixel, which really messes with the frame.
 *
 * The sillyness is that all of these simply perform the same underlying function/logic: shifting individual
 * r, g, b pixels by some distance.
 *
 * By introducing a modulated value rather than implementing the various random logic in subclasses, the modulated
 * value encapsulates the logic of how these values are changed (either randomly, lfo, or static). The underlying
 * logic is the only thing left; e.g. there is only one class which shifts pixels based on some distance, while
 * the logic for changing that distance is pulled out into the various modulations available.
 *
 */
trait ModulatedValue[T] {
    var valueSource: ValueSource[T] = null

    def value() =  valueSource.currentValue()
    def apply() = value()

    def setSource(source: ValueSource[T]) {
        this.valueSource = source
    }
}

class ModulatedIntValue(initialSource: ValueSource[Int]) extends ModulatedValue[Int] {
    def this(value: Int) {
        this(new StaticIntModulator(value))
    }

    setSource(initialSource)
}

class ModulatedBooleanValue(initialSource: ValueSource[Boolean]) extends ModulatedValue[Boolean] {

    def this(value: Boolean) {
        this(new StaticBooleanModulator(value))
    }
    setSource(initialSource)
}

/*

if every modulated value pulls/gets from the modulator, the question is when does the modulator update its tick? IS it
tied to frame rate or faster? Independent? User-controlled? Automation?

a get will work if we separate out the "ticking" of the modulator

a pull/get model requires the modulated variable to ask the modulator for its value (in this sense it is a facade
to the modulator itself). This is simple to implement... but what about switching the modulation itself?

what about a push? this would mean keeping a list of modulated variables in the modulator, and as it updates it sets
the new value in all the modulated variables.

get/pull means the association between variable and modulator is contained in the variable. a push means the
association is contained in the modulator.


which determines the range of values?
A thing that changes x values has a range that depends on the width, regardless
of how it is modulated.

What types of modulation are there?
LFO with a sine wave (or other wave shapes), linear increment or decrement, static value, user-automated values, step
sequencers, random. Maybe a combination... in the case of the sine wave random pixelation, the LFO isn't attached to
the actual value, it's attached to the another random source.

*/