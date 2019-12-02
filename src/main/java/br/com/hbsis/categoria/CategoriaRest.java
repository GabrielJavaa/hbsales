package br.com.hbsis.categoria;

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;


@RestController
@RequestMapping("/categoria")
public class CategoriaRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaRest.class);

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaRest(CategoriaService categoriaService){this.categoriaService = categoriaService;}

    @PostMapping
    public CategoriaDTO save(@RequestBody CategoriaDTO categoriaDTO){
        LOGGER.info("Recebendo solicitação de persistencia do Produto...");
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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        LOGGER.info("Deletando categoria pelo id: {}", id);

        this.categoriaService.delete(id);
    }
    @GetMapping("/export-csv")
    public void exportCSV(HttpServletResponse response) throws Exception{

        String file = "categoria.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; file=\""+ file + "\"");

        /*Criando instancia para gravar uma lista de categorias no arquivo CSV*/

        PrintWriter mostrar = response.getWriter();
        CSVWriter csvWriter = (CSVWriter) new CSVWriterBuilder(mostrar)
                .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                .build();

        /*listando todas as minha categorias */

        String headerCSV []= {"id","nomeCategoria", "fornecedorCategoria", "codigoCategoria", "unidadeCategoria" };
        csvWriter.writeNext(headerCSV);

        List<Categoria> categorias = this.categoriaService.categoriaList();

    }
}
