/* Generated by AN DISI Unibo */ 
package it.unibo.timer0

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Timer0 ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "handleRequest"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 
				val Table = 0
				val MaxStayTime = 30000L	
		return { //this:ActionBasciFsm
				state("handleRequest") { //this:State
					action { //it:State
						println("Timer0: waiting for requests")
						updateResourceRep( """{"state":"handleRequest"}"""  
						)
					}
					 transition(edgeName="t021",targetState="enable",cond=whenDispatch("startTimer0"))
				}	 
				state("enable") { //this:State
					action { //it:State
						println("Timer0: Enabling the timer")
						updateResourceRep( """{"state":"enable"}"""  
						)
						stateTimer = TimerActor("timer_enable", 
							scope, context!!, "local_tout_timer0_enable", MaxStayTime )
					}
					 transition(edgeName="t122",targetState="notifyTimeElapsed",cond=whenTimeout("local_tout_timer0_enable"))   
					transition(edgeName="t123",targetState="handleRequest",cond=whenDispatch("stopTimer0"))
				}	 
				state("notifyTimeElapsed") { //this:State
					action { //it:State
						println("Timer0: notifying time elapsed")
						updateResourceRep( """{"state":"notifyTimeElapsed"}"""  
						)
						forward("maxStayTimeExpired", "maxStayTimeExpired($Table)" ,"waiter" ) 
					}
					 transition( edgeName="goto",targetState="handleRequest", cond=doswitch() )
				}	 
			}
		}
}
