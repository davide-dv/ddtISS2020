package prova

import it.unibo.supports.FactoryProtocol
import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage

class connQakTcp(val hostIP : String, val port : String, val destName : String ) {
	lateinit var conn   : IConnInteraction
	
	fun createConnection( ){ //hostIP: String, port: String  
		val fp    = FactoryProtocol(null,"TCP","connQakTcp")
	    conn      = fp.createClientProtocolSupport(hostIP, port.toInt() )    
	}
	
	fun forward( msg: ApplMessage ){
 		conn.sendALine( msg.toString()  )				
	}
	
	fun request( msg: ApplMessage ){
 		conn.sendALine( msg.toString()  )
		//Acquire the answer	
		val answer = conn.receiveALine()
		println("connQakTcp | answer= $answer")		
	}
	
	fun emit( msg: ApplMessage ){
 		conn.sendALine( msg.toString()  )			
	}
	
	
	
	
	
}