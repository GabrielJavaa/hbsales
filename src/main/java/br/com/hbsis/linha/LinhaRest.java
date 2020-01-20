package br.com.hbsis.linha;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/linha")
public class LinhaRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaRest.class);

    private final LinhaService linhaService;

    @Autowired
    public LinhaRest( LinhaService linhaService){
        this.linhaService = linhaService;
    }
    @PostMapping
    public LinhaDTO save (@RequestBody LinhaDTO linhaDTO){
        LOGGER.info("Recebendo persistencia de Linha...");
        LOGGER.debug("Recebendo {}", linhaDTO);
        return this.linhaService.save(linhaDTO);
    }
    @GetMapping("/{id}")
    public LinhaDTO find(@PathVariable ("id") Long id){
        LOGGER.info("recebendo id da linha... id:[{}]", id);
        return this.linhaService.findById(id);
    }
    @PutMapping("{/id}")
    public LinhaDTO update(@PathVariable ("id") Long id , @RequestBody LinhaDTO linhaDTO){
        LOGGER.info("Alterando linhas pelo id... id:{}", id);
        LOGGER.debug("Recebendo {} ", linhaDTO);
        return this.linhaService.update(linhaDTO, id);
    }

    @GetMapping("/export.csv")
    public void exportCSV(HttpServletResponse file) throws Exception {
        linhaService.escreverLinha(file);
    }

    @PostMapping("/importarcsv")
    public void importCSV(@RequestParam("file") MultipartFile file) throws Exception {
        linhaService.lerlinha(file);
    }

}
