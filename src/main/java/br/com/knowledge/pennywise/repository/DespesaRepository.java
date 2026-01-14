package br.com.knowledge.pennywise.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.knowledge.pennywise.model.Despesa;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {
    boolean existsByDescricaoAndData(String descricao, LocalDate data);

    @Query("select coalesce(sum(d.valor),0) from Despesa d")
    BigDecimal sumTotal();

    @Query("""
            SELECT d
            FROM Despesa d
            WHERE (:inicio IS NULL OR d.data >= :inicio) AND (:fim IS NULL OR d.data <= :fim)
            ORDER BY d.data DESC
            """)
    List<Despesa> findByData(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}
