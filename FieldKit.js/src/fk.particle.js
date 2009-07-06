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
	System: function(_updateTicks, _logicTicks) {
		this.updateTicks = _updateTicks
		this.logicTicks = _logicTicks
		this.friction = 0.97
		this.flocks = new Array()

		var timerSim = new Date()
		var timerLogic = new Date()

		this.update = function() {
			var now = new Date()
			var dtSim = now - timerSim
			timerSim = now

			// update logic
			var dtLogic = now - timerLogic
			if(dtLogic > 1000/this.logicTicks) {
				timerLogic = now
				for(var i=0; i<this.flocks.length; i++)
					this.flocks[i].updateLogic(dtLogic)
			}
			
			// update simulation
			for(var i=0; i<this.flocks.length; i++)
				this.flocks[i].updateSimulation(dtSim)
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
		this.position = new fk.math.Vec2()
		this.behaviours = new Array()
		this.rate = 1
		this.interval = 1000.0
		this.particleMax = 100

		// private
		var time = 0.0

		this.updateLogic = function(dt) {
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

		this.updateLogic = function(dt) {
			// update emitter
			this.emitter.updateLogic(dt)

			// prepare behaviours
			for(var i=0; i<this.behaviours.length; i++)
				this.behaviours[i].prepare(dt)

			// update particles
			for(var i=0; i<this.particles.length; i++) {
				var p = this.particles[i]

				for(var j=0; j<this.behaviours.length; j++)
					this.behaviours[j].apply(p, dt)

				p.updateLogic(dt)
			}
		}
		
		this.updateSimulation = function(dt) {
			for(var i=0; i<this.particles.length; i++)
				this.particles[i].updateSimulation(dt)
		}

		this.add = function(behaviour) { this.behaviours.push(behaviour) }

		this.toString = function() { return 'Flock' }
	},

	Particle: function(_flock) {
		var P = function(_flock) {
			this.flock = _flock
	 		this.position = new fk.math.Vec2()
			this.velocity = new fk.math.Vec2()
			this.steer = new fk.math.Vec2()
			this.age = 0.0
			this.steerMax = 1.0
			this.velocityMax = 10
			this.rotation = 0.0
			this.turningSpeed = 0.1

			this.size = 10

			// temp fields
			this.theta = 0
			this.absVelocity = new fk.math.Vec2()
		}

		P.prototype.init = function() {}

		P.prototype.updateLogic = function(dt) {
			this.age += dt
			this.steer.clamp(this.steerMax)
			this.velocity.addV(this.steer).clamp(this.velocityMax)
			this.steer.zero()
			this.theta = -Math.atan2(this.velocity.x, this.velocity.y)
		}
		
		P.prototype.updateSimulation = function(dt) {
			this.absVelocity.setV(this.velocity).mulS(dt / this.flock.ps.updateTicks)
			
			this.position.addV(this.absVelocity)
			this.velocity.mulS(this.flock.ps.friction)
			this.rotation = fk.math.slerpAngle(this.rotation, this.theta, this.turningSpeed)
		}

		return new P(_flock)
	},
}
