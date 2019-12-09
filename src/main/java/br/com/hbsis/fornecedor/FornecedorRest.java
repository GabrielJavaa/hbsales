package br.com.hbsis.fornecedor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/fornecedor")
 public class FornecedorRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FornecedorRest.class);

    private final FornecedorService fornecedorService;

    @Autowired
    public FornecedorRest(FornecedorService fornecedorService) { this.fornecedorService = fornecedorService; }

    @PostMapping
    public FornecedorDTO save (@RequestBody FornecedorDTO fornecedorDTO) {
        LOGGER.info("Recebendo solicitação de persistencia do Fornecedor...");
        LOGGER.debug("Payaload {}", fornecedorDTO);
        return this.fornecedorService.save(fornecedorDTO);
    }

    @GetMapping("/{id}")
    public FornecedorDTO find(@PathVariable ("id") Long id){

        LOGGER.info("Encontrando o fornecedor pelo Id... id: [{}]", id);

        return this.fornecedorService.findById(id);
    }

    @PutMapping("/{id}")
    public FornecedorDTO update(@PathVariable("id")Long id, @RequestBody FornecedorDTO fornecedorDTO) {
        LOGGER.info("Alterando fornecedor pelo id... id: {}", id);
        LOGGER.debug("Payload {}", fornecedorDTO);

        return this.fornecedorService.update(fornecedorDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        LOGGER.info("Deletando fornecedor pelo id: {}", id);

        this.fornecedorService.delete(id);
    }

}
