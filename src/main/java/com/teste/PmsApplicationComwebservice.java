package com.teste;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PmsApplicationComwebservice {

	public static void main(String[] args)throws Exception {
		SpringApplication.run(PmsApplicationComwebservice.class, args);

		CamelContext context = new DefaultCamelContext();
		context.addRoutes(createRouteBuilder());
		context.start();
	}

	static RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {

				restConfiguration().component("jetty").host("localhost").port(8080);

				//expor uma rota
				rest("/clientes")
						.produces("application/json")
						.get("/contascorrentes/{idConta}").to("direct:contacorrentes")
						.get("/bruno").to("direct:bruno");

				from("direct:bruno")
				.log("${headers} ${body}")
				.setHeader(Exchange.CONTENT_TYPE, simple("text/html"))
				.transform()
				.simple("AA${headers[idConta]}");


				from("direct:contacorrentes")
						.setHeader(Exchange.HTTP_METHOD, constant("GET"))
						.removeHeader(Exchange.HTTP_PATH)
						.setHeader(Exchange.HTTP_QUERY,simple("contaCorrente=${headers[idConta]}"))
						.to("http4://localhost:8080/clientes/bruno?bridgeEndpoint=true")
						.process(exchange -> {
							String trancode = exchange.getIn().getBody(String.class);
							exchange.getOut().setBody(new Message(trancode.substring(0,4)));
						}).marshal(new JacksonDataFormat());
			}
		};
	}
}
