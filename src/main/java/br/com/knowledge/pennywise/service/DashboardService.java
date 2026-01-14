package br.com.knowledge.pennywise.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import br.com.knowledge.pennywise.domain.dto.DashboardResumoDTO;
import br.com.knowledge.pennywise.repository.DespesaRepository;
import br.com.knowledge.pennywise.repository.EntradaRepository;

@Service
public class DashboardService {
    private final EntradaRepository entradaRepository;
    private final DespesaRepository despesaRepository;

    public DashboardService(EntradaRepository entradaRepository, DespesaRepository despesaRepository) {
        this.entradaRepository = entradaRepository;
        this.despesaRepository = despesaRepository;
    }

    public DashboardResumoDTO getResumo() {
        BigDecimal totalEntradas = entradaRepository.sumTotal();
        BigDecimal totalDespesas = despesaRepository.sumTotal();
        return new DashboardResumoDTO(totalEntradas, totalDespesas);
    }

}
