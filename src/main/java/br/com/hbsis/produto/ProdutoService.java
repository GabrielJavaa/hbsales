package br.com.hbsis.produto;


import br.com.hbsis.categoria.Categoria;
import br.com.hbsis.linha.Linha;
import br.com.hbsis.linha.LinhaService;
import com.google.common.net.HttpHeaders;
import com.microsoft.sqlserver.jdbc.StringUtils;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Optional;

@Service
public class ProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);

    private final IProdutoRepository iProdutoRepository;
    private final LinhaService linhaService;

    public ProdutoService(IProdutoRepository iProdutoRepository, LinhaService linhaService){
        this.iProdutoRepository = iProdutoRepository;
        this.linhaService = linhaService;
    }

    public ProdutoDTO save(ProdutoDTO produtoDTO){

        Optional<Linha> linhaOptional = this.linhaService.findOptionalById(produtoDTO.getId());

        this.validate(produtoDTO);

        LOGGER.info("Salvando produtos");
        LOGGER.debug("Produto: {}", produtoDTO);

        Linha linha = linhaOptional.get();

        Produto produto = new Produto();
        produto.setCodigoProduto(produtoDTO.getCodigoProduto());
        produto.setNome(produtoDTO.getNome());
        produto.setPreco(produtoDTO.getPreco());
        produto.setLinhaCategoria(linha);
        produto.setUnidadeCaixa(produtoDTO.getUnidadeCaixa());
        produto.setPesoUnidade(produtoDTO.getPesoUnidade());
        produto.setValidade(produtoDTO.getValidade());
        produto = this.iProdutoRepository.save(produto);
        return ProdutoDTO.of(produto);
    }

    public void validate(ProdutoDTO produtoDTO){

        LOGGER.info("Validando Produtos");

        if (produtoDTO == null) {
            throw new IllegalArgumentException(" produto não pode ser nulo");
        }
        if (StringUtils.isEmpty(produtoDTO.getCodigoProduto())) {
            throw new IllegalArgumentException("O codigo do produto não pode ser nulo");
        }
        if (StringUtils.isEmpty(produtoDTO.getNome())){
            throw new  IllegalArgumentException("O nome nao pode ser nulo");
        }

    }
    public ProdutoDTO findById(Long id) {
        Optional<Produto> produtoOptional = this.iProdutoRepository.findById(id);

        if (produtoOptional.isPresent()) {
            return ProdutoDTO.of(produtoOptional.get());
        }

        throw new IllegalArgumentException(String.format("id %s não existe", id));
    }

    public ProdutoDTO update(ProdutoDTO produtoDTO, Long id) {
        Optional<Produto> produtoExistenteOptical = this.iProdutoRepository.findById(id);

        if (produtoExistenteOptical.isPresent()) {
            Produto produtoExistente = produtoExistenteOptical.get();


            LOGGER.info("Atualizando produto... id: [{}]", produtoExistente.getId());
            LOGGER.debug("Recebendo: {}", produtoDTO);
            LOGGER.debug("produto existente: {}", produtoExistente);

            produtoExistente.setCodigoProduto(produtoDTO.getCodigoProduto());
            produtoExistente.setNome(produtoDTO.getNome());
            produtoExistente.setPreco(produtoDTO.getPreco());
            produtoExistente.setLinhaCategoria(produtoDTO.getLinhaCategoria());
            produtoExistente.setUnidadeCaixa(produtoDTO.getUnidadeCaixa());
            produtoExistente.setPesoUnidade(produtoDTO.getPesoUnidade());
            produtoExistente.setValidade(produtoDTO.getValidade());

            produtoExistente = this.iProdutoRepository.save(produtoExistente);

            return produtoDTO.of(produtoExistente);
        }
        throw new IllegalArgumentException(String.format("id %s nao existente", id));
     }

    public void delete(Long id) {
        LOGGER.info("Excluindo produto pelo id: [{}]", id);

        this.iProdutoRepository.deleteById(id);
    }

    void escreverLinha(HttpServletResponse reponde) throws Exception {
        String nomeArquivo = "arquivoLinha.csv";
        reponde.setContentType("text/csv");
        reponde.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"");

        PrintWriter escritor = reponde.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(escritor).withSeparator(';').withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER).withLineEnd(CSVWriter.DEFAULT_LINE_END).build();
        String[] tituloCSV = {"id", "codigoProduto", "nome", "preco", "linhaCategoria", "unidadeCaixa", "pesoUnidade", "validade"};
        icsvWriter.writeNext(tituloCSV);

        for (Produto linhas : iProdutoRepository.findAll()) {
            icsvWriter.writeNext(new String[]{String.valueOf(linhas.getId()), linhas.getCodigoProduto(), linhas.getNome(), String.valueOf(linhas.getPreco()), String.valueOf(linhas.getLinhaCategoria()), linhas.getUnidadeCaixa(), String.valueOf(linhas.getPesoUnidade()), String.valueOf(linhas.getValidade())});
        }
    }

}
