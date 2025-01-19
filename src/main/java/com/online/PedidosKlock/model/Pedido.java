package com.online.PedidosKlock.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Data
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnoreProperties("pedidos") // Ignorar o outro lado do relacionamento
    private Cliente cliente;

    @ManyToMany
    @JoinTable(
        name = "pedido_item",
        joinColumns = @JoinColumn(name = "pedido_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    @JsonIgnoreProperties("pedidos") // Ignorar o outro lado do relacionamento
 
    private List<Item> itens = new ArrayList<>(); // Corrigido o nome para "itens"

    private double total;
    private double totalComDesconto;
    private boolean emEstoque;
    private LocalDate dataEntrega;

    public void calcularTotais() {
        double total = 0.0;
    
        for (Item item : itens) {
            total += item.getPreco() * item.getQuantidade();
        }
    
        this.total = total;
    
        // Aplicar desconto se o cliente for VIP
        if (cliente != null && cliente.isVip()) {
            this.totalComDesconto = total * 0.9; // 10% de desconto
        } else {
            this.totalComDesconto = total;
        }
    }
    
    
    public void verificarEstoque() {
        this.emEstoque = itens.stream()
                .allMatch(item -> item.getQuantidade() <= item.getEstoque());
    }

    public void definirDataEntrega() {
        if (emEstoque) {
            this.dataEntrega = LocalDate.now().plusDays(3); // Exemplo de prazo para entrega
        } else {
            this.dataEntrega = null;
        }
    }
}
