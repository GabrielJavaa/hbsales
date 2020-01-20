package br.com.hbsis.item;

import org.springframework.data.jpa.repository.JpaRepository;

interface IItemRepository extends JpaRepository<Item, Long> {
}
