package com.teste;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class TesteProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        int idConta = Integer.parseInt(exchange.getIn().getHeader("idConta").toString());

        exchange.getOut().setHeader("idConta",idConta * 2);
    }
}
