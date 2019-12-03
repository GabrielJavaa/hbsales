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

    public FornecedorDTO save(FornecedorDTO fornecedorDTO) {

        this.validate(fornecedorDTO);

        LOGGER.info("salvando fornecedor");
        LOGGER.debug("Fornecedor: {}", fornecedorDTO);

        Fornecedor fornecedor = new Fornecedor();

        fornecedor.setRazaoSocial(fornecedorDTO.getRazaoSocial());
        fornecedor.setCnpj(fornecedorDTO.getCnpj());
        fornecedor.setNomeFantasia(fornecedorDTO.getNomeFantasia());
        fornecedor.setEndereco(fornecedorDTO.getEndereco());
        fornecedor.setTelefone(fornecedorDTO.getTelefone());
        fornecedor.setEmail(fornecedorDTO.getEmail());

        fornecedor = this.iFornecedorRepository.save(fornecedor);

        return FornecedorDTO.of(fornecedor);
    }

    private void validate(FornecedorDTO fornecedorDTO){
        LOGGER.info("validando fornecedor");

        if (fornecedorDTO == null){
            throw new IllegalArgumentException("O fornecedor não pode ser nulo");
        }
        if (StringUtils.isEmpty(fornecedorDTO.getRazaoSocial())){
            throw new IllegalArgumentException("Razao social não pode ser nulo");
        }
        if (StringUtils.isEmpty(fornecedorDTO.getCnpj())){
            throw new IllegalArgumentException("CNPJ não pode ser nulo");
        }
        if (StringUtils.isEmpty(fornecedorDTO.getNomeFantasia())){
            throw new IllegalArgumentException("Nome Fantasia não pode ser nulo");
        }
        if (StringUtils.isEmpty(fornecedorDTO.getEndereco())){
            throw new IllegalArgumentException("Endereço não pode ser nullo");
        }
        if (StringUtils.isEmpty(fornecedorDTO.getTelefone())){
            throw new IllegalArgumentException("Telefone não pode ser nullo");
        }
        if (StringUtils.isEmpty(fornecedorDTO.getEmail())){
            throw new IllegalArgumentException("E-mail não pode ser nullo");
        }
    }


    public FornecedorDTO findById(Long id){

        Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findById(id);

        if (fornecedorOptional.isPresent()){
            return FornecedorDTO.of(fornecedorOptional.get());
        }

        throw new IllegalArgumentException(String.format("id %s não existente", id));
    }

    public Optional<Fornecedor> findOptionalById(Long id){

        Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findById(id);

        if (fornecedorOptional.isPresent()){
            return fornecedorOptional;
        }

        throw new IllegalArgumentException(String.format("id %s não existente", id));
    }


    public FornecedorDTO update(FornecedorDTO fornecedorDTO, Long id){
        Optional<Fornecedor> fornecedorExistenteOptional = this.iFornecedorRepository.findById(id);

            if (fornecedorExistenteOptional.isPresent()){
                Fornecedor fornecedorExistente = fornecedorExistenteOptional.get();

                LOGGER.info("Alterando fornecedores pelo id: [{}]", fornecedorExistente.getId());
                LOGGER.debug("payaload: {}", fornecedorDTO);
                LOGGER.debug("Fornecedor Existente:{}", fornecedorExistente);

                fornecedorExistente.setRazaoSocial(fornecedorDTO.getRazaoSocial());
                fornecedorExistente.setCnpj(fornecedorDTO.getCnpj());
                fornecedorExistente.setNomeFantasia(fornecedorDTO.getNomeFantasia());
                fornecedorExistente.setEndereco(fornecedorDTO.getEndereco());
                fornecedorExistente.setTelefone(fornecedorDTO.getTelefone());
                fornecedorExistente.setEmail(fornecedorDTO.getEmail());

                fornecedorExistente = this.iFornecedorRepository.save(fornecedorExistente);

                return FornecedorDTO.of(fornecedorExistente);
            }

            throw new IllegalArgumentException(String.format("id %s nao existente", id));
    }

    public void delete(Long id){

        LOGGER.info("Deletando Fornecedor pelo id: [{}]",id);

        this.iFornecedorRepository.deleteById(id);
    }

    public Optional <Fornecedor> forneId(String rz){
        Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findByRazaoSocial(rz);

        if (fornecedorOptional.isPresent()){
            return fornecedorOptional;
        }

        throw new IllegalArgumentException(String.format("id %s fornecedor nao existe"));


    }
}
