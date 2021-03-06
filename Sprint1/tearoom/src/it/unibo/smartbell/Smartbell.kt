/* Generated by AN DISI Unibo */ 
package it.unibo.smartbell

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Smartbell ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "handleRequest"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 var CurrentID = 0  
		return { //this:ActionBasciFsm
				state("handleRequest") { //this:State
					action { //it:State
						println("Smart Bell: Start and listening")
						updateResourceRep( "handleRequest"  
						)
					}
					 transition(edgeName="t013",targetState="checkTemperature",cond=whenDispatch("notify"))
					transition(edgeName="t014",targetState="showWaiterResponse",cond=whenReply("accept"))
				}	 
				state("checkTemperature") { //this:State
					action { //it:State
						println("Smart Bell: Checking client temperature")
						 var Temperature = 35  
						if(  Temperature < 37.5  
						 ){ CurrentID ++  
						updateResourceRep( "$CurrentID --- Temperature_OK"  
						)
						request("enterRequest", "enterRequest($CurrentID)" ,"waiter" )  
						}
						else
						 {updateResourceRep( "$CurrentID --- Temperature too high"  
						 )
						 }
						delay(500) 
					}
					 transition( edgeName="goto",targetState="handleRequest", cond=doswitch() )
				}	 
				state("showWaiterResponse") { //this:State
					action { //it:State
						println("Smart Bell: Showing waiter response")
						if( checkMsgContent( Term.createTerm("accept(X)"), Term.createTerm("accept(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								updateResourceRep( "$CurrentID --- Enter Now"  
								)
						}
						if( checkMsgContent( Term.createTerm("inform(MAX_WAIT)"), Term.createTerm("inform(MAX_TIME)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								updateResourceRep( "$CurrentID --- Wait for " + payloadArg(0)  
								)
						}
						delay(1000) 
					}
					 transition( edgeName="goto",targetState="handleRequest", cond=doswitch() )
				}	 
			}
		}
}
