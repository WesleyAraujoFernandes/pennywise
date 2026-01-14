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

import br.com.knowledge.pennywise.domain.dto.EntradaDTO;
import br.com.knowledge.pennywise.service.EntradaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/entradas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class EntradaController {

    private final EntradaService entradaService;

    @PostMapping
    public ResponseEntity<EntradaDTO> createEntrada(@Valid @RequestBody EntradaDTO dto) {
        return ResponseEntity.ok(entradaService.createEntrada(dto));
    }

    @GetMapping
    public ResponseEntity<List<EntradaDTO>> getAllEntradas() {
        return ResponseEntity.ok(entradaService.getAllEntradas());
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<EntradaDTO>> getEntradaPorPeriodo(@RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim) {
        return ResponseEntity.ok(entradaService.listarPorPeriodo(dataInicio, dataFim));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntradaDTO> getEntradaById(@PathVariable Long id) {
        return ResponseEntity.ok(entradaService.getEntradaById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntradaDTO> updateEntrada(@PathVariable Long id, @Valid @RequestBody EntradaDTO dto) {
        return ResponseEntity.ok(entradaService.updateEntrada(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntrada(@PathVariable Long id) {
        entradaService.deleteEntrada(id);
        return ResponseEntity.noContent().build();
    }
}