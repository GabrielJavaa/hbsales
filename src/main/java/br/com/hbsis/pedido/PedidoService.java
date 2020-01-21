package br.com.hbsis.pedido;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.funcionario.FuncionarioService;
import br.com.hbsis.item.Item;
import br.com.hbsis.item.ItemDTO;
import br.com.hbsis.item.ItemService;
import br.com.hbsis.periodo.Periodo;
import br.com.hbsis.periodo.PeriodoService;
import br.com.hbsis.produto.Produto;
import br.com.hbsis.produto.ProdutoService;
import com.microsoft.sqlserver.jdbc.StringUtils;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class PedidoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoService.class);

    private final IRepositoryPedido iRepositoryPedido;
    private final PeriodoService periodoService;
    private final FornecedorService fornecedorService;
    private final ProdutoService produtoService;
    private final FuncionarioService funcionarioService;
    private final ItemService itemService;


    public PedidoService(IRepositoryPedido iRepositoryPedido, PeriodoService periodoService, FornecedorService fornecedorService, ProdutoService produtoService, FuncionarioService funcionarioService,@Lazy ItemService itemService) {
        this.iRepositoryPedido = iRepositoryPedido;
        this.periodoService = periodoService;
        this.fornecedorService = fornecedorService;
        this.produtoService = produtoService;
        this.funcionarioService = funcionarioService;
        this.itemService = itemService;
    }

    public PedidoDTO findById(Long id) {
        Optional<Pedido> pedidoOptional = this.iRepositoryPedido.findById(id);
        if (pedidoOptional.isPresent()) {
            return PedidoDTO.of(pedidoOptional.get());
        }
        throw new IllegalArgumentException(String.format("id %s não informado", id));
    }

    public Pedido findByOptionalId(Long id) {
        Optional<Pedido> pedidoOptional = this.iRepositoryPedido.findById(id);
        if (pedidoOptional.isPresent()) {
            return pedidoOptional.get();
        }
        throw new IllegalArgumentException(String.format("id %s não informado", id));
    }


    public PedidoDTO save(PedidoDTO pedidoDTO) {

        LOGGER.info("Salvando pedido");
        LOGGER.debug("Pedido : {}", pedidoDTO);

        this.validateSalvar(pedidoDTO);
        Pedido pedido = new Pedido();

        Fornecedor fornecedor = this.fornecedorService.findByIdFornecedor(pedidoDTO.getFornecedor());
        Periodo periodo = this.periodoService.findByPeriodo(pedidoDTO.getPeriodo());
        Funcionario funcionario = this.funcionarioService.findByFuncionarioId(pedidoDTO.getFuncionario());

        List<Item> items = new ArrayList<>();

        pedido.setCodigoUnico(pedidoDTO.getCodigoUnico());
        pedido.setStatusPedido("Ativo");
        pedido.setDataCriacaoPedido(LocalDate.now());
        pedido.setFornecedor(fornecedor);
        pedido.setPeriodo(periodo);
        pedido.setFuncionario(funcionario);




        if (validateInvoice(pedido.getFornecedor().getCnpj(), pedido.getFuncionario().getUuid(), dadosItem(pedidoDTO.getItemDTOS(),pedido), totalValor(pedidoDTO.getItemDTOS()))){
            this.iRepositoryPedido.save(pedido);
            for (ItemDTO itemDTO : pedidoDTO.getItemDTOS()){
                Item item = new Item();
                itemDTO.setPedido(pedido.getId());

                itemService.save(itemDTO);
                item.setProduto(produtoService.findOptionalById(itemDTO.getProduto().getId()));
                item.setQuantidade(itemDTO.getQuantidade());

                items.add(item);
            }
            }else{
            throw new IllegalArgumentException("conexao com a API falhou");
        }
        return PedidoDTO.of(pedido);
    }

    public PedidoDTO update(PedidoDTO pedidoDTO, Long id) {
        Optional<Pedido> pedidoOptionalExistente = this.iRepositoryPedido.findById(id);

        if (pedidoOptionalExistente.isPresent()) {

            this.validateSalvar(pedidoDTO);

            Pedido pedidoOptional = pedidoOptionalExistente.get();
            LOGGER.info("Atualizando pedido : [{}]", pedidoOptional.getId());
            LOGGER.debug("Pedido inexistente: {}", pedidoDTO);
            LOGGER.debug("Pedido existente: {}", pedidoOptional);
            Fornecedor fornecedor = this.fornecedorService.findByIdFornecedor(pedidoDTO.getFornecedor());
            Periodo periodo = this.periodoService.findByPeriodo(pedidoDTO.getPeriodo());

            pedidoOptional.setCodigoUnico(pedidoDTO.getCodigoUnico());
            pedidoOptional.setStatusPedido(pedidoDTO.getStatusPedido());
            pedidoOptional.setDataCriacaoPedido(LocalDate.now());
            pedidoOptional.setFornecedor(fornecedor);
            pedidoOptional.setPeriodo(periodo);

            this.iRepositoryPedido.save(pedidoOptional);
            return PedidoDTO.of(pedidoOptional);
        }
        throw new IllegalArgumentException(String.format("id da pedido %s não existe"));
    }


    public static boolean validateInvoice(String cnpjFornecedor, String employeeUuid, List<Item> items, Double total) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<InvoiceDTO> entity = new HttpEntity (InvoiceDTO.dados(cnpjFornecedor, employeeUuid, items, total));

        ResponseEntity<InvoiceDTO> responseEntity = restTemplate.exchange("http://10.2.54.25:9999/v2/api-docs", HttpMethod.POST, entity, InvoiceDTO.class);
        if (responseEntity.getStatusCodeValue() == 200 || responseEntity.getStatusCodeValue() == 201) {
            return true;
        }
        throw new IllegalArgumentException("Inválido. Status: [" + responseEntity.getStatusCodeValue() + "]");
    }

    private void validateSalvar(PedidoDTO pedidoDTO) {
        LOGGER.info("Validando pedido");

        if(StringUtils.isEmpty(String.valueOf(pedidoDTO.getFornecedor()))) {
            throw new IllegalArgumentException("Favor informar o fornecedor");
        }
        if(StringUtils.isEmpty(String.valueOf(pedidoDTO.getStatusPedido()))) {
            throw new IllegalArgumentException("Favor informar o status");
        }
    }

    public List<Item> dadosItem(List<ItemDTO> itensDTO, Pedido pedido){
        List<Item> items = new ArrayList<>();
        for (ItemDTO itemDTO : itensDTO ){
            Item item = new Item();
            item.setPedido(pedido);
            item.setProduto(produtoService.findOptionalById(itemDTO.getProduto().getId()));
            item.setQuantidade(itemDTO.getQuantidade());
            items.add(item);
        }
        return items;
    }

    public double totalValor(List<ItemDTO> items){
        double soma = 0;
        for (ItemDTO item : items){
            Produto produto =  produtoService.findOptionalById(item.getProduto().getId());
            soma += (produto.getPreco()* item.getQuantidade());
        }
        return soma;
    }


    public List<Pedido> findByPeriodoVenda(Periodo periodo) {
        List<Pedido> pedidoList = new ArrayList<>();
        try {
            pedidoList = iRepositoryPedido.findByPeriodo(periodo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pedidoList;
    }
    //export
    void escreverPedido (HttpServletResponse response, Long id ) throws Exception{
        String nomeArquivo = "arquivoProduto.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"");

        PrintWriter escritor = response.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(escritor).withSeparator(';').withEscapeChar(
                CSVWriter.DEFAULT_ESCAPE_CHARACTER).
                withLineEnd(CSVWriter.DEFAULT_LINE_END).build();
        String[] tituloCSV = {"produto, quantidade, fornecedor"};
        icsvWriter.writeNext(tituloCSV);

        Periodo periodo;
        periodo  = periodoService.findByPeriodo(id);

        List<Pedido> pedidos;
        pedidos = findByPeriodoVenda(periodo);

        List<Item> items;

        for (Pedido pedido : pedidos){
            items = itemService.findByItemId(pedido);
            for (Item item : items)
                icsvWriter.writeNext(new String[]{item.getProduto().getNome(), String.valueOf(item.getQuantidade()), pedido.getFornecedor().getRazaoSocial()+formatarCnpj(pedido.getFornecedor().getCnpj())});

        }
    }

    public String formatarCnpj(String cnpj) {
        String mask = "";

        mask = (cnpj.substring(0, 2) + "."
                + cnpj.substring(2, 5) + "."
                + cnpj.substring(5, 8) + "/"
                + cnpj.substring(8, 12) + "-"
                + cnpj.substring(12, 14));
        return mask;
    }
}
