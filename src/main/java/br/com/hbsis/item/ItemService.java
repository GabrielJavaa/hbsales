package br.com.hbsis.item;

import br.com.hbsis.pedido.Pedido;
import br.com.hbsis.pedido.PedidoService;
import br.com.hbsis.produto.Produto;
import br.com.hbsis.produto.ProdutoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private final Logger LOGGER = LoggerFactory.getLogger(ItemService.class);
    private final IItemRepository iItemRepository;
    private final PedidoService pedidoService;
    private final ProdutoService produtoService;

    public ItemService(IItemRepository iItemRepository,  PedidoService pedidoService, ProdutoService produtoService) {
        this.iItemRepository = iItemRepository;
        this.pedidoService = pedidoService;
        this.produtoService = produtoService;
    }

    public ItemDTO save(ItemDTO itemDTO) {
        this.validate(itemDTO);
        LOGGER.info("Criando itens");
        LOGGER.debug("Item : {}", itemDTO);

        Item item = new Item();
        Produto produto = this.produtoService.findOptionalById(itemDTO.getProduto().getId());
        Pedido pedido = this.pedidoService.findByOptionalId(itemDTO.getPedido());

        item.setQuantidade(itemDTO.getQuantidade());
        item.setProduto(produto);
        item.setPedido(pedido);

        this.iItemRepository.save(item);
        return ItemDTO.of(item);
    }
    public ItemDTO findById(Long id){
        Optional<Item> itemOptional = this.iItemRepository.findById(id);

        if (itemOptional.isPresent()){
            return ItemDTO.of(itemOptional.get());
        }
        throw new IllegalArgumentException(String.format("id %s nao existe",id));
    }
    public List<Item> findByItemId(Pedido pedido){
        List<Item> items = this.iItemRepository.findAllByPedido(pedido);
        if (items != null ){
            return items;
        }
        throw new IllegalArgumentException(String.format("id %s nao existe"));
    }


    public ItemDTO update(ItemDTO itemDTO, Long id){
        this.validate(itemDTO);
        Optional<Item> itemOptionalExistente = this.iItemRepository.findById(id);

        if (itemOptionalExistente.isPresent()){
            Item itemOptional = itemOptionalExistente.get();
            LOGGER.info("Atualizando item: [{}]",itemOptional.getId());
            LOGGER.debug("item inexistente: {}",itemDTO);
            LOGGER.debug("Item existente : {}", itemOptional);

            itemOptional.setQuantidade(itemDTO.getQuantidade());
            itemOptional.setPedido(pedidoService.findByOptionalId(itemDTO.getPedido()));
            itemOptional.setProduto(produtoService.findOptionalById(itemDTO.getProduto().getId()));

            this.iItemRepository.save(itemOptional);

            return ItemDTO.of(itemOptional);
        }
        throw new IllegalArgumentException(String.format("id do Item %s inexistente"));
    }
    public void validate(ItemDTO itemDTO){
        LOGGER.info("Validando Itens");

        if (itemDTO == null){
            throw new IllegalArgumentException(String.format("Coluna de itens não pode ser nulo"));
        }
        if (StringUtils.isEmpty(String.valueOf(itemDTO.getQuantidade()))){
            throw new IllegalArgumentException(String.format("Quantidade de Itens não pode ser nulo"));
        }
        if (StringUtils.isEmpty(String.valueOf(itemDTO.getPedido()))){
            throw new IllegalArgumentException(String.format("Pedido não pode ser nulo"));
        }
        if (StringUtils.isEmpty(String.valueOf(itemDTO.getProduto()))){
            throw new IllegalArgumentException(String.format("Produto não pode ser nulo"));
        }
    }

}
