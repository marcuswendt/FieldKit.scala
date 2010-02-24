/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created February 23, 2010 */
package field.kit.physics

import field.kit._
import field.kit.math.geometry.AABB

/**
 * 3D Verlet Particle class with support for multiple behaviours, finite state handling, colour integration
 * This is 
 */
class Particle extends Vec3 with Updateable with Behavioural {
		
	/**
	 * Creates a new particle at the given position 
	 */
	def this(v:Vec3) {
		this()
		reset(v)
	}
	
	/**
	 * Advances the particle one timestep
	 */
	def update(dt:Float) {
		updateState(dt)
		if(_isLocked) return
		updatePosition
		updateBounds
		updateBehaviours
	}
	
	// -- Verlet Integration ---------------------------------------------------
	protected var _isLocked = false
	
	def lock = _isLocked = true
	
	def unlock {
		clearVelocity
		_isLocked = false
	}
	
	/** Steering force */
	val steer = new Vec3
	
	def timestep = _timestep
	def timestep_=(value:Float) {
		_timestep = value
		_timestepSq = value * value
	}
	protected var _timestep = 1f
	protected var _timestepSq = _timestep * _timestep
	
	private val prev = new Vec3
	
	/**
	 * Updates the position based on the previous position and the steering force 
	 */
	protected def updatePosition {
		this.x = 2*x - prev.x + steer.x * _timestepSq
		this.y = 2*y - prev.y + steer.y * _timestepSq
		this.z = 2*z - prev.z + steer.z * _timestepSq
		prev := this
	}
	
	def reset(v:Vec3) {
		this := v
		clearVelocity
	}
	
	def clearVelocity {
		prev := this
	}
	
	// -- State Machine --------------------------------------------------------
	var age = 0f
	var state = 0
	
	/**
	 * For now simply keeps track of the particle's age; override this method to
	 * perform custom state changes e.g. fading in, alive, fading out etc.
	 */
	protected def updateState(dt:Float) {
		this.age += dt
	}

	// -- Boundaries -----------------------------------------------------------
	var bounds:AABB = _
	
	def extent:Vec3 = {
		if(bounds == null) return new Vec3()
		bounds.extent
	}
	
	def extent_=(value:Vec3) {
		if(bounds == null) bounds = new AABB
		bounds.extent = value
	}
	
	def extent_=(value:Float) {
		if(bounds == null) bounds = new AABB
		bounds.extent = value
	}
	
	/**
	 * Updates the bounding box (if set)
	 */
	protected def updateBounds {
		if(bounds == null) return
		bounds := this
	}
	
	// -- Colour ---------------------------------------------------------------
	protected var _colour:Colour = _
	
//	protected def updateColour {
//		this.x = 2*x - prev.x + steer.x * _timestepSq
//		this.y = 2*y - prev.y + steer.y * _timestepSq
//		this.z = 2*z - prev.z + steer.z * _timestepSq
//		prev := this
//	}
	
	// -- Weights --------------------------------------------------------------
	// TODO add weights when implementing springs
//	def weight = _weight
//	def weight_=(value:Float) {
//		_weight = value
//		_invWeight = 1f / value
//	}
//	protected var _weight = 1f
//	protected var _invWeight = 1f/ _weight
	
	// -- Behaviours -----------------------------------------------------------
	/**
	 * Applies all assigned behaviours to this particle
	 */
	protected def updateBehaviours {
		if(behaviours == null) return
		var i = 0
		while(i < behaviours.length) {
			behaviours(i).apply(this)				
			i += 1
		}
	}
}
