package com.online.PedidosKlock.service;

import com.online.PedidosKlock.model.Item;
import com.online.PedidosKlock.model.Pedido;
import com.online.PedidosKlock.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private Item item;

    @BeforeEach
    public void setUp() {
        item = new Item();
        item.setNome("Item Teste");
        item.setPreco(100.0);
        item.setQuantidade(10);
        item.setEstoque(50);
    }

    @Test
    public void testCriarItem_Sucesso() {
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item itemCriado = itemService.criarItem(item);

        assertNotNull(itemCriado);
        assertEquals(item.getNome(), itemCriado.getNome());
        assertEquals(item.getPreco(), itemCriado.getPreco());
        assertEquals(item.getQuantidade(), itemCriado.getQuantidade());
        assertEquals(item.getEstoque(), itemCriado.getEstoque());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    public void testCriarItem_NomeInvalido() {
        item.setNome("");  // Nome inválido

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.criarItem(item);
        });

        assertEquals("O nome do item não pode ser vazio.", exception.getMessage());
    }

    @Test
    public void testCriarItem_PrecoInvalido() {
        item.setPreco(0);  // Preço inválido

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.criarItem(item);
        });

        assertEquals("O preço do item deve ser maior que zero.", exception.getMessage());
    }

    @Test
    public void testCriarItem_QuantidadeInvalida() {
        item.setQuantidade(-1);  // Quantidade negativa

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.criarItem(item);
        });

        assertEquals("A quantidade do item não pode ser negativa.", exception.getMessage());
    }

    @Test
    public void testCriarItem_EstoqueInvalido() {
        item.setEstoque(-1);  // Estoque negativo

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.criarItem(item);
        });

        assertEquals("O estoque do item não pode ser negativo.", exception.getMessage());
    }

    @Test
    public void testListarTodos() {
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itens = itemService.listarTodos();

        assertNotNull(itens);
        assertEquals(1, itens.size());
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    public void testBuscarPorId_ItemNaoEncontrado() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.buscarPorId(1L));
    }

    @Test
    public void testAtualizarItem_Sucesso() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        item.setPreco(120.0);
        Item itemAtualizado = itemService.atualizarItem(1L, item);

        assertNotNull(itemAtualizado);
        assertEquals(120.0, itemAtualizado.getPreco());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    public void testAtualizarItem_NomeInvalido() {
        item.setNome("");  // Nome inválido

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.atualizarItem(1L, item);
        });

        assertEquals("O nome do item não pode ser vazio.", exception.getMessage());
    }

    @Test
    public void testAtualizarItem_PrecoInvalido() {
        item.setPreco(0);  // Preço inválido

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.atualizarItem(1L, item);
        });

        assertEquals("O preço do item deve ser maior que zero.", exception.getMessage());
    }

    @Test
    public void testAtualizarItem_QuantidadeInvalida() {
        item.setQuantidade(-1);  // Quantidade negativa

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.atualizarItem(1L, item);
        });

        assertEquals("A quantidade do item não pode ser negativa.", exception.getMessage());
    }

    @Test
    public void testAtualizarItem_EstoqueInvalido() {
        item.setEstoque(-1);  // Estoque negativo

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.atualizarItem(1L, item);
        });

        assertEquals("O estoque do item não pode ser negativo.", exception.getMessage());
    }

    @Test
    public void testExcluirItem_Sucesso() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        doNothing().when(itemRepository).deleteById(1L);

        itemService.excluirItem(1L);

        verify(itemRepository, times(1)).deleteById(1L);
    }

}
