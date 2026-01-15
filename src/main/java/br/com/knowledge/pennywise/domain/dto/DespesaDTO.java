package br.com.knowledge.pennywise.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.knowledge.pennywise.model.CategoriaDespesa;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DespesaDTO {

    private Long id;
    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 255, message = "Descrição não deve ter mais de 255 caracteres")
    private String descricao;
    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser maior que zero")
    private BigDecimal valor;
    @NotNull(message = "Data é obrigatória")
    private LocalDate data;
    @NotNull(message = "Categoria é obrigatória")
    private CategoriaDespesa categoria;
}
