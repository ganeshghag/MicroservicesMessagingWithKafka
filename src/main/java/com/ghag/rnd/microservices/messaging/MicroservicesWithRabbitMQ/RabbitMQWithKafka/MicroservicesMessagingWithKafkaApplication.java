package com.ghag.rnd.microservices.messaging.MicroservicesWithRabbitMQ.RabbitMQWithKafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//https://docs.spring.io/spring-cloud-stream/docs/Elmhurst.RELEASE/reference/htmlsingle
//https://dzone.com/articles/spring-cloud-stream-with-kafka
@SpringBootApplication
@EnableBinding(Processor.class)
public class MicroservicesMessagingWithKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicesMessagingWithKafkaApplication.class, args);
	}

	@StreamListener(Processor.INPUT)
	public void handle(Person person) {
		System.out.println("Received: " + person);
		//System.out.println("person addr=" + person.getAddress());
		//System.out.println("person name=" + person.getName());
	}

	public static class Person {

		private String name;

		private String address;

		public String getAddress() {
			return address;
		}

		public void setAddres(String addres) {
			this.address = addres;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "Person [name=" + name + ", addres=" + address + "]";
		}

		@RestController
		static public class MyMicroService1SampleRestController {

			@Autowired
			private Processor processor;

			@RequestMapping("/mymicroservice1/sayHello/{param1}")
			public String sayHello(@PathVariable String param1) {

				Person person = new Person();
				person.setAddres("addr1" + param1);
				person.setName("Ganya Ghag" + param1);
				processor.output().send(MessageBuilder.withPayload(person).build());
				System.out.println("after send of message from controller+"+person);
				return "Hello from sayHello using param1 = " + param1;
			}
		}

	}

}
