package com.teste;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PmsApplication2 {

	public static void main(String[] args)throws Exception {
		SpringApplication.run(PmsApplication2.class, args);

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
						.get("/contascorrentes/{idConta}")
				.to("direct:xxxx");

				from("direct:xxxx")
						//.log("${headers}") // logar os headers

						/*.process(exchange ->  {
								int idConta = Integer.parseInt(exchange.getIn().getHeader("idConta").toString());

								exchange.getOut().setHeader("idConta",idConta * 2);
							}
						)
						.process(new TesteProcessor()) //executar um codigo java
						.process(new Processor() {
							@Override
							public void process(Exchange exchange) throws Exception {
								int idConta = Integer.parseInt(exchange.getIn().getHeader("idConta").toString());

								exchange.getOut().setHeader("idConta",idConta * 2);
							}
						})*/
						//.setHeader(Exchange.CONTENT_TYPE, simple("text/html"))
						.setHeader(Exchange.HTTP_METHOD, constant("GET"))
						.removeHeader(Exchange.HTTP_PATH)
						.to("http4://demo0179941.mockable.io/melissaasdf?bridgeEndpoint=true")
						.process(exchange -> {
							String trancode = exchange.getIn().getBody(String.class);



							exchange.getOut().setBody(new Message(trancode.substring(0,4)));
						}).marshal(new JacksonDataFormat());
						//.transform()
						//.simple("{\"idConta\":${headers[idConta]}}");
						//.constant("lol");

			}
		};
	}
}
