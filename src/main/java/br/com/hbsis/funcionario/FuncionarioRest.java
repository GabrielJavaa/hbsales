package br.com.hbsis.funcionario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FuncionarioRest.class);

    private final FuncionarioService funcionarioService;

    @Autowired
    public FuncionarioRest(FuncionarioService funcionarioService){
        this.funcionarioService = funcionarioService;
    }
    @PostMapping
    public FuncionarioDTO save (@RequestBody FuncionarioDTO FuncionarioDTO){
        return this.funcionarioService.save(FuncionarioDTO);
    }
    @GetMapping("/{id}")
    public FuncionarioDTO find(@PathVariable ("id") Long id){
        LOGGER.info("buscando id do funcionario... id:[{}]", id);
        return this.funcionarioService.findById(id);
    }
    @PutMapping
    public FuncionarioDTO update(@RequestBody FuncionarioDTO funcionarioDTO){
        return this.funcionarioService.update(funcionarioDTO);
    }

}
