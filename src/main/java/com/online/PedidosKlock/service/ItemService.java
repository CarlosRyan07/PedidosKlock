package com.online.PedidosKlock.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import com.online.PedidosKlock.model.Item;
import com.online.PedidosKlock.repository.ItemRepository;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> listarTodos() {
        return itemRepository.findAll();
    }

    public Item buscarPorId(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item não encontrado!"));
    }

    public Item criarItem(Item item) {
        return itemRepository.save(item);
    }

    public Item atualizarItem(Long id, Item itemAtualizado) {
        Item itemExistente = buscarPorId(id);
        itemExistente.setNome(itemAtualizado.getNome());
        itemExistente.setPreco(itemAtualizado.getPreco());
        itemExistente.setQuantidade(itemAtualizado.getQuantidade());
        itemExistente.setEstoque(itemAtualizado.getEstoque());
        return itemRepository.save(itemExistente);
    }

    public void excluirItem(Long id) {
        // Buscar o item pelo ID
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item não encontrado"));

        // Verificar se o item está associado a pedidos
        if (item.getPedidos() != null && !item.getPedidos().isEmpty()) {
            throw new IllegalStateException("Não é possível excluir um item que está associado a pedidos.");
        }

        // Excluir o item
        itemRepository.deleteById(id);
    }
}
