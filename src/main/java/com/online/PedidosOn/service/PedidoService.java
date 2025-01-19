package com.online.PedidosOn.service;

import com.online.PedidosOn.model.Cliente;
import com.online.PedidosOn.model.Pedido;
import com.online.PedidosOn.repository.ClienteRepository;
import com.online.PedidosOn.repository.PedidoRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;  // Adicione esta linha

    public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;  // Inicialize o ClienteRepository
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
    }

    public Pedido criarPedido(Pedido pedido) {
        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new IllegalArgumentException("O pedido deve conter ao menos um item.");
        }

        if (pedido.getCliente() == null || pedido.getCliente().getId() == null) {
            throw new IllegalArgumentException("O cliente do pedido não foi informado.");
        }

        // Verificar e carregar cliente do banco
        Cliente cliente = clienteRepository.findById(pedido.getCliente().getId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        pedido.setCliente(cliente);

        // Calcular valores do pedido
        pedido.calcularTotais(); // Calcula total e totalComDesconto
        pedido.verificarEstoque(); // Verifica se todos os itens têm estoque
        pedido.definirDataEntrega(); // Define a data de entrega

        return pedidoRepository.save(pedido);
    }



    public Pedido atualizarPedido(Long id, Pedido pedidoAtualizado) {
        Pedido pedidoExistente = buscarPorId(id);

        // Atualizar os campos necessários
        pedidoExistente.setItens(pedidoAtualizado.getItens());
        pedidoExistente.calcularTotais();
        pedidoExistente.verificarEstoque();
        pedidoExistente.definirDataEntrega();

        return pedidoRepository.save(pedidoExistente);
    }

    public void excluirPedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new EntityNotFoundException("Pedido não encontrado");
        }
        pedidoRepository.deleteById(id);
    }
}
