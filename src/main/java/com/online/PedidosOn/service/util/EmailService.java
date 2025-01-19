package com.online.PedidosOn.service.util;

import com.online.PedidosOn.model.Pedido;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    public void enviarNotificacao(Pedido pedido) {
        String email = pedido.getCliente().getEmail();
        String mensagem = pedido.isEmEstoque()
                ? "Seu pedido será entregue em breve."
                : "Um ou mais itens do seu pedido estão fora de estoque.";
        enviarEmail(email, mensagem);
    }

    private void enviarEmail(String email, String mensagem) {
        // Simula envio de e-mail
        System.out.println("Enviando e-mail para " + email + ": " + mensagem);
    }
}

