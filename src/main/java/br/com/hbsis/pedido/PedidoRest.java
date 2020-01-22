package br.com.hbsis.pedido;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
    @GetMapping("/export.csv/{id}")
    public void exportCSV(HttpServletResponse file,@PathVariable ("id") Long id) throws Exception {
        pedidoService.escreverPedido(file, id);
    }
    @GetMapping("/exportCSV/{id}")
    public void exportProdutoCSV(HttpServletResponse file,@PathVariable ("id") Long id) throws Exception {
        pedidoService.escreverVendaProduto(file, id);
    }
    @GetMapping("/pedido/{id}")
    public List<PedidoDTO> findPedido(@PathVariable("id") Long id){
        LOGGER.info("Buscando pedidos pelo id : [{}]", id);
        return this.pedidoService.findAllPedidos(id);
    }
    @PutMapping("/cancelaPedido/{id}")
    public PedidoDTO updateCancela(@PathVariable ("id") Long id, PedidoDTO pedidoDTO){
        LOGGER.info("Alterando pedidos pelo id... id: [{}]", id);
        LOGGER.debug("Recebendo {}", pedidoDTO);

        return this.pedidoService.cancelaPedido(pedidoDTO, id);
    }
    @PutMapping("/editaPedido/{id}")
    public PedidoDTO updateEdita(@PathVariable ("id") Long id, PedidoDTO pedidoDTO){
        LOGGER.info("Editanto periodo id: [{}]", id);
        LOGGER.debug("Recebendo {}",pedidoDTO);
        return this.pedidoService.editaPedido(pedidoDTO,id);
    }
}
