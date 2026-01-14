package br.com.knowledge.pennywise.domain.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResumoDTO {
    private BigDecimal totalEntradas;
    private BigDecimal totalDespesas;
    private BigDecimal saldo;

    public DashboardResumoDTO(BigDecimal totalEntradas, BigDecimal totalDespesas) {
        this.totalEntradas = totalEntradas;
        this.totalDespesas = totalDespesas;
        this.saldo = totalEntradas.subtract(totalDespesas);
    }

}