package br.com.knowledge.pennywise.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.knowledge.pennywise.domain.dto.DespesaDTO;
import br.com.knowledge.pennywise.service.DespesaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/despesas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DespesaController {

    private final DespesaService despesaService;

    @PostMapping
    public ResponseEntity<DespesaDTO> createDespesa(@Valid @RequestBody DespesaDTO dto) {
        return ResponseEntity.ok(despesaService.createDespesa(dto));
    }

    @GetMapping
    public ResponseEntity<List<DespesaDTO>> getAllDespesas() {
        return ResponseEntity.ok(despesaService.getAllDespesas());
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<DespesaDTO>> getEntradaPorPeriodo(@RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim) {
        return ResponseEntity.ok(despesaService.listarPorPeriodo(dataInicio, dataFim));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespesaDTO> getDespesaById(@PathVariable Long id) {
        return ResponseEntity.ok(despesaService.getDespesaById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DespesaDTO> updateDespesa(@PathVariable Long id, @Valid @RequestBody DespesaDTO dto) {
        return ResponseEntity.ok(despesaService.updateDespesa(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDespesa(@PathVariable Long id) {
        despesaService.deleteDespesa(id);
        return ResponseEntity.noContent().build();
    }
}