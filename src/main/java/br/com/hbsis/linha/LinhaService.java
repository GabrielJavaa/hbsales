package br.com.hbsis.linha;

import br.com.hbsis.categoria.Categoria;
import br.com.hbsis.categoria.CategoriaService;
import com.google.common.net.HttpHeaders;
import com.opencsv.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final CategoriaService categoriaService;

    @Autowired
    public LinhaService(ILinhaRepository iLinhaRepository, CategoriaService categoriaService) {
        this.iLinhaRepository = iLinhaRepository;
        this.categoriaService = categoriaService;
    }

    //CADASTRAR LINHAS

    public LinhaDTO save(LinhaDTO linhaDTO) {

        this.validate(linhaDTO);

        LOGGER.info("salvando Linhas");
        LOGGER.debug("Linha: {}", linhaDTO);

            Linha linha = new Linha();

            String codR = codigoValidar(linhaDTO.getCodigolinha());

            linha.setNome(linhaDTO.getNome());
            linha.setCategoria(categoriaService.findByCodigoCategoria(linhaDTO.getCategorialinha().toString()));
            linha.setCodigolinha(codR.toUpperCase());

            linha = this.iLinhaRepository.save(linha);
            return LinhaDTO.of(linha);

    }
    public LinhaDTO saveParaImportar(LinhaDTO linhaDTO){

        this.validateParaImportar(linhaDTO);
        LOGGER.info("salvando linha para importar");
        LOGGER.debug("Linha: {}",linhaDTO);

        Linha linha = new Linha();

            String codR = codigoValidar(linhaDTO.getCodigolinha());

        linha.setNome(linhaDTO.getNome());
        linha.setCategoria(categoriaService.findByCodigoCategoria(linhaDTO.getCategorialinha().toString()));
        linha.setCodigolinha(codR.toUpperCase());

        linha = this.iLinhaRepository.save(linha);
            return linhaDTO.of(linha);

    }

    public String codigoValidar(String Codigolinha) {
        return StringUtils.leftPad(Codigolinha, 10, "0");
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

    private void validateParaImportar( LinhaDTO linhaDTO){
        LOGGER.info("Validando linhas para importar");

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

    public Linha findByOptionalId(Long id) {
        Optional<Linha> linhaOptional = this.iLinhaRepository.findById(id);

        if (linhaOptional.isPresent()) {
            return linhaOptional.get();
        }
        throw new IllegalArgumentException(String.format("o %s id nao existente", id));
    }

//ALTERAR LINHAS

    public LinhaDTO update(LinhaDTO linhaDTO, Long id) {
        Optional<Linha> linhaExisteteOptional = this.iLinhaRepository.findById(id);

        this.validate(linhaDTO);

        Categoria categoria = linhaExisteteOptional.get().getCategoria();

        if (linhaExisteteOptional.isPresent()) {
            Linha linhaExistente = linhaExisteteOptional.get();

            LOGGER.info("Atualizando a linha de categoria... id:[{}]", linhaExistente.getId());
            LOGGER.debug("Recebendo {}", linhaDTO);
            LOGGER.debug("linha existente : {}", linhaExistente);

            linhaExistente.setNome(linhaDTO.getNome());
            linhaExistente.setCategoria(categoria);
            linhaExistente.setCodigolinha(linhaDTO.getCodigolinha());

            linhaExistente = this.iLinhaRepository.save(linhaExistente);

            return LinhaDTO.of(linhaExistente);
        }
        throw new IllegalArgumentException(String.format("Produto %s nao existente", id));
    }

    public LinhaDTO updateLinhaParaImportar(LinhaDTO linhaDTO, String codigolinha){
        Optional<Linha> linhaOptional = this.iLinhaRepository.findByCodigolinha(codigolinha);

        this.validateParaImportar(linhaDTO);

        Categoria categoria = linhaOptional.get().getCategoria();

        if (linhaOptional.isPresent()) {
            Linha linhaExistente = linhaOptional.get();

            LOGGER.info("Atualizando a linha de categoria... id:[{}]", linhaExistente.getId());
            LOGGER.debug("Recebendo {}", linhaDTO);
            LOGGER.debug("linha existente : {}", linhaExistente);

            linhaExistente.setNome(linhaDTO.getNome());
            linhaExistente.setCategoria(categoria);
            linhaExistente.setCodigolinha(linhaDTO.getCodigolinha());

            linhaExistente = this.iLinhaRepository.save(linhaExistente);

            return linhaDTO.of(linhaExistente);
        }
        throw new IllegalArgumentException(String.format("Linha %s nao existente", codigolinha));
    }

//EXPORT E IMPORT DOS CSV

    public List<Linha> linhaList() {
        return this.iLinhaRepository.findAll();
    }

    void escreverLinha(HttpServletResponse reponde) throws Exception {
        String nomeArquivo = "arquivoLinha.csv";
        reponde.setContentType("text/csv");
        reponde.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"");

        PrintWriter escritor = reponde.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(escritor).withSeparator(';')
                .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        String[] tituloCSV = {"codigolinha", "nome", "cateogrialinha", "nomeCategoria"};
        icsvWriter.writeNext(tituloCSV);

        for (Linha linhas : iLinhaRepository.findAll()) {
            icsvWriter.writeNext(new String[]{
                    linhas.getCodigolinha(),
                    linhas.getNome(),
                    String.valueOf(linhas.getCategoria().getCodigoCategoria()),
                    String.valueOf(linhas.getCategoria().getNomeCategoria())
            });
        }
    }

    public void lerlinha(MultipartFile importacao) throws Exception {
        InputStreamReader inserir = new InputStreamReader(importacao.getInputStream());


        CSVReader leitor = new CSVReaderBuilder(inserir).withSkipLines(1).build();

        List<String[]> linhaLer = leitor.readAll();
        List<Linha> resultado = new ArrayList<>();

        for (String[] lin : linhaLer) {

            try {

                String[] dados = lin[0].replaceAll("\"", "").split(";");

                if (iLinhaRepository.existsByCodigolinha(dados[0])) {
                    LOGGER.info("codigo da categoria ja existe...");
                } else if (!iLinhaRepository.existsByCodigolinha(dados[0])) {

                    Linha linha = new Linha();

                    linha.setCodigolinha(dados[0]);
                    linha.setNome(dados[1]);
                    linha.setCategoria(categoriaService.findByCodigoCategoria(dados[2]));

                    resultado.add(linha);
                    System.out.println(resultado);
                }

                iLinhaRepository.saveAll(resultado);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public boolean existsByCodigolinha (String codigolinha){
        return this.iLinhaRepository.existsByCodigolinha(codigolinha);
    }

    public Linha findByCodigoLinha(String codigolinha) {
        Optional<Linha> linhaOptional = this.iLinhaRepository.findByCodigolinha(codigolinha);

        if (linhaOptional.isPresent()) {
            return linhaOptional.get();
        }
        throw new IllegalArgumentException(String.format("o %s id nao existente", codigolinha));
    }
}
