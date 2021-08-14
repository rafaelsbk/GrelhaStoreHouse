package tads.eaj.ufrn.grelhahousestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tads.eaj.ufrn.grelhahousestore.model.Produtos;

public interface RepoProduto extends JpaRepository<Produtos, Long> {

}
