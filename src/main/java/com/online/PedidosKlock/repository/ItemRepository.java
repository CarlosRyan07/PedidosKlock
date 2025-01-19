package com.online.PedidosKlock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.online.PedidosKlock.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
