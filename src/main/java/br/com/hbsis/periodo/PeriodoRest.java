package br.com.hbsis.periodo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/periodo")
public class PeriodoRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodoRest.class);

    private final PeriodoService periodoService;

    @Autowired
    public PeriodoRest(PeriodoService periodoService) {
        this.periodoService = periodoService;
    }

    @PostMapping
        public PeriodoDTO save(@RequestBody PeriodoDTO periodoDTO) {
            LOGGER.info("Recendo solicitação de persistencia do produto...");
            LOGGER.debug("Recebendo {}", periodoDTO);

            return this.periodoService.save(periodoDTO);
    }
    @GetMapping("/{id}")
    public PeriodoDTO find(@PathVariable("id") Long id){
        LOGGER.info("Buscando Produto pelo id... id: [{}]",id);
        return this.periodoService.findById(id);
    }
    @PutMapping("/{id}")
    public PeriodoDTO update(@PathVariable ("id") Long id, @RequestBody PeriodoDTO periodoDTO){
        LOGGER.info("Alterando periodo pelo id... id: [{}]", id);
        LOGGER.debug("Recebendo {}", periodoDTO);

        return this.periodoService.update(periodoDTO, id);
    }
}

