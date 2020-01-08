package br.com.hbsis.categoria;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
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
public class CategoriaService<ExportCSV> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaService.class);
    private final ICategoriaRepository iCategoriaRepository;
    private final FornecedorService fornecedorService;

    public CategoriaService(ICategoriaRepository iCategoriaRepository, FornecedorService fornecedorService) {
        this.iCategoriaRepository = iCategoriaRepository;
        this.fornecedorService = fornecedorService;

    }

    //CADASTRAR
    public CategoriaDTO save(CategoriaDTO categoriaDTO) {


            this.validate(categoriaDTO);
            LOGGER.info("Salvando Categorias");
            LOGGER.debug("Categoria: {}", categoriaDTO);

            Categoria categoria = new Categoria();

            categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());
            categoria.setFornecedor(fornecedorService.findOptionalById(categoriaDTO.getFornecedorId()));
            categoria.setCodigoCategoria(validaCodigo(categoriaDTO.getCodigoCategoria(), categoriaDTO.getFornecedorId()));

            categoria = this.iCategoriaRepository.save(categoria);
            return CategoriaDTO.of(categoria);

    }

    public CategoriaDTO saveParaImporte(CategoriaDTO categoriaDTO){

        LOGGER.info("Salvando Categorias");
        LOGGER.debug("Categoria: {}", categoriaDTO);

        Categoria categoriaCadastrar = new Categoria();

        categoriaCadastrar.setNomeCategoria(categoriaDTO.getNomeCategoria());
        categoriaCadastrar.setFornecedor(fornecedorService.findOptionalById(categoriaDTO.getFornecedorId()));
        categoriaCadastrar.setCodigoCategoria(validaCodigo(categoriaDTO.getCodigoCategoria(), categoriaDTO.getFornecedorId()));

        this.iCategoriaRepository.save(categoriaCadastrar);
        return CategoriaDTO.of(categoriaCadastrar);
    }

    public String validaCodigo(String codigoCategoria, Long id) {

        Fornecedor fornecedor = this.fornecedorService.findOptionalById(id);

        String cnpj = fornecedor.getCnpj();
        String formatado = "";
        String codigo = "CAT";

        String ultimosDigitos = cnpj.substring(cnpj.length() - 4);

        formatado = codigo + ultimosDigitos + codigoCategoria;

        return formatado;
    }

    //PERCORRER TABELA

    public CategoriaDTO findById(Long id) {
        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findById(id);

        if (categoriaOptional.isPresent()) {
            return CategoriaDTO.of(categoriaOptional.get());
        }

        throw new IllegalArgumentException(String.format("id %s não existe", id));
    }

    public Categoria findByOptionalId(Long id) {
        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findById(id);

        if (categoriaOptional.isPresent()) {
            return categoriaOptional.get();
        } else {
            throw new IllegalArgumentException(String.format(" id categoria %s inexistente", id));
        }
    }

    //ALTERAR A TABELA

    public CategoriaDTO update(CategoriaDTO categoriaDTO, Long id) {
        Optional<Categoria> categoriaExistenteOptical = this.iCategoriaRepository.findById(id);

        this.validate(categoriaDTO);

        Fornecedor fornecedor = categoriaExistenteOptical.get().getFornecedor();

        Categoria categoriaExistente = categoriaExistenteOptical.get();

        LOGGER.info("Atualizando categoria... id: [{}]", categoriaExistente.getId());
        LOGGER.debug("Payload: {}", categoriaDTO);
        LOGGER.debug("Categoria existente: {}", categoriaExistente);

        categoriaExistente.setNomeCategoria(categoriaDTO.getNomeCategoria());
        categoriaExistente.setFornecedor(fornecedor);
        categoriaExistente.setCodigoCategoria(categoriaDTO.getCodigoCategoria());

        categoriaExistente = this.iCategoriaRepository.save(categoriaExistente);

        return CategoriaDTO.of(categoriaExistente);
    }

    public CategoriaDTO updateCategoriaParaImportar(CategoriaDTO categoriaDTO, String codigoCategoria){

        this.validate(categoriaDTO);

        Categoria categoria = this.findByCodigoCategoria(codigoCategoria);

        LOGGER.info("Atualizando categoria... id: [{}]", categoria.getId());
        LOGGER.debug("Payload: {}", categoriaDTO);
        LOGGER.debug("Categoria existente: {}", categoria);

        categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());
        categoria.setFornecedor(fornecedorService.findOptionalById(categoriaDTO.getFornecedorId()));
        categoria.setCodigoCategoria(categoriaDTO.getCodigoCategoria());

        categoria = this.iCategoriaRepository.save(categoria);

        return categoriaDTO.of(categoria) ;
    }

    //EXPORT E IMPORT CSV

    public List<Categoria> categoriaList() {
        return this.iCategoriaRepository.findAll();
    }

    public void escrever(HttpServletResponse pergunta) throws Exception {


        String nomeArquivo = "arquivoCategoria.csv";
        pergunta.setContentType("text/csv");
        pergunta.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"");

        PrintWriter escrito = pergunta.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(escrito).withSeparator(';').withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER).withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        String[] tituloCSV = {"codigoCategoria", "nomeCategoria", "razaoSocial", "cnpj"};

        List<Categoria> categorias = this.categoriaList();

        icsvWriter.writeNext(tituloCSV);


        for (Categoria cat : iCategoriaRepository.findAll()) {
            icsvWriter.writeNext(new String[]{
                    cat.getCodigoCategoria(),
                    cat.getNomeCategoria(),
                    cat.getFornecedor().getRazaoSocial(),
                    formatarCnpj(cat.getFornecedor().getCnpj())
            });

        }
    }

    public String formatarCnpj(String cnpj) {
        String mask = "";

        mask = (cnpj.substring(0, 2) + "."
                + cnpj.substring(2, 5) + "."
                + cnpj.substring(5, 8) + "/"
                + cnpj.substring(8, 12) + "-"
                + cnpj.substring(12, 14));
        return mask;
    }

    public void ler(MultipartFile importacao) throws Exception {

        InputStreamReader inserir = new InputStreamReader(importacao.getInputStream());


        CSVReader leitor = new CSVReaderBuilder(inserir).withSkipLines(1).build();

        List<String[]> linhaLer = leitor.readAll();
        List<Categoria> resultado = new ArrayList<>();

        for (String[] linha : linhaLer) {

            try {

                String[] dados = linha[0].replaceAll("\"", "").split(";");

                Categoria categoria = new Categoria();
                if (iCategoriaRepository.existsByCodigoCategoria(dados[0])) {
                    LOGGER.info("Categoria {}: nome da categoria ja existe", dados[0]);
                }
                if (!iCategoriaRepository.existsByCodigoCategoria(dados[0])) {


                    categoria.setNomeCategoria((dados[1]));
                    categoria.setCodigoCategoria(dados[0]);
                    categoria.setFornecedor(fornecedorService.findByFornecedorCnpj(formatarImport(dados[3])));
                    resultado.add(categoria);

                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        iCategoriaRepository.saveAll(resultado);
    }

    public String formatarImport(String cnpj) {
        cnpj = cnpj.replace(".", "").replace("-", "").replace("/", "").replace(" ", "");
        System.out.println(cnpj);
        return cnpj;
    }

    //VALIDAÇÕES

    private void validate(CategoriaDTO categoriaDTO) {
        LOGGER.info("validando categoria");

        if (categoriaDTO == null) {
            throw new IllegalArgumentException("A categoria não pode ser nulo");
        }
        if (StringUtils.isEmpty(categoriaDTO.getNomeCategoria())) {
            throw new IllegalArgumentException("Nome categoria não pode ser nulo");
        }

        if (StringUtils.isEmpty(categoriaDTO.getCodigoCategoria())) {
            throw new IllegalArgumentException("codigo categoria não pode ser nulo");
        }
    }

    private void validateParaImport(CategoriaDTO categoriaDTO){
        LOGGER.info("validando categoria para import");

        if (categoriaDTO == null){
            throw new IllegalArgumentException("A categoria nao pode se nulo");
        }
        if (StringUtils.isEmpty(categoriaDTO.getNomeCategoria())) {
            throw new IllegalArgumentException("Nome categoria não pode ser nulo");
        }

        if (StringUtils.isEmpty(categoriaDTO.getCodigoCategoria())) {
            throw new IllegalArgumentException("codigo categoria não pode ser nulo");
        }
    }

    public Categoria findByCodigoCategoria(String codigoCategoria) {

        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findByCodigoCategoria(codigoCategoria);

        if (categoriaOptional.isPresent()) {
            return categoriaOptional.get();
        }

        throw new IllegalArgumentException(String.format("Codigo %s não existe", codigoCategoria));
    }

    public boolean existsByCodigoCategoria(String codigoCategoria) {
        return iCategoriaRepository.existsByCodigoCategoria(codigoCategoria);
    }


}

