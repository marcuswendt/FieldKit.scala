/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 25, 2010 */
package field.kit.math

trait Randomness {
	import java.util.Random
	
	/** the default random generator */
	final var defaultRandom = new Random()
	
	// Float
	final def random:Float = defaultRandom.nextFloat
	
	final def random(r:Random):Float = 
		(if(r==null) defaultRandom else r).nextFloat
	
	final def random(min:Float, max:Float, r:Random = null):Float =
		random(r) * (max - min) + min
	
	// Normalized
	final def randomNormal:Float = random * 2f - 1f
	
	final def randomNormal(r:Random = null):Float = random(r) * 2f - 1f
		
	// Boolean
	final def flipCoin(chance:Float = 0.5f, r:Random = null):Boolean = 
		random(r) < chance
}