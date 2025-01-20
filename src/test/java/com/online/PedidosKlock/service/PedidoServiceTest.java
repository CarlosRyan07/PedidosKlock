package com.online.PedidosKlock.service;

import com.online.PedidosKlock.model.Pedido;
import com.online.PedidosKlock.model.Cliente;
import com.online.PedidosKlock.model.Item;
import com.online.PedidosKlock.repository.PedidoRepository;
import com.online.PedidosKlock.repository.ClienteRepository;
import com.online.PedidosKlock.repository.ItemRepository;
import com.online.PedidosKlock.service.util.EmailService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private PedidoService pedidoService;

    private Cliente cliente;
    private Item item;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup mock data
        cliente = new Cliente();
        cliente.setId(1L);
        
        item = new Item();
        item.setId(1L);
        item.setQuantidade(2);

        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setCliente(cliente);
        pedido.setItens(Arrays.asList(item));
    }

    @Test
    void testListarTodos() {
        // Mock para simular a resposta do repositório
        when(pedidoRepository.findAll()).thenReturn(Arrays.asList(pedido));

        // Executa o método
        var pedidos = pedidoService.listarTodos();

        // Verifica se o pedido foi retornado corretamente
        assertEquals(1, pedidos.size());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId_Sucesso() {
        // Mock para simular a resposta do repositório
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        // Executa o método
        Pedido pedidoEncontrado = pedidoService.buscarPorId(1L);

        // Verifica se o pedido correto foi retornado
        assertNotNull(pedidoEncontrado);
        assertEquals(1L, pedidoEncontrado.getId());
        verify(pedidoRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarPorId_ClienteNaoEncontrado() {
        // Mock para simular a resposta do repositório quando o pedido não é encontrado
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

        // Verifica se a exceção é lançada
        assertThrows(EntityNotFoundException.class, () -> pedidoService.buscarPorId(1L));
    }

    @Test
    void testCriarPedido_Sucesso() {
        // Mock para simular a resposta dos repositórios
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        // Executa o método
        Pedido novoPedido = pedidoService.criarPedido(pedido);

        // Verifica se o pedido foi criado com sucesso
        assertNotNull(novoPedido);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
        verify(emailService, times(1)).enviarNotificacao(novoPedido);
    }

    @Test
    void testCriarPedido_ClienteNaoEncontrado() {
        // Mock para simular a resposta do repositório quando o cliente não é encontrado
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        // Verifica se a exceção é lançada
        assertThrows(EntityNotFoundException.class, () -> pedidoService.criarPedido(pedido));
    }

    @Test
    void testExcluirPedido_Sucesso() {
        // Mock para simular a resposta do repositório
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        // Executa o método
        pedidoService.excluirPedido(1L);

        // Verifica se o pedido foi excluído
        verify(pedidoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testExcluirPedido_PedidoNaoEncontrado() {
        // Mock para simular a resposta do repositório quando o pedido não é encontrado
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

        // Verifica se a exceção é lançada
        assertThrows(EntityNotFoundException.class, () -> pedidoService.excluirPedido(1L));
    }
}
