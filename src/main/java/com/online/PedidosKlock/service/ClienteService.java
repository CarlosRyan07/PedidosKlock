package com.online.PedidosKlock.service;

import jakarta.persistence.EntityNotFoundException;
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
                .orElseThrow(() -> new EntityNotFoundException("Cliente com ID " + id + " não encontrado!"));
    }

    public Cliente criarCliente(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().isEmpty()) {
            throw new IllegalArgumentException("O nome do cliente não pode estar vazio.");
        }
        if (cliente.getEmail() == null || cliente.getEmail().isEmpty()) {
            throw new IllegalArgumentException("O email do cliente não pode estar vazio.");
        }
        return clienteRepository.save(cliente);
    }

    public Cliente atualizarCliente(Long id, Cliente clienteAtualizado) {
        if (clienteAtualizado.getNome() == null || clienteAtualizado.getNome().isEmpty()) {
            throw new IllegalArgumentException("O nome do cliente não pode estar vazio.");
        }
        if (clienteAtualizado.getEmail() == null || clienteAtualizado.getEmail().isEmpty()) {
            throw new IllegalArgumentException("O email do cliente não pode estar vazio.");
        }

        Cliente clienteExistente = buscarPorId(id);

        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setEmail(clienteAtualizado.getEmail());
        clienteExistente.setVip(clienteAtualizado.isVip());
        return clienteRepository.save(clienteExistente);
    }

    public void excluirCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente com ID " + id + " não encontrado!");
        }

        boolean clienteAssociado = pedidoRepository.existsByClienteId(id);

        if (clienteAssociado) {
            throw new IllegalStateException("Não é possível excluir o cliente pois ele está associado a pedidos.");
        }

        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente com ID " + id + " não encontrado!");
        }

        clienteRepository.deleteById(id);
    }
}
