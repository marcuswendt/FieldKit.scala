/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created February 23, 2010 */
package field.kit.physics

import field.kit._
import field.kit.math.geometry._

/**
 * 3D Verlet Particle class with support for multiple behaviours, finite state handling, colour integration
 * This is 
 */
class Particle extends Vec3 with Behavioural {
	
	/**
	 * Creates a new particle at the given position 
	 */
	def this(v:Vec3) {
		this()
		init(v)
	}
	
	/**
	 * Advances the particle one timestep
	 */
	def update(dt:Float) {
		updateState(dt)
		if(_isLocked) return
		updatePosition
		updateBounds
		
		applyBehaviours
		applyConstraints
	}
	
	// -- Weights --------------------------------------------------------------
	def weight = _weight
	def weight_=(value:Float) {
		_weight = value
		_invWeight = 1f / value
	}
	
	def invWeight = _invWeight
	
	protected var _weight = 1f
	protected var _invWeight = 1f/ _weight
	
	// -- Verlet Integration ---------------------------------------------------
	def lock = _isLocked = true
	
	def unlock {
		clearVelocity
		_isLocked = false
	}

	def isLocked = _isLocked
	
	protected var _isLocked = false
	
	/** Steering force */
	val force = new Vec3

	/** Air resistance or fluid resistance, force opposed to the relative motion of this particle */
	var drag = 0.03f
	
	/** Time between updates */
	def timestep = _timestep
	def timestep_=(value:Float) {
		_timestep = value
		_timestepSq = value * value
	}
	protected var _timestep = 1f
	protected var _timestepSq = _timestep * _timestep
	
	val prev = new Vec3
	private val tmp = new Vec3
	
	/**
	 * Updates the position based on the previous position and the steering force 
	 */
	protected def updatePosition {
		tmp := this
		
		//this += (this - prev) + force * (1f - drag) * _timestepSq
		
//		force *= (1f - drag) *= _timestepSq
//		
//		this.x += (this.x - prev.x) + force.x
//		this.y += (this.y - prev.y) + force.y
//		this.z += (this.z - prev.z) + force.z
		
		force *= _timestepSq
		
		this.x += (this.x - prev.x) + force.x
		this.y += (this.y - prev.y) + force.y
		this.z += (this.z - prev.z) + force.z

		prev := tmp
		
		scaleVelocity(1f - drag)
		
		force.zero
	}
	
	def init(v:Vec3) {
		this := v
		clearVelocity
	}
	
	def clearVelocity {
		prev := this
	}
	
	def scaleVelocity(s:Float) = prev.interpolate(this, 1f - s)
	
	// -- Neighbours -----------------------------------------------------------
	import scala.collection.mutable.ArrayBuffer
	
	/** List to keep track of this particles neighbours */
	var neighbours:ArrayBuffer[Particle] = _
	
	/**
	 * Adds another particle as this particles neighbour
	 */
	def +=(p:Particle) = {
		if(neighbours == null)
			neighbours = new ArrayBuffer[Particle]
		neighbours += p
	}
	
	/**
	 * Clears neighbour list (if set)
	 */
	def clearNeighbours = {
		if(neighbours == null) 
			neighbours = new ArrayBuffer[Particle]
		
		neighbours.clear
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
	var bounds:BoundingVolume = _
	
	def size = if(bounds == null) 0f else bounds.size
	
	def size_=(value:Float) {
		if(bounds == null)
			bounds = createBounds
			
		bounds.size = value
	}
	
	protected def createBounds = new Sphere(this, 0f)
	
	/**
	 * Updates the bounding box (if set)
	 */
	protected def updateBounds {
		if(bounds == null) return
		bounds := this
	}
	
	// -- Colour ---------------------------------------------------------------
	var colour:Colour = _
	
//	protected def updateColour {
//		this.x = 2*x - prev.x + steer.x * _timestepSq
//		this.y = 2*y - prev.y + steer.y * _timestepSq
//		this.z = 2*z - prev.z + steer.z * _timestepSq
//		prev := this
//	}	
	
	// -- Behaviours -----------------------------------------------------------
	
	/**
	 * Applies all assigned behaviours to this particle
	 */
	def applyBehaviours {
		if(behaviours == null) return
		var i = 0
		while(i < behaviours.length) {
			behaviours(i).apply(this)				
			i += 1
		}
	}
	
	// -- Constraints ----------------------------------------------------------
	/**
	 * Applies all assigned constraints to this particle
	 */
	def applyConstraints {
		if(constraints == null) return
		var i = 0
		while(i < constraints.length) {
			constraints(i).apply(this)				
			i += 1
		}
	}
}
