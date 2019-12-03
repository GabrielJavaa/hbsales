package br.com.hbsis.categoria;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping("/categoria")
public class CategoriaRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaRest.class);

    private final CategoriaService categoriaService;

    private final ICategoriaRepository iCategoriaRepository;

    @Autowired
    public CategoriaRest(CategoriaService categoriaService, ICategoriaRepository iCategoriaRepository){
        this.categoriaService = categoriaService;
        this.iCategoriaRepository = iCategoriaRepository;
    }

    @PostMapping
    public CategoriaDTO save(@RequestBody CategoriaDTO categoriaDTO) throws IllegalAccessException {
        LOGGER.info("Recebendo solicitação de persistencia do Produto...");
        LOGGER.debug("Payaload {}", categoriaDTO);

        return this.categoriaService.save(categoriaDTO);
    }

    @GetMapping("/{id}")
    public CategoriaDTO find(@PathVariable ("id") Long id) throws IllegalAccessException {
        LOGGER.info("Recebendo o find by id... id: [{}]", id);

        return this.categoriaService.findById(id);
    }

    @PutMapping("/{id}")
    public CategoriaDTO update(@PathVariable("id") Long id, @RequestBody CategoriaDTO categoriaDTO) throws IllegalAccessException {
        LOGGER.info("Alterando dados da categoria pelo id: {}", id);
        LOGGER.debug("Payaload {}", categoriaDTO);

        return this.categoriaService.update(categoriaDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        LOGGER.info("Deletando categoria pelo id: {}", id);

        this.categoriaService.delete(id);
    }


    @GetMapping("/export.csv")
    public void exportCSV(HttpServletResponse file) throws Exception {
        categoriaService.escrever(file.getWriter());


    }
    @PostMapping(value ="/import" , consumes ="multipart/form-data" )
    public void importCSV(@RequestParam ("file") MultipartFile csvfile) throws IOException {
        this.categoriaService.importcsv(csvfile);

    }

}
