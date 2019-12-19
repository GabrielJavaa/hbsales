package br.com.hbsis.linha;

import br.com.hbsis.categoria.Categoria;
import br.com.hbsis.categoria.CategoriaDTO;
import br.com.hbsis.categoria.CategoriaService;
import br.com.hbsis.categoria.ICategoriaRepository;
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
    private final ICategoriaRepository iCategoriaRepository;

    /* TESTE */
    private final CategoriaService categoriaService;

    @Autowired
    public LinhaService(ILinhaRepository iLinhaRepository, ICategoriaRepository iCategoriaRepository, CategoriaService categoriaService) {
        this.iLinhaRepository = iLinhaRepository;
        this.iCategoriaRepository = iCategoriaRepository;
        this.categoriaService = categoriaService;
    }

    //CADASTRAR LINHAS

    public LinhaDTO save(LinhaDTO linhaDTO) {

        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findById(linhaDTO.getCategorialinha());

        this.validate(linhaDTO);

        LOGGER.info("salvando Linhas");
        LOGGER.debug("Linha: {}", linhaDTO);

        Linha linha = new Linha();

        Categoria categoria = categoriaOptional.get();
        System.out.println(categoria);

        String codR = codigoValidar(linhaDTO.getCodigolinha());
        System.out.println(codR);

        linha.setNome(linhaDTO.getNome());
        linha.setCategoria(categoria);
        linha.setCodigolinha(codR);

        linha = this.iLinhaRepository.save(linha);
        return LinhaDTO.of(linha);

    }

    public String codigoValidar(String Codigolinha) {
        String codigo = StringUtils.leftPad(Codigolinha, 9, "0");

        return codigo;

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
    public LinhaDTO findByCodigoLinha(String codigolinha){
        Optional<Linha> linhaOptional = this.iLinhaRepository.findByCodigolinha(codigolinha);

        if (linhaOptional.isPresent()) {
            return LinhaDTO.of(linhaOptional.get());
        }
        throw new IllegalArgumentException(String.format("o %s codigo da linha nao existente", codigolinha));
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
        throw new IllegalArgumentException(String.format("id %s nao existente", id));
    }
//EXPORT E IMPORT DOS CSV


    void escreverLinha(HttpServletResponse reponde) throws Exception {
        String nomeArquivo = "arquivoLinha.csv";
        reponde.setContentType("text/csv");
        reponde.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"");

        PrintWriter escritor = reponde.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(escritor).withSeparator(';')
                .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        String[] tituloCSV = {"codigolinha", "nome", "cateogrialinha" , "nomeCategoria"};
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

                if ( iLinhaRepository.existsByCodigolinha(dados [0])){
                    LOGGER.info("codigo da categoria ja existe...");
                }else if ( !iLinhaRepository.existsByCodigolinha(dados [0])){



                        Linha linha = new Linha();

                        linha.setCodigolinha(dados[0]);
                        linha.setNome(dados[1]);

                        CategoriaDTO categoriaDTO = categoriaService.findByCodigoCategoria(dados[2]);
                        Categoria categoria = categoriaService.converter(categoriaDTO);
                        linha.setCategoria(categoria);

                        resultado.add(linha);
                        System.out.println(resultado);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        iLinhaRepository.saveAll(resultado);
    }

}
