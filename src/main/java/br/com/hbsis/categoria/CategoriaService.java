package br.com.hbsis.categoria;


import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
import com.opencsv.CSVReader;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService<ExportCSV> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaService.class);

    private final ICategoriaRepository iCategoriaRepository;
    private final FornecedorService fornecedorService;


    public CategoriaService(ICategoriaRepository iCategoriaRepository, FornecedorService fornecedorService){
        this.iCategoriaRepository = iCategoriaRepository;
        this.fornecedorService = fornecedorService;

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
    /* METODO PARA PERCORRER A LISTA*/

    public List<Categoria>categoriaList(){
        List<Categoria> categorias;
        categorias= this.iCategoriaRepository.findAll();
        return categorias;
    }

}
