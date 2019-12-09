package br.com.hbsis.categoria;


import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorDTO;
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

    public CategoriaDTO save(CategoriaDTO categoriaDTO) {

        Optional<Fornecedor> fornecedorOptional = this.fornecedorService.findOptionalById(categoriaDTO.getFornecedorCategoria().getId());

        this.validate(categoriaDTO);

        LOGGER.info("Salvando Categorias");
        LOGGER.debug("Categoria: {}", categoriaDTO);

        Fornecedor fornecedor = fornecedorOptional.get();

        Categoria categoria = new Categoria();
        categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());
        categoria.setFornecedorCategoria(fornecedor);
        categoria.setCodigoCategoria(categoriaDTO.getCodigoCategoria());
        categoria = this.iCategoriaRepository.save(categoria);
        return CategoriaDTO.of(categoria);
    }

    private void validate(CategoriaDTO categoriaDTO) {
        LOGGER.info("validando fornecedor");

        if (categoriaDTO == null) {
            throw new IllegalArgumentException("A categoria não pode ser nulo");
        }
        if (StringUtils.isEmpty(categoriaDTO.getNomeCategoria())) {
            throw new IllegalArgumentException("Nome categoria não pode ser nulo");
        }

        if (categoriaDTO.getFornecedorCategoria().getId() == null) {
            throw new IllegalArgumentException("fornecedor categoria não pode ser nulo");
        }
        if (categoriaDTO.getCodigoCategoria() == 0) {
            throw new IllegalArgumentException("codigo categoria não pode ser nulo");
        }

    }

    public CategoriaDTO findById(Long id) {
        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findById(id);

        if (categoriaOptional.isPresent()) {
            return CategoriaDTO.of(categoriaOptional.get());
        }

        throw new IllegalArgumentException(String.format("id %s não existe", id));
    }

    public Optional<Categoria> findOptionalinById(Long id) {
        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findById(id);
        if (categoriaOptional.isPresent()) {
            return categoriaOptional;
        }
        throw new IllegalArgumentException(String.format("id %s nao existe", id));
    }



    public CategoriaDTO update(CategoriaDTO categoriaDTO, Long id) {
        Optional<Categoria> categoriaExistenteOptical = this.iCategoriaRepository.findById(id);

        if (categoriaExistenteOptical.isPresent()) {
            Categoria categoriaExistente = categoriaExistenteOptical.get();


            LOGGER.info("Atualizando categoria... id: [{}]", categoriaExistente.getId());
            LOGGER.debug("Payload: {}", categoriaDTO);
            LOGGER.debug("Categoria existente: {}", categoriaExistente);

            categoriaExistente.setNomeCategoria(categoriaDTO.getNomeCategoria());
            categoriaExistente.setFornecedorCategoria(categoriaDTO.getFornecedorCategoria());
            categoriaExistente.setCodigoCategoria(categoriaDTO.getCodigoCategoria());

            categoriaExistente = this.iCategoriaRepository.save(categoriaExistente);

            return CategoriaDTO.of(categoriaExistente);
        }
        throw new IllegalArgumentException(String.format("id %s nao existente", id));
    }


    public void delete(Long id) {
        LOGGER.info("Excluindo Categoria id: [{}]", id);

        this.iCategoriaRepository.deleteById(id);
    }


    void escrever(HttpServletResponse pergunta) throws Exception {
        String nomeArquivo = "arquivoCategoria.csv";
        pergunta.setContentType("text/csv");
        pergunta.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"");

        PrintWriter escrito = pergunta.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(escrito).withSeparator(';').withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER).withLineEnd(CSVWriter.DEFAULT_LINE_END).build();
        String[] tituloCSV = {"id", "nomeCategoria", "fornecedorCategoria", "codigoCategoria"};
        icsvWriter.writeNext(tituloCSV);

        for (Categoria linhas : iCategoriaRepository.findAll()) {
            icsvWriter.writeNext(new String[]{String.valueOf(linhas.getId()), linhas.getNomeCategoria(), String.valueOf(linhas.getFornecedorCategoria().getId()), linhas.getCodigoCategoria().toString()})
            ;
        }
    }

    void ler(MultipartFile importacao) throws Exception {
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
                Fornecedor fornecedor = fornecedorService.findIdFornecedor(Long.parseLong(dados[2]));
                categoria.setCodigoCategoria(Integer.parseInt(dados[3]));

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


                categoria.setCodigoCategoria(Integer.parseInt(dados[3]));
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
}
