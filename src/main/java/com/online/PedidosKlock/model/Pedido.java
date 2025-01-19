package com.online.PedidosKlock.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "cliente_id", nullable = true)
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
    private LocalDate dataCompra = LocalDate.now(); // Data do pedido será a data atual


    public void calcularTotais() {  
        BigDecimal total = BigDecimal.ZERO;
        
        // Soma os preços de todos os itens no pedido
        for (Item item : itens) {
            BigDecimal precoItem = BigDecimal.valueOf(item.getPreco());
            BigDecimal quantidadeItem = BigDecimal.valueOf(item.getQuantidade());
            total = total.add(precoItem.multiply(quantidadeItem));
        }
        
        // Arredondando o total para 2 casas decimais
        this.total = total.setScale(2, RoundingMode.HALF_UP).doubleValue();
        
        // Aplicar desconto se o cliente for VIP
        if (cliente != null && cliente.isVip()) {
            this.totalComDesconto = total.multiply(BigDecimal.valueOf(0.9)).setScale(2, RoundingMode.HALF_UP).doubleValue(); // 10% de desconto
        } else {
            this.totalComDesconto = total.setScale(2, RoundingMode.HALF_UP).doubleValue();
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
