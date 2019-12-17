package br.com.hbsis.categoria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/categoria")
public class CategoriaRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaRest.class);

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaRest(CategoriaService categoriaService){

         this.categoriaService = categoriaService;
    }

    @PostMapping
    public CategoriaDTO save(@RequestBody CategoriaDTO categoriaDTO){
        LOGGER.info("Recebendo solicitação de persistencia da categoria...");
        LOGGER.debug("Payaload {}", categoriaDTO);

        return this.categoriaService.save(categoriaDTO);
    }

    @GetMapping("/{id}")
    public CategoriaDTO find(@PathVariable ("id") Long id){
        LOGGER.info("Recebendo o find by id... id: [{}]", id);

        return this.categoriaService.findById(id);
    }

    @PutMapping("/{id}")
    public CategoriaDTO update(@PathVariable("id") Long id, @RequestBody CategoriaDTO categoriaDTO){
        LOGGER.info("Alterando dados da categoria pelo id: {}", id);
        LOGGER.debug("Payaload {}", categoriaDTO);

        return this.categoriaService.update(categoriaDTO, id);
    }
    @GetMapping("/export.csv")
    public void exportCSV(HttpServletResponse file) throws Exception {
        categoriaService.escrever(file);


    }
    @PostMapping("/importarcsv")
    public void importCSV(@RequestParam("file") MultipartFile file) throws Exception {
        categoriaService.ler(file);
    }

}
