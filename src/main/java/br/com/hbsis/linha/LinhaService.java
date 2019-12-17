package br.com.hbsis.linha;

import br.com.hbsis.categoria.Categoria;
import br.com.hbsis.categoria.ICategoriaRepository;
import com.google.common.net.HttpHeaders;
import com.opencsv.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LinhaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaService.class);

    private final ILinhaRepository iLinhaRepository;

    private final ICategoriaRepository iCategoriaRepository;

    public LinhaService(ILinhaRepository iLinhaRepository, ICategoriaRepository iCategoriaRepository) {
        this.iLinhaRepository = iLinhaRepository;

        this.iCategoriaRepository = iCategoriaRepository;
    }

//CADASTRAR LINHAS

    public LinhaDTO save(LinhaDTO linhaDTO) {

        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findOptionalinById(linhaDTO.getCategorialinha().getId());

        this.validate(linhaDTO);

        LOGGER.info("salvando Linhas");
        LOGGER.debug("Linha : {}", linhaDTO);

        Categoria categoria = categoriaOptional.get();

        Linha linha = new Linha();

        String codR = codigoValidar(linhaDTO.getCodigolinha());

        linha.setNome(linhaDTO.getNome());
        linha.setCategorialinha(categoria);
        linha.setCodigolinha(linhaDTO.getCodigolinha());
        linha = this.iLinhaRepository.save(linha);
        return LinhaDTO.of(linha);

    }

    public String codigoValidar(String codigo) {
        String codigoProcessador = StringUtils.leftPad(codigo, 3, "0");

        return codigoProcessador;

    }

    //VALIDACOES
    private void validate(LinhaDTO linhaDTO) {
        LOGGER.info("Validando Linhas");

        if (linhaDTO == null) {
            throw new IllegalArgumentException("A categoria nao pode ser nulo");
        }
        if (StringUtils.isEmpty(linhaDTO.getNome())) {
            throw new IllegalArgumentException("O nome da linha nao pode ser nulo");
        }
        if (StringUtils.isEmpty(linhaDTO.getCodigolinha())) {
            throw new IllegalArgumentException("o codigo da linha nao pode ser nulo");
        }

    }

//METODOS DE PESQUISA

    public LinhaDTO findById(Long id) {
        Optional<Linha> linhaOptional = this.iLinhaRepository.findById(id);

        if (linhaOptional.isPresent()) {
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

//ALTERAR LINHAS

    public LinhaDTO update(LinhaDTO linhaDTO, Long id) {
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
//EXPORT E IMPORT DOS CSV


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


    public void ler(MultipartFile importacao) throws Exception {
        InputStreamReader inserir = new InputStreamReader(importacao.getInputStream());


        CSVReader leitor = new CSVReaderBuilder(inserir).withSkipLines(1).build();

        List<String[]> linhaLer = leitor.readAll();
        List<Categoria> resultado = new ArrayList<>();

        for (String[] lin : linhaLer) {

            try {

                String[] dados = lin[0].replaceAll("\"", "").split(";");

                Linha linha = new Linha();


                linha.setNome((dados[1]));
                Optional<Categoria> categoria = (iCategoriaRepository.findByCodigoCategoria(dados[2]));
                linha.setCodigolinha(dados[0]);


                linha.setCategorialinha(categoria.get());
//                    resultado.add(linha);
                System.out.println(resultado);

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        iCategoriaRepository.saveAll(resultado);

    }

}
