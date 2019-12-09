package br.com.hbsis.linha;

import br.com.hbsis.categoria.Categoria;
import br.com.hbsis.categoria.CategoriaService;
import com.google.common.net.HttpHeaders;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Optional;

@Service
public class LinhaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaService.class);

    private final ILinhaRepository iLinhaRepository;
    private final CategoriaService categoriaService;

    public LinhaService( ILinhaRepository iLinhaRepository, CategoriaService categoriaService){
        this.iLinhaRepository = iLinhaRepository;
        this.categoriaService = categoriaService;
    }

    public LinhaDTO save (LinhaDTO linhaDTO){

        Optional<Categoria> categoriaOptional = this.categoriaService.findOptionalinById(linhaDTO.getCategorialinha().getId());

        this.validate (linhaDTO);

        LOGGER.info("salvando Linhas");
        LOGGER.debug("Linha : {}", linhaDTO);

        Categoria categoria = categoriaOptional.get();

        Linha linha = new Linha();

            linha.setNome(linhaDTO.getNome());
            linha.setCategorialinha(categoria);
            linha.setCodigolinha(linhaDTO.getCodigolinha());
            linha = this.iLinhaRepository.save(linha);
            return LinhaDTO.of(linha);

    }

    private void validate(LinhaDTO linhaDTO){
        LOGGER.info("Validando Linhas");

        if(linhaDTO == null){
            throw new IllegalArgumentException("A categoria nao pode ser nulo");
        }
        if(StringUtils.isEmpty(linhaDTO.getNome())){
            throw  new IllegalArgumentException("O nome da linha nao pode ser nulo");
        }
        if(StringUtils.isEmpty(linhaDTO.getCodigolinha())) {
            throw new IllegalArgumentException("o codigo da linha nao pode ser nulo");
        }

    }
    public LinhaDTO findById(Long id){
        Optional<Linha> linhaOptional = this.iLinhaRepository.findById(id);

        if(linhaOptional.isPresent()){
            return LinhaDTO.of(linhaOptional.get());
        }
        throw new IllegalArgumentException(String.format("o %s id nao existente", id));
    }
    public Optional<Linha> findOptionalById(Long id) {
        Optional<Linha> linhaOptional = this.iLinhaRepository.findById(id);
        if (linhaOptional.isPresent()) {
            return linhaOptional;
        }
        throw new IllegalArgumentException(String.format("id %s nao existe", id));
    }
    public LinhaDTO update(LinhaDTO linhaDTO, Long id){
        Optional<Linha> linhaExisteteOptional = this.iLinhaRepository.findById(id);

        if (linhaExisteteOptional.isPresent()) {
            Linha linhaExistente = linhaExisteteOptional.get();

            LOGGER.info("Atualizando a linha de categoria... id:[{}]", linhaExistente.getId());
            LOGGER.debug("Recebendo {}", linhaDTO);
            LOGGER.debug("linha existente : {}", linhaExistente);

            linhaExistente.setNome(linhaDTO.getNome());
            linhaExistente.setCategorialinha(linhaDTO.getCategorialinha());
            linhaExistente.setCodigolinha(linhaDTO.getCodigolinha());

            linhaExistente = this.iLinhaRepository.save(linhaExistente);

            return LinhaDTO.of(linhaExistente);
        }
        throw new IllegalArgumentException(String.format("id %s nao existente", id));
    }
    public void delete(Long id){
        LOGGER.info("Deletando linhas pelo id : [{}]", id);

        this.iLinhaRepository.deleteById(id);
    }
    void escreverLinha(HttpServletResponse reponde) throws Exception {
        String nomeArquivo = "arquivoLinha.csv";
        reponde.setContentType("text/csv");
        reponde.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"");

        PrintWriter escritor = reponde.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(escritor).withSeparator(';').withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER).withLineEnd(CSVWriter.DEFAULT_LINE_END).build();
        String[] tituloCSV = {"id", "nome", "cateogrialinha", "codigolinha"};
        icsvWriter.writeNext(tituloCSV);

        for (Linha linhas : iLinhaRepository.findAll()) {
            icsvWriter.writeNext(new String[]{String.valueOf(linhas.getId()), linhas.getNome(), String.valueOf(linhas.getCategorialinha().getId()), linhas.getCodigolinha().toString()});
        }
    }




}
