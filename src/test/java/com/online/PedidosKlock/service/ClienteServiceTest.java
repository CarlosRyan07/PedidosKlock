package com.online.PedidosKlock.service;

import com.online.PedidosKlock.model.Cliente;
import com.online.PedidosKlock.repository.ClienteRepository;
import com.online.PedidosKlock.repository.PedidoRepository;
import com.online.PedidosKlock.service.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    public void setUp() {
        cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        cliente.setEmail("cliente@teste.com");
        cliente.setVip(true);
    }

    @Test
    public void testCriarCliente_Sucesso() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente clienteCriado = clienteService.criarCliente(cliente);

        assertNotNull(clienteCriado);
        assertEquals(cliente.getNome(), clienteCriado.getNome());
        assertEquals(cliente.getEmail(), clienteCriado.getEmail());
        assertTrue(clienteCriado.isVip());
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    public void testCriarCliente_NomeInvalido() {
        cliente.setNome(""); // Nome inválido

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.criarCliente(cliente);
        });

        assertEquals("O nome do cliente não pode estar vazio.", exception.getMessage());
    }

    @Test
    public void testCriarCliente_EmailInvalido() {
        cliente.setEmail(""); // Email inválido

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.criarCliente(cliente);
        });

        assertEquals("O email do cliente não pode estar vazio.", exception.getMessage());
    }

    @Test
    public void testAtualizarCliente_Sucesso() {
        when(clienteRepository.findById(1L)).thenReturn(java.util.Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        cliente.setNome("Cliente Atualizado");
        cliente.setEmail("cliente@atualizado.com");

        Cliente clienteAtualizado = clienteService.atualizarCliente(1L, cliente);

        assertNotNull(clienteAtualizado);
        assertEquals("Cliente Atualizado", clienteAtualizado.getNome());
        assertEquals("cliente@atualizado.com", clienteAtualizado.getEmail());
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    public void testAtualizarCliente_NomeInvalido() {
        cliente.setNome(""); // Nome inválido

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.atualizarCliente(1L, cliente);
        });

        assertEquals("O nome do cliente não pode estar vazio.", exception.getMessage());
    }

    @Test
    public void testAtualizarCliente_EmailInvalido() {
        cliente.setEmail(""); // Email inválido

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.atualizarCliente(1L, cliente);
        });

        assertEquals("O email do cliente não pode estar vazio.", exception.getMessage());
    }

    @Test
    public void testBuscarPorId_ClienteNaoEncontrado() {
        when(clienteRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> clienteService.buscarPorId(1L));
    }


    @Test
    public void testExcluirCliente_Sucesso() {
        // Criação de um cliente mock
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");
        cliente.setEmail("cliente@test.com");

        // Configuração do mock
        when(clienteRepository.findById(1L)).thenReturn(java.util.Optional.of(cliente));
        when(pedidoRepository.existsByClienteId(1L)).thenReturn(false); // Cliente não associado a pedidos

        // Executa o método que será testado
        clienteService.excluirCliente(1L);

        // Verifica se o método findById foi chamado corretamente
        verify(clienteRepository, times(1)).findById(1L);

        // Verifica se o cliente foi excluído
        verify(clienteRepository, times(1)).deleteById(1L);
    }

    
    @Test
    public void testExcluirCliente_ClienteAssociadoAPedidos() {
        // Mock para simular cliente encontrado e associado a pedidos
        when(clienteRepository.findById(1L)).thenReturn(java.util.Optional.of(cliente));
        when(pedidoRepository.existsByClienteId(1L)).thenReturn(true); // Cliente associado a pedidos
        
        // Executa o teste para verificar se a exceção IllegalStateException é lançada
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            clienteService.excluirCliente(1L);
        });
        
        // Verifica a mensagem da exceção
        assertEquals("Não é possível excluir o cliente pois ele está associado a pedidos.", exception.getMessage());
    }
    
    @Test
    public void testExcluirCliente_ClienteNaoEncontrado() {
        // Mock para simular cliente não encontrado
        when(clienteRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        
        // Verifica se a exceção EntityNotFoundException é lançada
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> clienteService.excluirCliente(1L));
        
        // Verifica a mensagem da exceção
        assertEquals("Cliente com ID 1 não encontrado!", exception.getMessage());
    }
    
}
