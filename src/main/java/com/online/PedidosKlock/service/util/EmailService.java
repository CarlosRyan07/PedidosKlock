package com.online.PedidosKlock.service.util;
import org.springframework.stereotype.Service;

import com.online.PedidosKlock.model.Pedido;

@Service
public class EmailService {

    public void enviarNotificacao(Pedido pedido) {
        String email = pedido.getCliente().getEmail();
        String mensagem = pedido.isEmEstoque()
                ? "Seu pedido será entregue em breve."
                : "Um ou mais itens do seu pedido estão fora de estoque.";

        enviarEmail(email, mensagem);  // Chama o método para enviar o email
    }

    private void enviarEmail(String email, String mensagem) {
        // Simula envio de e-mail
        System.out.println("Enviando e-mail para " + email + ": " + mensagem);
    }
}

