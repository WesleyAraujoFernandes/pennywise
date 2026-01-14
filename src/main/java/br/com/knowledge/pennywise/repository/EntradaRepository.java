package br.com.knowledge.pennywise.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.knowledge.pennywise.model.Entrada;

@Repository
public interface EntradaRepository extends JpaRepository<Entrada, Long> {
    boolean existsByDescricaoAndData(String descricao, LocalDate data);

    @Query("select coalesce(sum(e.valor),0) from Entrada e")
    BigDecimal sumTotal();

    @Query("""
            SELECT e
            FROM Entrada e
            WHERE (:inicio IS NULL OR e.data >= :inicio) AND (:fim IS NULL OR e.data <= :fim)
            ORDER BY e.data DESC
            """)
    List<Entrada> findByData(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}
