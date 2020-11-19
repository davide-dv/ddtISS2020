/*package prova;

import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.MsgUtil;

public class ClientMain {
	
	

	public static void main(String[] args) {
	
		ConnQakTcp conn;	
		conn = new prova.ConnQakTcp("localhost", "1883", "waitermind");
		conn.createConnection();  
		
		System.out.println("Connection = " + conn );
		
		  ApplMessage msg = MsgUtil.buildDispatch("web", "enterrequest", "enterrequest(1)", "waitermind" );
		  conn.forward( msg );
		  
		  System.out.println("Forward done" );
		
		
	}
	
	
}*/
