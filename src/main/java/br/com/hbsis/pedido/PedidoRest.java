package br.com.hbsis.pedido;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedido")
public class PedidoRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoService.class);

    private final PedidoService pedidoService;

    @Autowired

    public PedidoRest(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public PedidoDTO save(@RequestBody PedidoDTO pedidoDTO){
        LOGGER.info("Salvando Pedidos");
        LOGGER.debug("recebendo {}",pedidoDTO);
        return this.pedidoService.save(pedidoDTO);
    }
    @GetMapping("/{id}")
    public PedidoDTO find(@PathVariable("id") Long id){
        LOGGER.info("Buscando pedidos pelo id : [{}]", id);
        return this.pedidoService.findById(id);
    }
    @PutMapping("/{id}")
    public PedidoDTO update(@PathVariable ("id") Long id, @RequestBody PedidoDTO pedidoDTO){
        LOGGER.info("Alterando pedidos pelo id... id: [{}]", id);
        LOGGER.debug("Recebendo {}", pedidoDTO);

        return this.pedidoService.update(pedidoDTO, id);
    }
}
