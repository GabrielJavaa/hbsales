package br.com.hbsis.categoria;


import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.IFornecedorRepository;
import com.opencsv.CSVWriter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaService.class);

    private final ICategoriaRepository iCategoriaRepository;
    private final FornecedorService fornecedorService;
    private IFornecedorRepository iFornecedorRepository = null;

    public CategoriaService(ICategoriaRepository iCategoriaRepository, FornecedorService fornecedorService){
        this.iCategoriaRepository = iCategoriaRepository;
        this.fornecedorService = fornecedorService;
        this.iFornecedorRepository = iFornecedorRepository;

    }

    public CategoriaDTO save(CategoriaDTO categoriaDTO) {

        Fornecedor fornecedorService = this.fornecedorService.forneId(categoriaDTO.getFornecedorCategoria().getId());

        this.validate(categoriaDTO);

        LOGGER.info("Salvando Categorias");
        LOGGER.debug("Categoria: {}", categoriaDTO);

            Categoria categoria = new Categoria();
            categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());
            categoria.setFornecedorCategoria(fornecedorService);
            categoria.setCodigoCategoria(categoriaDTO.getCodigoCategoria());
            categoria= this.iCategoriaRepository.save(categoria);
            return CategoriaDTO.of(categoria);
        }

    public void validate(CategoriaDTO categoriaDTO) {
        LOGGER.info("validando fornecedor");

        if (categoriaDTO == null){
            throw new IllegalArgumentException("A categoria não pode ser nulo");
        }
        if (StringUtils.isEmpty(categoriaDTO.getNomeCategoria())){
            throw new IllegalArgumentException("Nome categoria não pode ser nulo");
        }

        if (categoriaDTO.getFornecedorCategoria().getId() == null){
            throw new IllegalArgumentException("fornecedor categoria não pode ser nulo");
        }
        if (categoriaDTO.getCodigoCategoria() == 0){
            throw new IllegalArgumentException("codigo categoria não pode ser nulo");
        }

    }

    public CategoriaDTO findById(Long id){
        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findById(id);

            if(categoriaOptional.isPresent()){
                return CategoriaDTO.of(categoriaOptional.get());
            }

            throw new IllegalArgumentException(String.format("id %s não existe", id));
    }


    public CategoriaDTO update(CategoriaDTO categoriaDTO, Long id) {
        Optional<Categoria> categoriaExistenteOptical = this.iCategoriaRepository.findById(id);


                Categoria categoriaExistente = categoriaExistenteOptical.get();


                LOGGER.info("Atualizando categoria... id: [{}]", categoriaExistente.getId());
                LOGGER.debug("Payload: {}", categoriaDTO);
                LOGGER.debug("Categoria existente: {}", categoriaExistente);

                categoriaExistente.setNomeCategoria(categoriaDTO.getNomeCategoria());
                categoriaExistente.setFornecedorCategoria(categoriaDTO.getFornecedorCategoria());
                categoriaExistente.setCodigoCategoria(categoriaDTO.getCodigoCategoria());

                categoriaExistente = this.iCategoriaRepository.save(categoriaExistente);

                return categoriaDTO.of(categoriaExistente);
            }




    public void delete(Long id){
        LOGGER.info("Excluindo Categoria id: [{}]", id);

        this.iCategoriaRepository.deleteById(id);
    }

    public List<Categoria> categoriaList(Writer writer){
        List<Categoria> categorias = new ArrayList<>();
        categorias = this.iCategoriaRepository.findAll();
        return categorias;
    }

    public void escrever(Writer writer) {
        CSVWriter csvWriter = new CSVWriter(writer,
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END
        );
        String[] dados = {"id;nomeCategoria;fornecedorCategoria;codigoCategoria"};

        csvWriter.writeNext(dados);

        List<Categoria> categoriaList = this.iCategoriaRepository.findAll();
        for (Categoria categoria : categoriaList) {
            csvWriter.writeNext(new String[]{
                    categoria.getId() + ";" +
                    categoria.getNomeCategoria() + ";" +
                    categoria.getFornecedorCategoria().getRazaoSocial() + ";" +
                    categoria.getCodigoCategoria() + ";"
            });

        }
    }

    public void importcsv(MultipartFile csvfile) throws IOException {


        String linha="";
        String esc=";";

        try{BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(csvfile.getInputStream()));

            while ((linha = bufferedReader.readLine()) !=null) {

                String[] country = linha.split(esc);

                Categoria categoria = new Categoria();
                Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findById(Long.parseLong(country[2]));

                if (fornecedorOptional.isPresent()) {
                    Fornecedor fornecedor = fornecedorOptional.get();

                    categoria.setNomeCategoria(country[1]);
                    categoria.setCodigoCategoria(Integer.parseInt(country[3]));
                    categoria.setFornecedorCategoria(fornecedor);

                    this.iCategoriaRepository.save(categoria);
                } else {
                    throw new IllegalArgumentException(String.format("id %s nao existe",Long.parseLong(country[2])));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }





}
