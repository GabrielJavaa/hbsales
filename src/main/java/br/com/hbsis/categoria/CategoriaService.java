package br.com.hbsis.categoria;


import br.com.hbsis.fornecedor.FornecedorService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaService.class);

    private final ICategoriaRepository iCategoriaRepository;


    public CategoriaService(ICategoriaRepository iCategoriaRepository){
        this.iCategoriaRepository = iCategoriaRepository;

    }

    public CategoriaDTO save(CategoriaDTO categoriaDTO) {


        LOGGER.info("Salvando Categorias");
        LOGGER.debug("Categoria: {}", categoriaDTO);

                Categoria categoria = new Categoria();
                categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());
                categoria.setFornecedorCategoria(categoriaDTO.getFornecedorCategoria());
                categoria.setCodigoCategoria(categoriaDTO.getCodigoCategoria());
                categoria= this.iCategoriaRepository.save(categoria);
                return CategoriaDTO.of(categoria);
            }

    private void validate(CategoriaDTO categoriaDTO) throws IllegalAccessException {
        LOGGER.info("validando fornecedor");

        if (categoriaDTO == null){
            throw new IllegalAccessException("A categoria não pode ser nulo");
        }
        if (StringUtils.isEmpty(categoriaDTO.getNomeCategoria())){
            throw new IllegalAccessException("Razao social não pode ser nulo");
        }
        if (StringUtils.isEmpty(categoriaDTO.getFornecedorCategoria())){
            throw new IllegalAccessException("CNPJ não pode ser nulo");
        }
        if (StringUtils.isEmpty(categoriaDTO.getCodigoCategoria())){
            throw new IllegalAccessException("Nome Fantasia não pode ser nulo");
        }

    }

    public CategoriaDTO findById(Long id) throws IllegalAccessException {
        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findById(id);

            if(categoriaOptional.isPresent()){
                return CategoriaDTO.of(categoriaOptional.get());
            }

            throw new IllegalAccessException(String.format("id %s não existe", id));
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


}
