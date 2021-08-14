package tads.eaj.ufrn.grelhahousestore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.eaj.ufrn.grelhahousestore.erro.ErroFiltro;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Produtos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idProduto;
    String nomeProduto;
    @Size(min = 2, max = 40, message = ErroFiltro.ERRO_TAMANHO_STRING)
    String fabricante;
    String tipoProduto;
    String categoria;
    Float precoFinal;
    Date dateDeleted;
    String imagem_Uri;
}