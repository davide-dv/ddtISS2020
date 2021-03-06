/* Generated by AN DISI Unibo */ 
package it.unibo.timer1

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Timer1 ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "handleRequest"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 
				val Table = 1
				val MaxStayTime = 30000L	
		return { //this:ActionBasciFsm
				state("handleRequest") { //this:State
					action { //it:State
						println("Timer1: waiting for requests")
						updateResourceRep( """{"state":"handleRequest"}"""  
						)
					}
					 transition(edgeName="t03",targetState="enable",cond=whenDispatch("startTimer1"))
				}	 
				state("enable") { //this:State
					action { //it:State
						println("Timer1: Enabling the timer")
						updateResourceRep( """{"state":"enable"}"""  
						)
						stateTimer = TimerActor("timer_enable", 
							scope, context!!, "local_tout_timer1_enable", MaxStayTime )
					}
					 transition(edgeName="t14",targetState="notifyTimeElapsed",cond=whenTimeout("local_tout_timer1_enable"))   
					transition(edgeName="t15",targetState="handleRequest",cond=whenDispatch("stopTimer1"))
				}	 
				state("notifyTimeElapsed") { //this:State
					action { //it:State
						println("Timer1: notifying time elapsed")
						updateResourceRep( """{"state":"notifyTimeElapsed"}"""  
						)
						forward("maxStayTimeExpired", "maxStayTimeExpired($Table)" ,"waiter" ) 
					}
					 transition( edgeName="goto",targetState="handleRequest", cond=doswitch() )
				}	 
			}
		}
}
