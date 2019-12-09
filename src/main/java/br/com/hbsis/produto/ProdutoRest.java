package br.com.hbsis.produto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/produto")
public class ProdutoRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoRest.class);

    private final ProdutoService produtoService;

    @Autowired
    public ProdutoRest(ProdutoService produtoService){
        this.produtoService = produtoService;
    }

    @PostMapping
    public ProdutoDTO save(@RequestBody ProdutoDTO produtoDTO){
        LOGGER.info("Recendo solicitação de persistencia do produto...");
        LOGGER.debug("Recebendo {}", produtoDTO);

        return this.produtoService.save(produtoDTO);
    }
    @GetMapping("/{id}")
    public ProdutoDTO find(@PathVariable ("id") Long id){
        LOGGER.info("Buscando Produto pelo id... id: [{}]",id);
        return this.produtoService.findById(id);
    }
    @PutMapping("/{id}")
    public ProdutoDTO update(@PathVariable ("id") Long id, @RequestBody ProdutoDTO produtoDTO){
        LOGGER.info("Alterando produtos pelo id... id: [{}]", id);
        LOGGER.debug("Recebendo {}", produtoDTO);

        return this.produtoService.update(produtoDTO,id);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        LOGGER.info("Deletando produtos pelo id : {}", id);

        this.produtoService.delete(id);
    }
    @GetMapping("/export.csv")
    public void exportCSV(HttpServletResponse file) throws Exception {
        produtoService.escreverLinha(file);
    }

}
