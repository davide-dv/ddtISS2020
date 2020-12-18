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
 

class testTearoom {

val initDelayTime     = 6000L
	
val context     = "ctxtearoom"
val destactor   = "smartbell"
val addr        = "127.0.0.1:8050"
val smartbell      = CoapClient()
val uriStr      = "coap://$addr/$context/$destactor"
var ContextQak  : QakContext? = null
//var connSmartbell : connQakCoap? = null
val waiter = CoapClient()
val uriWaiter      = "coap://127.0.0.1:8050/ctxtearoom/waiter"
	

 		
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
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		
		val msg : ApplMessage = MsgUtil.buildDispatch("test", "notify", "notify(X)", "smartbell")		
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)
		
		assertTrue(smartbell.get().getResponseText() == """{"state":"checkTemperature","clientID":1,"msg":"Temperature OK"}""")
		
		delay(1200)
				
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":1,"msg":"Enter Now"}""")		
		delay(1200)	
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")		
		delay(5000)

		assertTrue(waiter.get().getResponseText() == """{"state":"convoyClient","target":0,"table0":"dirty","table1":"clean"}""")
		val msgOrder : ApplMessage = MsgUtil.buildDispatch("test", "order", "order(Twinings, 0)", "waiter")
		waiter.put(msgOrder.toString(), MediaTypeRegistry.TEXT_PLAIN)
		delay(10000)	
		val msgPayment : ApplMessage = MsgUtil.buildDispatch("test", "payment", "payment(0)", "waiter")
		waiter.put(msgPayment.toString(), MediaTypeRegistry.TEXT_PLAIN)
		delay(12000)
		assertTrue(waiter.get().getResponseText() == """{"state":"cleanTable"}""")
		delay(5500)
		assertTrue(waiter.get().getResponseText() == """{"state":"setTableClean","table":0}""")
	}
}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi		
fun testCoapFullRoom() {				// third client requires to enter when all tables are occupied 
	smartbell.setURI(uriStr)
	waiter.setURI(uriWaiter)
	
	runBlocking{
		delay(initDelayTime)			
	
		//assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		println(smartbell.get().getResponseText())
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		
		// ------------------- NOTIFY ------------------- 
		// first client notify
		var msg : ApplMessage = MsgUtil.buildDispatch("test", "notify", "notify(X)", "smartbell")		
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)
		assertTrue(smartbell.get().getResponseText() == """{"state":"checkTemperature","clientID":1,"msg":"Temperature OK"}""")
		
		delay(1200)
				
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":1,"msg":"Enter Now"}""")		
		delay(1200)	
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		delay(5000)
		println(waiter.get().getResponseText())
		assertTrue(waiter.get().getResponseText() == """{"state":"convoyClient","target":0,"table0":"dirty","table1":"clean"}""")
		
		// second client notify	
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)
		//println("----> " + smartbell.get().getResponseText())
		assertTrue(smartbell.get().getResponseText() == """{"state":"checkTemperature","clientID":2,"msg":"Temperature OK"}""")
		delay(4500)
		
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":2,"msg":"Enter Now"}""")		
		delay(1200)
			
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		
		delay(8000)
		//println("----> " + waiter.get().getResponseText())
		assertTrue(waiter.get().getResponseText() == """{"state":"convoyClient","target":1,"table0":"dirty","table1":"dirty"}""")
		
		// third client notify
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)	
		delay(2000)	
		
		// needs to wait: the tearoom is full
		println("----> " + smartbell.get().getResponseText())
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":3,"msg":"Wait for 1000"}""")

		// ------------------- ORDER -------------------		
		// Client (id:0) order
		msg = MsgUtil.buildDispatch("test", "order", "order(Twinings, 0)", "waiter")
		waiter.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
		delay(250)
		
		// Client (id:1) order		
		msg = MsgUtil.buildDispatch("test", "order", "order(Lipton, 1)", "waiter")
		waiter.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)		
		delay(1000)
		// Client (id:0) receiving tea
		println("----> " + waiter.get().getResponseText())
		assertTrue(waiter.get().getResponseText() == """{"state":"serve","tea":"Twinings","table":0}""")
		
	
		delay(10000)
		// Client (id:0) receiving tea
		//println("----> " + waiter.get().getResponseText())
		assertTrue(waiter.get().getResponseText() == """{"state":"serve","tea":"Lipton","table":1}""")
		delay(5000)
		
		// ------------------- PAY -------------------
		msg = MsgUtil.buildDispatch("test", "payment", "payment(1)", "waiter")
		waiter.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
		delay(10000)
		assertTrue(waiter.get().getResponseText() == """{"state":"cleanTable","table":1}""")	
		delay(1300)
		
		msg = MsgUtil.buildDispatch("test", "payment", "payment(0)", "waiter")
		waiter.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
		delay(10000)		
		assertTrue(waiter.get().getResponseText() == """{"state":"cleanTable","table":0}""")
		delay(7000)		
	}
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun testCoapFullRoomOneTimer() {		// timer elapses for one client 
	smartbell.setURI(uriStr)
	waiter.setURI(uriWaiter)
	val timer0 = CoapClient()
	val uriTimer0 = "coap://127.0.0.1:8050/ctxtearoom/timer0"
	timer0.setURI(uriTimer0)
	
	runBlocking{
		delay(initDelayTime)			
	
		//assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		println(smartbell.get().getResponseText())
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		
		// ------------------- NOTIFY ------------------- 
		// first client notify
		var msg : ApplMessage = MsgUtil.buildDispatch("test", "notify", "notify(X)", "smartbell")		
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)
		assertTrue(smartbell.get().getResponseText() == """{"state":"checkTemperature","clientID":1,"msg":"Temperature OK"}""")
		
		delay(1200)
				
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":1,"msg":"Enter Now"}""")		
		delay(1200)	
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		delay(5000)
		println(waiter.get().getResponseText())
		assertTrue(waiter.get().getResponseText() == """{"state":"convoyClient","target":0,"table0":"dirty","table1":"clean"}""")
		
		// second client notify	
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)
		//println("----> " + smartbell.get().getResponseText())
		assertTrue(smartbell.get().getResponseText() == """{"state":"checkTemperature","clientID":2,"msg":"Temperature OK"}""")
		delay(4500)
		
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":2,"msg":"Enter Now"}""")		
		delay(1200)
			
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		
		delay(8000)
		//println("----> " + waiter.get().getResponseText())
		assertTrue(waiter.get().getResponseText() == """{"state":"convoyClient","target":1,"table0":"dirty","table1":"dirty"}""")
		
		// third client notify
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)	
		delay(2000)	
		
		// needs to wait: the tearoom is full
		println("----> " + smartbell.get().getResponseText())
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":3,"msg":"Wait for 1000"}""")

		// ------------------- ORDER -------------------		
		// Client (id:0) order
		msg = MsgUtil.buildDispatch("test", "order", "order(Twinings, 0)", "waiter")
		waiter.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
		delay(250)		
	
		println("A----> " + waiter.get().getResponseText())

		// Client (id:1) order		
		msg = MsgUtil.buildDispatch("test", "order", "order(Lipton, 1)", "waiter")
		waiter.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)		
		delay(1000)
		// Client (id:0) receiving tea
		println("B----> " + waiter.get().getResponseText())
		//assertTrue(waiter.get().getResponseText() == """{"state":"serve","tea":"Twinings","table":0}""")
		
		delay(10000)				
		println("C----> " + timer0.get().getResponseText())
		assertTrue(timer0.get().getResponseText() == """{"state":"enable"}""")		

		delay(10000)
		// Client (id:0) receiving tea
		//println("----> " + waiter.get().getResponseText())
		//assertTrue(waiter.get().getResponseText() == """{"state":"serve","tea":"Lipton","table":1}""")
		delay(5000)
		
		// ------------------- PAY -------------------
		//msg = MsgUtil.buildDispatch("test", "payment", "payment(1)", "waiter")
		//waiter.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
		/*delay(10000)
		assertTrue(waiter.get().getResponseText() == """{"state":"cleanTable","table":1}""")	
		delay(1300)*/
		
		msg = MsgUtil.buildDispatch("test", "payment", "payment(1)", "waiter")
		waiter.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)		
		delay(7000)			
		assertTrue(waiter.get().getResponseText() == """{"state":"cleanTable","table":1}""")	
		
		delay(18000)
		println("D----> " + waiter.get().getResponseText())
		assertTrue(waiter.get().getResponseText() == """{"state":"cleanTable","table":0}""")	
		delay(5000)

	}

}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun testCoapFullRoomTwoTimer() {		// timer elapses for two client 
	smartbell.setURI(uriStr)
	waiter.setURI(uriWaiter)
	val timer0 = CoapClient()
	val uriTimer0 = "coap://127.0.0.1:8050/ctxtearoom/timer0"
	val timer1 = CoapClient()
	val uriTimer1 = "coap://127.0.0.1:8050/ctxtearoom/timer1"
	timer0.setURI(uriTimer0)
	timer1.setURI(uriTimer1)
	
	runBlocking{
		delay(initDelayTime)			
	
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		
		// ------------------- NOTIFY ------------------- 
		// first client notify
		var msg : ApplMessage = MsgUtil.buildDispatch("test", "notify", "notify(X)", "smartbell")		
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)
		assertTrue(smartbell.get().getResponseText() == """{"state":"checkTemperature","clientID":1,"msg":"Temperature OK"}""")
		
		delay(1200)
				
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":1,"msg":"Enter Now"}""")		
		delay(1200)	
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		delay(5000)
		println(waiter.get().getResponseText())
		assertTrue(waiter.get().getResponseText() == """{"state":"convoyClient","target":0,"table0":"dirty","table1":"clean"}""")
		
		// second client notify	
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)
		
		assertTrue(smartbell.get().getResponseText() == """{"state":"checkTemperature","clientID":2,"msg":"Temperature OK"}""")
		delay(4500)
		
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":2,"msg":"Enter Now"}""")		
		delay(1200)
			
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		
		delay(8000)

		assertTrue(waiter.get().getResponseText() == """{"state":"convoyClient","target":1,"table0":"dirty","table1":"dirty"}""")
		/*
		// third client notify
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)	
		delay(2000)	
		
		// needs to wait: the tearoom is full
		println("----> " + smartbell.get().getResponseText())
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":3,"msg":"Wait for 1000"}""")
*/
		// ------------------- ORDER -------------------		
		// Client (id:0) order
		msg = MsgUtil.buildDispatch("test", "order", "order(Twinings, 0)", "waiter")
		waiter.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
		delay(250)		
	
		println("A----> " + waiter.get().getResponseText())

		// Client (id:1) order		
		msg = MsgUtil.buildDispatch("test", "order", "order(Lipton, 1)", "waiter")
		waiter.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)		
		delay(1000)
		// Client (id:0) receiving tea
		println("B----> " + waiter.get().getResponseText())
		//assertTrue(waiter.get().getResponseText() == """{"state":"serve","tea":"Twinings","table":0}""")
		
		delay(10000)				
		println("T0----> " + timer0.get().getResponseText())
		assertTrue(timer0.get().getResponseText() == """{"state":"enable"}""")		

		delay(10000)
		// Client (id:0) receiving tea
		println("----> " + waiter.get().getResponseText())
		//assertTrue(waiter.get().getResponseText() == """{"state":"serve","tea":"Lipton","table":1}""")
		println("T1----> " + timer1.get().getResponseText())
		assertTrue(timer1.get().getResponseText() == """{"state":"enable"}""")
		delay(5000)
		
		// ------------------- PAY -------------------
		//msg = MsgUtil.buildDispatch("test", "payment", "payment(1)", "waiter")
		//waiter.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
		/*delay(10000)
		assertTrue(waiter.get().getResponseText() == """{"state":"cleanTable","table":1}""")	
		delay(1300)
		
		msg = MsgUtil.buildDispatch("test", "payment", "payment(1)", "waiter")
		waiter.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)		
		delay(7000)			
		assertTrue(waiter.get().getResponseText() == """{"state":"cleanTable","table":1}""")*/	
		
		delay(28000)
		println("C----> " + waiter.get().getResponseText())
		assertTrue(waiter.get().getResponseText() == """{"state":"cleanTable","table":0}""")
		
		// TROVARE DELAY
		delay(10000)
		println("D----> " + waiter.get().getResponseText())		
		assertTrue(waiter.get().getResponseText() == """{"state":"cleanTable","table":1}""")
			
		delay(5000)

	}

}	
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun testCoapFullRoomThirdClient() {		// third Client enter when a table is clean 
	smartbell.setURI(uriStr)
	waiter.setURI(uriWaiter)
	val timer0 = CoapClient()
	val uriTimer0 = "coap://127.0.0.1:8050/ctxtearoom/timer0"
	timer0.setURI(uriTimer0)
	
	runBlocking{
		delay(initDelayTime)			
	
		//assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		println(smartbell.get().getResponseText())
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		
		// ------------------- NOTIFY ------------------- 
		// first client notify
		var msg : ApplMessage = MsgUtil.buildDispatch("test", "notify", "notify(X)", "smartbell")		
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)
		assertTrue(smartbell.get().getResponseText() == """{"state":"checkTemperature","clientID":1,"msg":"Temperature OK"}""")
		
		delay(1200)
				
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":1,"msg":"Enter Now"}""")		
		delay(1200)	
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		delay(5000)
		println(waiter.get().getResponseText())
		assertTrue(waiter.get().getResponseText() == """{"state":"convoyClient","target":0,"table0":"dirty","table1":"clean"}""")
		
		// second client notify	
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)
		//println("----> " + smartbell.get().getResponseText())
		assertTrue(smartbell.get().getResponseText() == """{"state":"checkTemperature","clientID":2,"msg":"Temperature OK"}""")
		delay(4500)
		
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":2,"msg":"Enter Now"}""")		
		delay(1200)
			
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		
		delay(8000)
		//println("----> " + waiter.get().getResponseText())
		assertTrue(waiter.get().getResponseText() == """{"state":"convoyClient","target":1,"table0":"dirty","table1":"dirty"}""")
		
		// third client notify
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)	
		delay(2000)	
		
		// needs to wait: the tearoom is full
		println("----> " + smartbell.get().getResponseText())
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":3,"msg":"Wait for 1000"}""")

		// ------------------- ORDER -------------------		
		// Client (id:0) order
		msg = MsgUtil.buildDispatch("test", "order", "order(Twinings, 0)", "waiter")
		waiter.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
		delay(250)		
	
		println("A----> " + waiter.get().getResponseText())

		// Client (id:1) order		
		msg = MsgUtil.buildDispatch("test", "order", "order(Lipton, 1)", "waiter")
		waiter.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)		
		delay(1000)
		// Client (id:0) receiving tea
		println("B----> " + waiter.get().getResponseText())
		//assertTrue(waiter.get().getResponseText() == """{"state":"serve","tea":"Twinings","table":0}""")
		
		delay(10000)				
		println("C----> " + timer0.get().getResponseText())
		assertTrue(timer0.get().getResponseText() == """{"state":"enable"}""")		

		delay(10000)
		// Client (id:0) receiving tea
		//println("----> " + waiter.get().getResponseText())
		//assertTrue(waiter.get().getResponseText() == """{"state":"serve","tea":"Lipton","table":1}""")
		delay(5000)
		
		// ------------------- PAY -------------------
		//msg = MsgUtil.buildDispatch("test", "payment", "payment(1)", "waiter")
		//waiter.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
		/*delay(10000)
		assertTrue(waiter.get().getResponseText() == """{"state":"cleanTable","table":1}""")	
		delay(1300)*/
		
		msg = MsgUtil.buildDispatch("test", "payment", "payment(1)", "waiter")
		waiter.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)		
		delay(7000)			
		assertTrue(waiter.get().getResponseText() == """{"state":"cleanTable","table":1}""")	
		
		delay(18000)
		println("D----> " + waiter.get().getResponseText())
		assertTrue(waiter.get().getResponseText() == """{"state":"cleanTable","table":0}""")	
		//delay(5000)
		

		// third Client notify again
		msg = MsgUtil.buildDispatch("test", "notify", "notify(X)", "smartbell")		
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)
		assertTrue(smartbell.get().getResponseText() == """{"state":"checkTemperature","clientID":4,"msg":"Temperature OK"}""")		
	
		delay(5000)	
		println("----> " + smartbell.get().getResponseText())

		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":4,"msg":"Enter Now"}""")		
		delay(3000)
		println("----> " + waiter.get().getResponseText())		
		assertTrue(waiter.get().getResponseText() == """{"state":"convoyClient","target":0,"table0":"dirty","table1":"clean"}""")
		delay(5000)
	}

}





@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun testMaxStayTime() {
	// Only with 5 seconds timer	

	smartbell.setURI(uriStr)
	waiter.setURI(uriWaiter)
	
	val timer0 = CoapClient()
	val uriTimer0 = "coap://127.0.0.1:8050/ctxtearoom/timer0"
	timer0.setURI(uriTimer0)
	
	runBlocking{
		delay(initDelayTime)
				
		val msg : ApplMessage = MsgUtil.buildDispatch("test", "notify", "notify(X)", "smartbell")		
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)		
		delay(20000)		
		val msgOrder : ApplMessage = MsgUtil.buildDispatch("test", "order", "order(Twinings, 0)", "waiter")
		waiter.put(msgOrder.toString(), MediaTypeRegistry.TEXT_PLAIN)		
		delay(15000)
		println("ENABLE ----> " + timer0.get().getResponseText())
		assertTrue(timer0.get().getResponseText() == """{"state":"enable"}""")		
		delay(1700)		
		println("COLLECT ----> " + waiter.get().getResponseText())
		assertTrue(waiter.get().getResponseText() == """{"state":"collect","table":0}""")
		delay(20000)
	}	
}	

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun testTemperatureTooHigh() {		// high temperature must be set in the smartbell qak
	smartbell.setURI(uriStr)
	runBlocking{
		delay(initDelayTime)

		val msg : ApplMessage = MsgUtil.buildDispatch("test", "notify", "notify(X)", "smartbell")		
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)
		println("smart: " + smartbell.get().getResponseText())
		assertTrue(smartbell.get().getResponseText() == """{"state":"checkTemperature","clientID":0,"msg":"Temperature too high"}""")
		delay(1200)	
	}
}


	
			
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun testTeaRoom(){		
		//testCoap()
		//testCoapMultiClient()
		//testCoapFullRoom()
		//testCoapFullRoomOneTimer()
		testCoapFullRoomTwoTimer()
		//testCoapFullRoomThirdClient()
		//testMaxStayTime()
		//testTemperatureTooHigh()
	}
}
