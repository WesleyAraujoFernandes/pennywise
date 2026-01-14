package br.com.knowledge.pennywise.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.knowledge.pennywise.domain.dto.EntradaDTO;
import br.com.knowledge.pennywise.exception.EntradaDuplicadaException;
import br.com.knowledge.pennywise.model.Entrada;
import br.com.knowledge.pennywise.repository.EntradaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EntradaService {

    private final EntradaRepository entradaRepository;

    public EntradaDTO createEntrada(EntradaDTO dto) {
        boolean exists = entradaRepository.existsByDescricaoAndData(dto.getDescricao(), dto.getData());
        if (exists) {
            throw new EntradaDuplicadaException(
                    "Já existe uma entrada com a descrição: " + dto.getDescricao() + " para a data: " + dto.getData());
        }
        Entrada entrada = Entrada.builder()
                .descricao(dto.getDescricao())
                .valor(dto.getValor())
                .data(dto.getData())
                .categoria(dto.getCategoria())
                .build();
        Entrada saved = entradaRepository.save(entrada);
        return mapToDTO(saved);
    }

    public List<EntradaDTO> getAllEntradas() {
        return entradaRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public EntradaDTO getEntradaById(Long id) {
        return entradaRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Entrada não encontrada"));
    }

    public EntradaDTO updateEntrada(Long id, EntradaDTO dto) {
        Entrada entrada = entradaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrada não encontrada"));
        entrada.setDescricao(dto.getDescricao());
        entrada.setValor(dto.getValor());
        entrada.setData(dto.getData());
        entrada.setCategoria(dto.getCategoria());
        Entrada updated = entradaRepository.save(entrada);
        return mapToDTO(updated);
    }

    public void deleteEntrada(Long id) {
        entradaRepository.deleteById(id);
    }

    public List<EntradaDTO> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return entradaRepository.findByData(dataInicio, dataFim).stream().map(this::mapToDTO).toList();
    }

    private EntradaDTO mapToDTO(Entrada entrada) {
        return EntradaDTO.builder()
                .id(entrada.getId())
                .descricao(entrada.getDescricao())
                .valor(entrada.getValor())
                .data(entrada.getData())
                .categoria(entrada.getCategoria())
                .build();
    }

}