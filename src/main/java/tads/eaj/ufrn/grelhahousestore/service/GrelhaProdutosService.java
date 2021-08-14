package tads.eaj.ufrn.grelhahousestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tads.eaj.ufrn.grelhahousestore.model.Produtos;
import tads.eaj.ufrn.grelhahousestore.repository.RepoProduto;

import java.util.Date;
import java.util.List;

@Service
public class GrelhaProdutosService {
    RepoProduto repository;

    @Autowired
    public void setRepository(RepoProduto repository) {
        this.repository = repository;
    }

    public List<Produtos> findAll(){
        return repository.findAll();
    }

    public void save(Produtos a){
        repository.save(a);
    }

    public void delete(Long id){
        Date data = new Date();
        Produtos produto = repository.getById(id);
        produto.setDateDeleted(data);

        repository.save(produto);
    }

    public Produtos findById(Long id){
        return repository.getById(id);
    }
}
