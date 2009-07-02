/*!
 * FieldKit JavaScript Library
 * http://code.google.com/p/fieldkit
 *
 * Copyright (c) 2009 Marcus Wendt
 * Licensed under the LGPL license.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Date: June 20, 2009
 */

// =============================================================================
// SUBSUMPTION PARTICLE SYSTEM
// =============================================================================
fk.particle = {
	// -- Particle System --------------------------------------------------------
	System: function(_timeStep) {
		this.timeStep = _timeStep
		this.friction = 0.97
		this.flocks = new Array()
		
		var timer = new Date()

		this.update = function() {
			var now = new Date()
			var dt = now - timer
			timer = now
	
			for(var i=0; i<this.flocks.length; i++)
				this.flocks[i].update(dt)
		}

		this.add = function(flock) { 
			flock.ps = this
			this.flocks.push(flock) 
		}
		
		this.width = function() { return document.documentElement.clientWidth }
		
		this.height = function() { return document.documentElement.clientHeight }
	},

	Emitter: function(_flock) {
		this.flock = _flock
		this.position = new fk.math.Vec()
		this.behaviours = new Array()
		this.rate = 1
		this.interval = 1000.0
		this.particleMax = 100
		
		// private
		var time = 0.0
		
		this.update = function(dt) {
			time += dt
			
			if(time >= this.interval) {
				time = 0
				
				if(this.emissionCount() > 0) {
					// prepare behaviours
					for(var i=0; i<this.behaviours.length; i++)
						this.behaviours[i].prepare(dt)
				
					for(var i=0; i<this.rate; i++)
						if(this.emissionCount() > 0)
							this.emit()
				}
			}
		}
		
		this.emissionCount = function() {
			return this.particleMax - this.flock.particles.length
		}
		
		this.emit = function() {
			var p = new fk.particle.Particle(this.flock)
			p.position.setV(this.position)
			p.init()
			
			// apply behaviours
			for(var i=0; i<this.behaviours.length; i++)
				this.behaviours[i].apply(p, 0)
				
			// add particle to flock			
			this.flock.particles.push(p)
		}
		
		this.add = function(behaviour) { this.behaviours.push(behaviour) }
	},
	
	Flock: function() {
		this.ps = null
		this.particles = new Array()
		this.behaviours = new Array()
		this.emitter = new fk.particle.Emitter(this)
		
		this.update = function(dt) {
			// update emitter
			this.emitter.update(dt)
			
			// prepare behaviours
			for(var i=0; i<this.behaviours.length; i++)
				this.behaviours[i].prepare(dt)
				
			// update particles
			for(var i=0; i<this.particles.length; i++) {
				var p = this.particles[i]
				
				for(var j=0; j<this.behaviours.length; j++)
					this.behaviours[j].apply(p, dt)
					
				p.update(dt)
			}
		}
		
		this.add = function(behaviour) { this.behaviours.push(behaviour) }
		
		this.toString = function() { return 'Flock' }
	},

	Particle: function(_flock) {
		var P = function(_flock) {
			this.flock = _flock
	 		this.position = new fk.math.Vec()
			this.velocity = new fk.math.Vec()
			this.steer = new fk.math.Vec()
			this.age = 0.0
			this.steerMax = 1.0
			this.velocityMax = 10
			this.rotation = 0.0
			this.turningSpeed = 0.2
		
			this.size = 10
		
			// temp fields
			this.absVelocity = new fk.math.Vec()
		}

		P.prototype.init = function() {}

		P.prototype.update = function(dt) {
			this.age += dt
			
			// update driving
			this.steer.clamp(this.steerMax)
			this.velocity.addV(this.steer).clamp(this.velocityMax)

			// this.absVelocity.setV(this.velocity).mulS( dt / this.flock.ps.timeStep )
			// this.position.addV(this.absVelocity)
			
			this.position.addV(this.velocity)
			
			this.velocity.mulS( this.flock.ps.friction )
			this.steer.zero()
			
			// update rotation
			var theta = -Math.atan2(this.velocity.x, this.velocity.y)
			this.rotation = fk.math.slerpAngle(this.rotation, theta, this.turningSpeed)
			//this.rotation = theta
		}
		
		return new P(_flock)
	},
}

// load behaviour library
// fk.include('fk.behaviour')

