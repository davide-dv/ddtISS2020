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

val initDelayTime     = 16000L
val smartbell      = CoapClient()
val uriStr      = "coap://127.0.0.1:8050/ctxtearoom/smartbell"
val waiter = CoapClient()
val uriWaiter      = "coap://127.0.0.1:8050/ctxtearoom/waiter"
val timer0 = CoapClient()
val uriTimer0 = "coap://127.0.0.1:8050/ctxtearoom/timer0"
val timer1 = CoapClient()
val uriTimer1 = "coap://127.0.0.1:8050/ctxtearoom/timer1"
val knowledgebase = CoapClient()
val uriKB = "coap://127.0.0.1:8050/ctxtearoom/knowledgebase"
 		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Before
	fun systemSetUp() {
		smartbell.setURI(uriStr)
		waiter.setURI(uriWaiter)	
		timer0.setURI(uriTimer0)
		timer1.setURI(uriTimer1)
		knowledgebase.setURI(uriKB)
   		kotlin.concurrent.thread(start = true) {
			it.unibo.ctxtearoom.main()
			println("testTearoom systemSetUp done")
 	}
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
	runBlocking{
		delay(initDelayTime)
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")		
		val msg : ApplMessage = MsgUtil.buildDispatch("test", "notify", "notify(X)", "smartbell")		
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)		
		assertTrue(smartbell.get().getResponseText() == """{"state":"checkTemperature","clientID":1,"msg":"Temperature OK"}""")
		delay(3000)		
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
		delay(12500)
		assertTrue(waiter.get().getResponseText() == """{"state":"cleanTable","table":0}""")
		delay(4500)
	}
}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi		
fun testCoapFullRoom() {				// third client requires to enter when all tables are occupied 
	runBlocking{
		delay(initDelayTime)			
	
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		
		// ------------------- NOTIFY ------------------- 
		// first client notify
		var msg : ApplMessage = MsgUtil.buildDispatch("test", "notify", "notify(X)", "smartbell")		
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)		
		assertTrue(smartbell.get().getResponseText() == """{"state":"checkTemperature","clientID":1,"msg":"Temperature OK"}""")		
		delay(2400)		
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":1,"msg":"Enter Now"}""")		
		delay(2200)			
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		delay(5000)		
		assertTrue(waiter.get().getResponseText() == """{"state":"convoyClient","target":0,"table0":"dirty","table1":"clean"}""")
		
		// second client notify	
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)
		assertTrue(smartbell.get().getResponseText() == """{"state":"checkTemperature","clientID":2,"msg":"Temperature OK"}""")
		delay(3000)
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":2,"msg":"Enter Now"}""")		
		delay(1200)			
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")		
		delay(3500)	
		assertTrue(waiter.get().getResponseText() == """{"state":"convoyClient","target":1,"table0":"dirty","table1":"dirty"}""")
		
		// third client notify
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)	
		delay(5500)	
		
		// needs to wait: the tearoom is full			
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":3,"msg":"Wait for 1000"}""")

		// ------------------- ORDER -------------------		
		// Client (id:0) order
		msg = MsgUtil.buildDispatch("test", "order", "order(Twinings, 0)", "waiter")
		waiter.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
		delay(250)
		
		// Client (id:1) order		
		msg = MsgUtil.buildDispatch("test", "order", "order(Lipton, 1)", "waiter")
		waiter.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
		
		// Client (id:0) receiving tea		
		delay(3000)		
		assertTrue(waiter.get().getResponseText() == """{"state":"serve","tea":"Twinings","table":0}""")	
		delay(10000)
		// Client (id:0) receiving tea
		assertTrue(waiter.get().getResponseText() == """{"state":"serve","tea":"Lipton","table":1}""")
		delay(5000)
		
		// ------------------- PAY -------------------
		msg = MsgUtil.buildDispatch("test", "payment", "payment(1)", "waiter")
		waiter.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
		delay(9000)
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
	runBlocking{
		delay(initDelayTime)			
	
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		
		// ------------------- NOTIFY ------------------- 
		// first client notify
		var msg : ApplMessage = MsgUtil.buildDispatch("test", "notify", "notify(X)", "smartbell")		
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)		
		assertTrue(smartbell.get().getResponseText() == """{"state":"checkTemperature","clientID":1,"msg":"Temperature OK"}""")
		delay(3000)		
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":1,"msg":"Enter Now"}""")
		delay(3600)			
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")		
		delay(2000)				
		assertTrue(waiter.get().getResponseText() == """{"state":"convoyClient","target":0,"table0":"dirty","table1":"clean"}""")
		
		// second client notify	
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)
		println("CCCCCCCCCCC__________ ----> " + smartbell.get().getResponseText())			
		assertTrue(smartbell.get().getResponseText() == """{"state":"checkTemperature","clientID":2,"msg":"Temperature OK"}""")
		delay(3000)
		println("DDDDDDDDDDD__________ ----> " + smartbell.get().getResponseText())			
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":2,"msg":"Enter Now"}""")
		delay(5700)		
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")		
		delay(3500)		
		assertTrue(waiter.get().getResponseText() == """{"state":"convoyClient","target":1,"table0":"dirty","table1":"dirty"}""")
		
		// third client notify
		//smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)	
		delay(2000)	

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
		delay(10000)		
		assertTrue(timer0.get().getResponseText() == """{"state":"enable"}""")		
		delay(15000)
		
		// ------------------- PAY -------------------
		msg = MsgUtil.buildDispatch("test", "payment", "payment(1)", "waiter")
		waiter.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)		
		delay(7000)			
		assertTrue(waiter.get().getResponseText() == """{"state":"cleanTable","table":1}""")	
		
		delay(18000)		
		assertTrue(waiter.get().getResponseText() == """{"state":"cleanTable","table":0}""")	
		delay(5000)
	}
}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun testCoapFullRoomTwoTimer() {		// timer elapses for two client 
	runBlocking{
		delay(initDelayTime)			
		
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		
		// ------------------- NOTIFY ------------------- 
		// first client notify
		var msg : ApplMessage = MsgUtil.buildDispatch("test", "notify", "notify(X)", "smartbell")		
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	

		delay(250)		
		assertTrue(smartbell.get().getResponseText() == """{"state":"checkTemperature","clientID":1,"msg":"Temperature OK"}""")
		delay(3000)				
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":1,"msg":"Enter Now"}""")		
		delay(2200)					
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		delay(3000)
		assertTrue(waiter.get().getResponseText() == """{"state":"convoyClient","target":0,"table0":"dirty","table1":"clean"}""")
		
		// second client notify	
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)		
		assertTrue(smartbell.get().getResponseText() == """{"state":"checkTemperature","clientID":2,"msg":"Temperature OK"}""")
		delay(3000)		
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":2,"msg":"Enter Now"}""")		
		delay(1200)			
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")		
		delay(7000)		
		assertTrue(waiter.get().getResponseText() == """{"state":"convoyClient","target":1,"table0":"dirty","table1":"dirty"}""")

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
		delay(10500)				
		assertTrue(timer0.get().getResponseText() == """{"state":"enable"}""")	
		delay(10000)

		// Client (id:0) receiving tea		
		assertTrue(timer1.get().getResponseText() == """{"state":"enable"}""")
		delay(5000)
		
		// ------------------- WAIT TIMERS -------------------
		delay(28000)		
		assertTrue(waiter.get().getResponseText() == """{"state":"cleanTable","table":0}""")	
		delay(10000)		
		assertTrue(waiter.get().getResponseText() == """{"state":"cleanTable","table":1}""")			
		delay(5000)

	}

}	
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun testCoapFullRoomThirdClient() {		// third Client enter when a table is clean 
	runBlocking{
		delay(initDelayTime)
		
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		
		// ------------------- NOTIFY ------------------- 
		// first client notify
		var msg : ApplMessage = MsgUtil.buildDispatch("test", "notify", "notify(X)", "smartbell")		
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)		
		assertTrue(smartbell.get().getResponseText() == """{"state":"checkTemperature","clientID":1,"msg":"Temperature OK"}""")		
		delay(3000)		
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":1,"msg":"Enter Now"}""")		
		delay(1200)			
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		delay(5000)		
		assertTrue(waiter.get().getResponseText() == """{"state":"convoyClient","target":0,"table0":"dirty","table1":"clean"}""")
		
		// second client notify	
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)
		assertTrue(smartbell.get().getResponseText() == """{"state":"checkTemperature","clientID":2,"msg":"Temperature OK"}""")
		delay(2500)
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":2,"msg":"Enter Now"}""")		
		delay(1200)
		assertTrue(smartbell.get().getResponseText() == """{"state":"handleRequest"}""")
		delay(6000)		
		assertTrue(waiter.get().getResponseText() == """{"state":"convoyClient","target":1,"table0":"dirty","table1":"dirty"}""")
		
		// third client notify
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)			
		
		// needs to wait: the tearoom is full
		delay(4000)					
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
		delay(10000)						
		assertTrue(timer0.get().getResponseText() == """{"state":"enable"}""")		
		delay(15000)
	
		// ------------------- PAY -------------------
		msg = MsgUtil.buildDispatch("test", "payment", "payment(1)", "waiter")
		waiter.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)		
		delay(7000)			
		assertTrue(waiter.get().getResponseText() == """{"state":"cleanTable","table":1}""")			
		delay(18000)		
		assertTrue(waiter.get().getResponseText() == """{"state":"cleanTable","table":0}""")

		// third Client notify again
		msg = MsgUtil.buildDispatch("test", "notify", "notify(X)", "smartbell")		
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)
		assertTrue(smartbell.get().getResponseText() == """{"state":"checkTemperature","clientID":4,"msg":"Temperature OK"}""")			
		delay(5000)	
		assertTrue(smartbell.get().getResponseText() == """{"state":"showWaiterResponse","clientID":4,"msg":"Enter Now"}""")		
		delay(3000)		
		assertTrue(waiter.get().getResponseText() == """{"state":"convoyClient","target":0,"table0":"dirty","table1":"clean"}""")
		delay(5000)
	}
}


@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun testKnowledgebase() {
	runBlocking{
		delay(initDelayTime)

		val msg : ApplMessage = MsgUtil.buildDispatch("test", "notify", "notify(X)", "smartbell")		
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)		
		assertTrue(knowledgebase.get().getResponseText() == """{"smartbell": {"state":"checkTemperature","clientID":1,"msg":"Temperature_OK"},"waiter": {"state":"handleRequest"}, "barman":{"state":"waitingOrder"} }""")		
		delay(3000)				
		assertTrue(knowledgebase.get().getResponseText() == """{"smartbell": {"state":"showWaiterResponse","clientID":1,"msg":"Enter_Now"},"waiter": {"state":"tableCheck","table0":"clean","table1":"clean"}, "barman":{"state":"waitingOrder"} }""")
		delay(7200)		
		assertTrue(knowledgebase.get().getResponseText() == """{"smartbell": {"state":"handleRequest"},"waiter": {"state":"convoyClient", "target":0,"table0":"dirty","table1":"clean"}, "barman":{"state":"waitingOrder"} }""")

		val msgOrder : ApplMessage = MsgUtil.buildDispatch("test", "order", "order(Twinings, 0)", "waiter")
		waiter.put(msgOrder.toString(), MediaTypeRegistry.TEXT_PLAIN)
		delay(10000)		
		assertTrue(knowledgebase.get().getResponseText() == """{"smartbell": {"state":"handleRequest"},"waiter": {"state":"deliverTea","table":0}, "barman":{"state":"waitingOrder"} }""")		
		val msgPayment : ApplMessage = MsgUtil.buildDispatch("test", "payment", "payment(0)", "waiter")
		waiter.put(msgPayment.toString(), MediaTypeRegistry.TEXT_PLAIN)		
		delay(7000)				
		assertTrue(knowledgebase.get().getResponseText() == """{"smartbell": {"state":"handleRequest"},"waiter": {"state":"collect","table":0}, "barman":{"state":"waitingOrder"} }""")		
		delay(7300)		
		assertTrue(knowledgebase.get().getResponseText() == """{"smartbell": {"state":"handleRequest"},"waiter": {"state":"setTableClean","table":0}, "barman":{"state":"waitingOrder"} }""")
		delay(5500)	
	}
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun testTemperatureTooHigh() {		// high temperature must be set in the smartbell qak
	runBlocking{
		delay(initDelayTime)

		val msg : ApplMessage = MsgUtil.buildDispatch("test", "notify", "notify(X)", "smartbell")		
		smartbell.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)	
		delay(250)
		println("smart: " + smartbell.get().getResponseText())
		println("KNOWLEDGE BASE: " + knowledgebase.get().getResponseText())	
		assertTrue(knowledgebase.get().getResponseText() == """{"smartbell": {"state":"checkTemperature","clientID":0,"msg":"Temperature_too_High"},"waiter": {"state":"handleRequest"}, "barman":{"state":"waitingOrder"} }""")		
		delay(1200)	
	}
}

			
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun testTeaRoom(){		
		testCoap()
		//testCoapFullRoom()		
		//testCoapFullRoomOneTimer()
		//testCoapFullRoomTwoTimer()
		//testCoapFullRoomThirdClient()		
		//testKnowledgebase()
		//testTemperatureTooHigh()
	}
}
