package com.online.PedidosKlock.service;

import org.springframework.stereotype.Service;

import com.online.PedidosKlock.model.Cliente;
import com.online.PedidosKlock.repository.ClienteRepository;
import com.online.PedidosKlock.repository.PedidoRepository;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;

    public ClienteService(ClienteRepository clienteRepository, PedidoRepository pedidoRepository) {
        this.clienteRepository = clienteRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));
    }

    public Cliente criarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente atualizarCliente(Long id, Cliente clienteAtualizado) {
        Cliente clienteExistente = buscarPorId(id);
        clienteExistente.setEmail(clienteAtualizado.getEmail());
        clienteExistente.setVip(clienteAtualizado.isVip());
        return clienteRepository.save(clienteExistente);
    }

    public void excluirCliente(Long id) {
        // Verifica se o cliente está associado a algum pedido
        boolean clienteAssociado = pedidoRepository.existsByClienteId(id);

        if (clienteAssociado) {
            throw new IllegalStateException("Não é possível excluir o cliente pois ele está associado a pedidos.");
        }

        clienteRepository.deleteById(id);
    }
}
