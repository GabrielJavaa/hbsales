package br.com.hbsis.funcionario;

import br.com.hbsis.api.employee.EmployeeSaveDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Service
public class FuncionarioService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FuncionarioService.class);

    private final IFuncionarioRepository iFuncionarioRepository;
    private final EmployeeSaveDTO employeeSaveDTO;

    @Autowired
    public FuncionarioService(IFuncionarioRepository iFuncionarioRepository, EmployeeSaveDTO employeeSaveDTO){
        this.iFuncionarioRepository = iFuncionarioRepository;
        this.employeeSaveDTO = employeeSaveDTO;
    }

    public FuncionarioDTO save(FuncionarioDTO funcionarioDTO){

        this.employeeValidarFuncionario(funcionarioDTO);

        LOGGER.info("Salvando Funcionario");
        LOGGER.debug("Funcionario : {}",funcionarioDTO);

        Funcionario funcionario = new Funcionario();
        funcionario.setNomeFuncionario(funcionarioDTO.getNome());
        funcionario.setEmailFuncionario(funcionarioDTO.getEmail());
        funcionario.setUuid(funcionarioDTO.getUuid());

        this.iFuncionarioRepository.save(funcionario);
        LOGGER.info("funcionario salvo ");
        return FuncionarioDTO.of(funcionario);
    }
    public FuncionarioDTO update(FuncionarioDTO funcionarioDTO){
        LOGGER.info("Atualizando funcionario");

        Optional<Funcionario> funcionarioOptional = this.iFuncionarioRepository.findById(funcionarioDTO.getId());

        if (funcionarioOptional.isPresent()){
            Funcionario funcionario = funcionarioOptional.get();
        LOGGER.info("Atualizando informações do funcionario pelo id... id:[{}]",funcionario.getId());
        LOGGER.debug("Recebendo att {}",funcionarioDTO);
        LOGGER.debug("funcionario existente: {}",funcionario);

        funcionario.setNomeFuncionario(funcionarioDTO.getNome());
        funcionario.setEmailFuncionario(funcionarioDTO.getEmail());

        return FuncionarioDTO.of(this.iFuncionarioRepository.save(funcionario));
        }
        throw new IllegalArgumentException(String.format("id nao existe", funcionarioDTO.getId()));
    }
    public FuncionarioDTO findById(Long id){
        Optional<Funcionario> funcionarioOptional = this.iFuncionarioRepository.findById(id);

            if (funcionarioOptional.isPresent()){
                return FuncionarioDTO.of(funcionarioOptional.get());
            }
            throw new IllegalArgumentException(String.format("id %s inexistente",id));
    }
    public Funcionario findByFuncionarioId (Long id){
        Optional<Funcionario> funcionarioOptional = this.iFuncionarioRepository.findById(id);
        if (funcionarioOptional.isPresent()){
            return funcionarioOptional.get();
        }
        throw new IllegalArgumentException(String.format("id %s do funcionario não existe", id));
    }

    private void employeeValidarFuncionario(FuncionarioDTO funcionarioDTO){
        String urlApi = "http://10.2.54.25:9999/api/employees";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","f59ffc86-1b67-11ea-978f-2e728ce88125");
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity httpEntity = new HttpEntity<>(funcionarioDTO,httpHeaders);
        ResponseEntity<EmployeeSaveDTO> responseEntity = restTemplate.exchange(urlApi, HttpMethod.POST, httpEntity, EmployeeSaveDTO.class);
        funcionarioDTO.setUuid(Objects.requireNonNull(responseEntity.getBody().getEmployeeUuid()));

    }

}
