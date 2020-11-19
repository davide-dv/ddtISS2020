package com.unibo.iss.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

@SpringBootApplication
public class ManagerApplication {

	public static void main(String[] args) {

		SpringApplication.run(ManagerApplication.class, args);

	}

}
