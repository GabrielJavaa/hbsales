package br.com.hbsis.fornecedor;


import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FornecedorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FornecedorService.class);

    private final IFornecedorRepository iFornecedorRepository;

    public FornecedorService(IFornecedorRepository iFornecedorRepository) { this.iFornecedorRepository = iFornecedorRepository; }

    public FornecedorDTO save(FornecedorDTO fornecedorDTO) throws IllegalAccessException {

        this.validate(fornecedorDTO);

        LOGGER.info("salvando fornecedor");
        LOGGER.debug("Fornecedor: {}", fornecedorDTO);

        Fornecedor fornecedor = new Fornecedor();

        fornecedor.setRazaoSocial(fornecedorDTO.getRazaoSocial());
        fornecedor.setCNPJ(fornecedorDTO.getCnpj());
        fornecedor.setNomeFantasia(fornecedorDTO.getNomeFantasia());
        fornecedor.setEndereco(fornecedorDTO.getEndereco());
        fornecedor.setTelefone(fornecedorDTO.getTelefone());
        fornecedor.setEmail(fornecedorDTO.getEmail());

        fornecedor = this.iFornecedorRepository.save(fornecedor);

        return FornecedorDTO.of(fornecedor);
    }

    private void validate(FornecedorDTO fornecedorDTO) throws IllegalAccessException {
        LOGGER.info("validando fornecedor");

        if (fornecedorDTO == null){
            throw new IllegalAccessException("O fornecedor não pode ser nulo");
        }
        if (StringUtils.isEmpty(fornecedorDTO.getRazaoSocial())){
            throw new IllegalAccessException("Razao social não pode ser nulo");
        }
        if (StringUtils.isEmpty(fornecedorDTO.getCnpj())){
            throw new IllegalAccessException("CNPJ não pode ser nulo");
        }
        if (StringUtils.isEmpty(fornecedorDTO.getNomeFantasia())){
            throw new IllegalAccessException("Nome Fantasia não pode ser nulo");
        }
        if (StringUtils.isEmpty(fornecedorDTO.getEndereco())){
            throw new IllegalAccessException("Endereço não pode ser nullo");
        }
        if (StringUtils.isEmpty(fornecedorDTO.getTelefone())){
            throw new IllegalAccessException("Telefone não pode ser nullo");
        }
        if (StringUtils.isEmpty(fornecedorDTO.getEmail())){
            throw new IllegalAccessException("E-mail não pode ser nullo");
        }
    }


    public FornecedorDTO findById(Long id){

        Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findById(id);

        if (fornecedorOptional.isPresent()){
            return FornecedorDTO.of(fornecedorOptional.get());
        }

        throw new IllegalArgumentException(String.format("id %s não existente", id));
    }


    public FornecedorDTO update(FornecedorDTO fornecedorDTO, Long id) throws IllegalAccessException {
        Optional<Fornecedor> fornecedorExistenteOptional = this.iFornecedorRepository.findById(id);

            if (fornecedorExistenteOptional.isPresent()){
                Fornecedor fornecedorExistente = fornecedorExistenteOptional.get();

                LOGGER.info("Alterando fornecedores pelo id: [{}]", fornecedorExistente.getId());
                LOGGER.debug("payaload: {}", fornecedorDTO);
                LOGGER.debug("Fornecedor Existente:{}", fornecedorExistente);

                fornecedorExistente.setRazaoSocial(fornecedorDTO.getRazaoSocial());
                fornecedorExistente.setCNPJ(fornecedorDTO.getCnpj());
                fornecedorExistente.setNomeFantasia(fornecedorDTO.getNomeFantasia());
                fornecedorExistente.setEndereco(fornecedorDTO.getEndereco());
                fornecedorExistente.setTelefone(fornecedorDTO.getTelefone());
                fornecedorExistente.setEmail(fornecedorDTO.getEmail());

                fornecedorExistente = this.iFornecedorRepository.save(fornecedorExistente);

                return FornecedorDTO.of(fornecedorExistente);
            }

            throw new IllegalAccessException(String.format("id %s nao existente", id));
    }

    public void delete(Long id){

        LOGGER.info("Deletando Fornecedor pelo id: [{}]",id);

        this.iFornecedorRepository.deleteById(id);
    }
}
