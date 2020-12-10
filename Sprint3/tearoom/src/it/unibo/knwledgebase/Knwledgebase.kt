/* Generated by AN DISI Unibo */ 
package it.unibo.knwledgebase

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Knwledgebase ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "handleUpdate"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("handleUpdate") { //this:State
					action { //it:State
						println("Knowledge base ready to receive updates.")
					}
					 transition(edgeName="t027",targetState="setStatus",cond=whenDispatch("updateStatus"))
				}	 
				state("setStatus") { //this:State
					action { //it:State
						println("Knowledge base setting the state.")
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("updateStatus(STATUS)"), Term.createTerm("updateStatus(STATUS)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												
												val Status = payloadArg(0)
												
								println("HHHHHHH $Status")
						}
						println("FUORI")
					}
					 transition( edgeName="goto",targetState="handleUpdate", cond=doswitch() )
				}	 
			}
		}
}