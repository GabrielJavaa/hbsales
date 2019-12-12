package br.com.hbsis.categoria;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.IFornecedorRepository;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
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
    private final IFornecedorRepository iFornecedorRepository;

    public CategoriaService(ICategoriaRepository iCategoriaRepository, FornecedorService fornecedorService, IFornecedorRepository iFornecedorRepository) {
        this.iCategoriaRepository = iCategoriaRepository;
        this.fornecedorService = fornecedorService;
        this.iFornecedorRepository = iFornecedorRepository;
    }

    //CADASTRAR
    public CategoriaDTO save(CategoriaDTO categoriaDTO) {

        Optional<Fornecedor> fornecedorOptional = this.fornecedorService.findOptionalById(categoriaDTO.getIdFornecedorCategoria());

        this.validate(categoriaDTO);

        LOGGER.info("Salvando Categorias");
        LOGGER.debug("Categoria: {}", categoriaDTO);

        Fornecedor fornecedor = fornecedorOptional.get();

        Categoria categoria = new Categoria();
        categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());
        categoria.setFornecedor(fornecedor);
        categoria.setCodigoCategoria(categoriaDTO.getCodigoCategoria());
        categoria = this.iCategoriaRepository.save(categoria);
        return CategoriaDTO.of(categoria);
    }


//PERCORRER TABELA

    public CategoriaDTO findById(Long id) {
        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findById(id);

        if (categoriaOptional.isPresent()) {
            return CategoriaDTO.of(categoriaOptional.get());
        }

        throw new IllegalArgumentException(String.format("id %s não existe", id));
    }

//FAZER LIGAÇÃO COM A CATEGORIA LINHA

    public Optional<Categoria> findOptionalinById(Long id) {
        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findById(id);
        if (categoriaOptional.isPresent()) {
            return categoriaOptional;
        }
        throw new IllegalArgumentException(String.format("id %s nao existe", id));
    }

//ALTERAR A TABELA

    public CategoriaDTO update(CategoriaDTO categoriaDTO, Long id) {
        Optional<Categoria> categoriaExistenteOptical = this.iCategoriaRepository.findById(id);
        Fornecedor fornecedor = new Fornecedor();

        if (categoriaExistenteOptical.isPresent()) {
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
        throw new IllegalArgumentException(String.format("id %s nao existente", id));
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


                categoria.setId((int) Long.parseLong(dados[0]));
                categoria.setNomeCategoria((dados[1]));
                Optional<Fornecedor> fornecedor = fornecedorService.findOptionalById(Long.parseLong(dados[2]));
                categoria.setCodigoCategoria((dados[3]));

                resultado.add(categoria);
                System.out.println(resultado);


            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        iCategoriaRepository.saveAll(resultado);

    }

    public List<Categoria> lertudo(MultipartFile file) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader).withSkipLines(1).build();
        List<String[]> linha = csvReader.readAll();
        List<Categoria> result = new ArrayList<>();

        for (String[] lin : linha) {
            try {
                String[] dados = lin[1].replaceAll("\"", "").split(";");

                Categoria categoria = new Categoria();
                Fornecedor fornecedor = new Fornecedor();
                FornecedorDTO fornecedorDTO = new FornecedorDTO();


                categoria.setCodigoCategoria((dados[3]));
                categoria.setNomeCategoria((dados[1]));
                fornecedorDTO = fornecedorService.findById(Long.parseLong(dados[2]));

                fornecedor.setId(fornecedorDTO.getId());
                fornecedor.setRazaoSocial(fornecedorDTO.getRazaoSocial());
                fornecedor.setCnpj(fornecedorDTO.getCnpj());
                fornecedor.setNomeFantasia(fornecedorDTO.getNomeFantasia());
                fornecedor.setEndereco(fornecedorDTO.getEndereco());
                fornecedor.setTelefone(fornecedorDTO.getTelefone());
                fornecedor.setEmail(fornecedorDTO.getEmail());

                categoria.setId(Long.parseLong(String.valueOf(fornecedor)));

                result.add(categoria);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return iCategoriaRepository.saveAll(result);
    }
//VALIDAÇÕES

    public String codigoCNPJ(CategoriaDTO categoriaDTO) {
        LOGGER.info("Codigo ...");
        Optional<Fornecedor> fornecedor = this.iFornecedorRepository.findById(categoriaDTO.getIdFornecedorCategoria());

        String CnpjCod = "";
        String Cnpj1 = "CAT";
        String Cnpj2 = fornecedor.get().getCnpj();
        String Cnpj3 = "";

        if (categoriaDTO.getCodigoCategoria().length() == 3) {
            Cnpj3 = categoriaDTO.getCodigoCategoria();
        }
        if (categoriaDTO.getCodigoCategoria().length() == 2) {
            Cnpj3 = "0" + categoriaDTO.getCodigoCategoria();
        }
        if (categoriaDTO.getCodigoCategoria().length() == 1) {
            Cnpj3 = "00" + categoriaDTO.getCodigoCategoria();
        }
        CnpjCod = Cnpj1 + Cnpj2 + Cnpj3;

        return CnpjCod;
    }

    private void validate(CategoriaDTO categoriaDTO) {
        LOGGER.info("validando fornecedor");

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
}
