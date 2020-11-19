package test

import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.MqttUtils
import it.unibo.tearoom.Tearoom
import it.unibo.kactor.QakContext
import it.unibo.kactor.ApplMessage
import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.CoapResource
import it.unibo.kactor.CoapResourceCtx
import org.eclipse.californium.core.server.resources.ResourceObserver
import org.eclipse.californium.core.CoapHandler
import org.junit.Rule
import java.util.concurrent.CountDownLatch
import org.eclipse.californium.core.coap.MediaTypeRegistry
import connQak.connQakCoap
 

class testBasicrobot {

val initDelayTime     = 6000L
	
val context     = "ctxtearoom"
val destactor   = "smartbell"
val addr        = "192.168.1.125:8050"
val smartbell      = CoapClient()
val uriStr      = "coap://$addr/$context/$destactor"
var ContextQak  : QakContext? = null
//var connSmartbell : connQakCoap? = null
val waiter = CoapClient()
val uriWaiter      = "coap://192.168.1.125:8050/ctxtearoom/waiter"
	

 		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Before
	fun systemSetUp() {
		
   		kotlin.concurrent.thread(start = true) {
			it.unibo.ctxtearoom.main() 
			println("testTearoom systemSetUp done")
 	}
	Thread.sleep(3000)
	//connSmartbell = connQakCoap("localhost", "8050", "smartbell", "ctxtearoom")
	
}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@After
	fun terminate() {
		println("testTearoom terminated ")
	}


		

	
	

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi		
fun testCoap() {
	smartbell.setURI(uriStr)
	waiter.setURI(uriWaiter)
	runBlocking{
		delay(initDelayTime)			
	
		assertTrue(smartbell.get().getResponseText() == "handleRequest")					
		//MsgUtil.sendMsg(ApplMessage("msg(ring,request,test,smartbell,ring(a),19)") , smartbell!! )
		
		val msg : ApplMessage = MsgUtil.buildDispatch("test", "notify", "notify(X)", "smartbell")		
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)
		//println("Response: " + smartbell.get().getResponseText())
		assertTrue(smartbell.get().getResponseText() == "1 --- Temperature_OK")
		delay(1200)
		//println("RESPONSE: " + smartbell.get().getResponseText())		
		assertTrue(smartbell.get().getResponseText() == "1 --- Enter Now")		
		delay(1200)	
		assertTrue(smartbell.get().getResponseText() == "handleRequest")		
		delay(11000)
		//println("WAITER RESPONSE: " + waiter.get().getResponseText())
		assertTrue(waiter.get().getResponseText().contains("dirty") && waiter.get().getResponseText().contains("1"))
		val msgOrder : ApplMessage = MsgUtil.buildDispatch("test", "order", "order(Twinings, 1)", "waiter")
		waiter.put(msgOrder.toString(), MediaTypeRegistry.TEXT_PLAIN)
		delay(10000)	
		val msgPayment : ApplMessage = MsgUtil.buildDispatch("test", "payment", "payment(1)", "waiter")
		waiter.put(msgPayment.toString(), MediaTypeRegistry.TEXT_PLAIN)
		delay(10000)
		assertTrue(waiter.get().getResponseText() == "Exit Reached")
		delay(9000)
		System.out.println(waiter.get().getResponseText())
		assertTrue(waiter.get().getResponseText() == "clean")	
	}
}		
			
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun testTeaRoom(){
					
		testCoap()
		
		/*val walker      = CoapClient()
		val uri      = "coap://localhost:8050/ctxtearoom/walker"
		walker.setURI(uri)
		runBlocking {
			delay(2000)
		}
		val msg : ApplMessage = MsgUtil.buildRequest("test", "movetoCell", "movetoCell(2, 2)", "walker")		
		walker.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)*/		
	}
}