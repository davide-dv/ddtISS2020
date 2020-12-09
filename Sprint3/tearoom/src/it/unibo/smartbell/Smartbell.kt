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
						updateResourceRep( """{"state":"handleRequest"}"""  
						)
						emit("updateSmartbellHandleRequest", "updateSmartbellHandleRequest(X)" ) 
						delay(2000) 
					}
					 transition(edgeName="t014",targetState="checkTemperature",cond=whenDispatch("notify"))
					transition(edgeName="t015",targetState="showWaiterResponse",cond=whenReply("accept"))
					transition(edgeName="t016",targetState="showWaiterResponse",cond=whenReply("inform"))
				}	 
				state("checkTemperature") { //this:State
					action { //it:State
						println("Smart Bell: Checking client temperature")
						 var Temperature = 36  
						if(  Temperature < 37.5  
						 ){ CurrentID ++  
						updateResourceRep( """{"state":"checkTemperature","clientID":$CurrentID,"msg":"Temperature OK"}"""  
						)
						emit("updateCheckTemperature", "updateCheckTemperature($CurrentID,Temperature_OK)" ) 
						request("enterRequest", "enterRequest($CurrentID)" ,"waiter" )  
						}
						else
						 {println("Smart Bell: Client temperature is too high!: $Temperature")
						 updateResourceRep( """{"state":"checkTemperature","clientID":$CurrentID,"msg":"Temperature too high"}"""  
						 )
						 emit("updateCheckTemperature", "updateCheckTemperature($CurrentID,Temperature_too_High)" ) 
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
								updateResourceRep( """{"state":"showWaiterResponse","clientID":$CurrentID,"msg":"Enter Now"}"""  
								)
								emit("updateWaiterResponse", "updateWaiterResponse($CurrentID,Enter_Now)" ) 
						}
						if( checkMsgContent( Term.createTerm("inform(MAX_WAIT)"), Term.createTerm("inform(MAX_TIME)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val MaxTime = payloadArg(0)  
								updateResourceRep( """{"state":"showWaiterResponse","clientID":$CurrentID,"msg":"Wait for $MaxTime"}"""  
								)
								emit("updateWaiterResponse", "updateWaiterResponse($CurrentID,$MaxTime)" ) 
						}
						delay(1000) 
					}
					 transition( edgeName="goto",targetState="handleRequest", cond=doswitch() )
				}	 
			}
		}
}
