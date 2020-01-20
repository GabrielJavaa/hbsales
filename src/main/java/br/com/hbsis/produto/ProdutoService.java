package br.com.hbsis.produto;


import br.com.hbsis.categoria.Categoria;
import br.com.hbsis.categoria.CategoriaDTO;
import br.com.hbsis.categoria.CategoriaService;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.linha.Linha;
import br.com.hbsis.linha.LinhaDTO;
import br.com.hbsis.linha.LinhaService;
import com.opencsv.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);

    private final IProdutoRepository iProdutoRepository;
    private final FornecedorService fornecedorService;
    private final LinhaService linhaService;
    private final CategoriaService categoriaService;

    public ProdutoService(IProdutoRepository iProdutoRepository, FornecedorService fornecedorService, LinhaService linhaService, CategoriaService categoriaService) {
        this.iProdutoRepository = iProdutoRepository;
        this.fornecedorService = fornecedorService;
        this.linhaService = linhaService;
        this.categoriaService = categoriaService;
    }

//CADASTRO

    public ProdutoDTO save(ProdutoDTO produtoDTO) {


        this.validate(produtoDTO);
        this.validadeUnidadeMedida(produtoDTO.getUnidadeMedida());
        LOGGER.info("Salvando produtos");
        LOGGER.debug("Produto: {}", produtoDTO);

        Produto produto = new Produto();

        String codigoP = codigoValidar(produtoDTO.getCodigoProduto());


        produto.setCodigoProduto(codigoP.toUpperCase());
        produto.setNome(produtoDTO.getNome());

        produto.setPreco(produtoDTO.getPreco());
        produto.setLinhaCategoria(linhaService.findByOptionalId(produtoDTO.getLinhaCategoria()));

        produto.setUnidadeCaixa(produtoDTO.getUnidadeCaixa());
        produto.setPesoUnidade(produtoDTO.getPesoUnidade());
        produto.setUnidadeMedida((produtoDTO.getUnidadeMedida()));
        produto.setValidade(produtoDTO.getValidade());
        produto = this.iProdutoRepository.save(produto);
        return ProdutoDTO.of(produto);
    }

    private String codigoValidar(String CodigoProduto) {
        return StringUtils.leftPad(CodigoProduto, 10, "0");
    }

    public void validadeUnidadeMedida(String unidademedida) {

        switch (unidademedida.toUpperCase()) {
            case "MG":
            case "G":
            case "KG":
                break;
            default:
                throw new IllegalArgumentException("unidade de medida só pode ser mostrada com MG, G ou KG");

        }

    }
//VALIDAÇÕES

    public void validate(ProdutoDTO produtoDTO) {

        LOGGER.info("Validando Produtos");

        if (produtoDTO == null) {
            throw new IllegalArgumentException(" produto não pode ser nulo");
        }
        if (StringUtils.isEmpty(produtoDTO.getCodigoProduto())) {
            throw new IllegalArgumentException("O codigo do produto não pode ser nulo");
        }
        if (StringUtils.isEmpty(produtoDTO.getNome())) {
            throw new IllegalArgumentException("O nome nao pode ser nulo");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getPreco()))) {
            throw new IllegalArgumentException("preco nao pode ser nulo");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getLinhaCategoria()))) {
            throw new IllegalArgumentException("linha de categoria nao pode ser nulo");
        }
        if (StringUtils.isEmpty(produtoDTO.getUnidadeCaixa())) {
            throw new IllegalArgumentException("unidade de caixa nao pode ser nulo");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getPesoUnidade()))) {
            throw new IllegalArgumentException("peso nao pode ser nulo");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getUnidadeMedida()))) {
            throw new IllegalArgumentException("unidade de medida nao pode ser nulo");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getValidade()))) {
            throw new IllegalArgumentException("Validade nao pode ser nulo");
        }

    }

//BUSCAR PRODUTOS

    public ProdutoDTO findById(Long id) {
        Optional<Produto> produtoOptional = this.iProdutoRepository.findById(id);

        if (produtoOptional.isPresent())    {
            return ProdutoDTO.of(produtoOptional.get());
        }
        throw new IllegalArgumentException(String.format("id %s não existe", id));
    }

    public Produto findOptionalById(Long id){
        Optional<Produto> produtoOptional = this.iProdutoRepository.findById(id);
        if (produtoOptional.isPresent()){
            return produtoOptional.get();
        }
        throw new IllegalArgumentException(String.format("id %s nao existe", id));
    }

//ALTERAR PRODUTOS

    public ProdutoDTO update(ProdutoDTO produtoDTO, Long id) {

        Optional<Produto> produtoExistenteOptical = this.iProdutoRepository.findById(id);
        Optional<Linha> linhaOptional = Optional.ofNullable(this.linhaService.findByOptionalId(produtoDTO.getLinhaCategoria()));

        if (produtoExistenteOptical.isPresent() && linhaOptional.isPresent()) {

            Produto produtoExistente = produtoExistenteOptical.get();

            LOGGER.info("Atualizando produto... id: [{}]", produtoDTO.getId());
            LOGGER.debug("Recebendo: {}", produtoDTO);
            LOGGER.debug("produto existente: {}", produtoDTO);

            produtoExistente.setCodigoProduto(produtoDTO.getCodigoProduto());
            produtoExistente.setNome(produtoDTO.getNome());
            produtoExistente.setPreco(produtoDTO.getPreco());

            produtoExistente.setLinhaCategoria(linhaOptional.get());

            produtoExistente.setUnidadeCaixa(produtoDTO.getUnidadeCaixa());
            produtoExistente.setPesoUnidade(produtoDTO.getPesoUnidade());
            produtoExistente.setUnidadeMedida(produtoDTO.getUnidadeMedida());
            produtoExistente.setValidade(produtoDTO.getValidade());

            produtoExistente = this.iProdutoRepository.save(produtoExistente);

            return produtoDTO.of(produtoExistente);
        }
        throw new IllegalArgumentException(String.format("id %s nao existente", id));
    }

    public ProdutoDTO updateProdutoParaImportar(ProdutoDTO produtoDTO, String codigoProduto){

        Optional<Produto> produtoExistenteOptical = Optional.ofNullable(this.iProdutoRepository.findByCodigoProduto(codigoProduto));
        Optional<Linha> linhaOptional = Optional.ofNullable(this.linhaService.findByOptionalId(produtoDTO.getLinhaCategoria()));

        if (produtoExistenteOptical.isPresent() && linhaOptional.isPresent()) {

            Produto produtoExistente = produtoExistenteOptical.get();

            LOGGER.info("Atualizando produto... id: [{}]", produtoDTO.getId());
            LOGGER.debug("Recebendo: {}", produtoDTO);
            LOGGER.debug("produto existente: {}", produtoDTO);

            produtoExistente.setCodigoProduto(produtoDTO.getCodigoProduto());
            produtoExistente.setNome(produtoDTO.getNome());
            produtoExistente.setPreco(produtoDTO.getPreco());

            produtoExistente.setLinhaCategoria(linhaOptional.get());

            produtoExistente.setUnidadeCaixa(produtoDTO.getUnidadeCaixa());
            produtoExistente.setPesoUnidade(produtoDTO.getPesoUnidade());
            produtoExistente.setUnidadeMedida(produtoDTO.getUnidadeMedida());
            produtoExistente.setValidade(produtoDTO.getValidade());

            produtoExistente = this.iProdutoRepository.save(produtoExistente);

            return ProdutoDTO.of(produtoExistente) ;
        }
        throw new IllegalArgumentException(String.format("id %s nao existente", codigoProduto));
    }

//EXPORT
    void escreverProduto(HttpServletResponse reponde) throws Exception {

        String nomeArquivo = "arquivoProduto.csv";
        reponde.setContentType("text/csv");
        reponde.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"");

        PrintWriter escritor = reponde.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(escritor).withSeparator(';').withEscapeChar(
                CSVWriter.DEFAULT_ESCAPE_CHARACTER).
                withLineEnd(CSVWriter.DEFAULT_LINE_END).build();
        String[] tituloCSV = {"id", "codigoProduto", "nome", "preco", "unidadeCaixa", "pesoUnidade", "validade", "codigoLinha", "nomeLinha", "codigoCategoria", "nomeCategoria", "cnpj", "razaoSocial"};
        icsvWriter.writeNext(tituloCSV);


        for (Produto linhas : iProdutoRepository.findAll()) {


            icsvWriter.writeNext(new String[]{String.valueOf(linhas.getId()),


                    linhas.getCodigoProduto(),
                    linhas.getNome(),
                    "R$" + linhas.getPreco(),
                    linhas.getUnidadeCaixa() + "Caixas",
                    linhas.getPesoUnidade() + linhas.getUnidadeMedida(),
                    String.valueOf(linhas.getValidade()),
                    String.valueOf(linhas.getLinhaCategoria().getCodigolinha()),
                    String.valueOf(linhas.getLinhaCategoria().getNome()),
                    String.valueOf(linhas.getLinhaCategoria().getCategoria().getCodigoCategoria()),
                    String.valueOf(linhas.getLinhaCategoria().getCategoria().getNomeCategoria()),
                    formatarCnpj(linhas.getLinhaCategoria().getCategoria().getFornecedor().getCnpj()),
                    String.valueOf(linhas.getLinhaCategoria().getCategoria().getFornecedor().getRazaoSocial())});

        }
    }

//IMPORT
    public void lerProduto(MultipartFile importacao) throws Exception {
        InputStreamReader inserir = new InputStreamReader(importacao.getInputStream());

        CSVReader leitor = new CSVReaderBuilder(inserir).withSkipLines(1).build();

        List<String[]> linhaLer = leitor.readAll();
        List<Produto> resultado = new ArrayList<>();

        for (String[] lin : linhaLer) {

            try {

                String[] dados = lin[0].replaceAll("\"", "").split(";");

                if (!linhaService.existsByCodigolinha(dados[7])) {
                    throw new IllegalArgumentException(String.format("o codigo da linha %s nao existe", dados[7]));
                }

                if (iProdutoRepository.existsByCodigoProduto(dados[1])) {
                    LOGGER.info("codigo do produto ja existe...");

                } else {

                    Produto produto = new Produto();

                    double precoValidado = Double.parseDouble(dados[3].replace(",", ".").replace("R$", ""));
                    float pesoValidado = Float.parseFloat(dados[5].replaceAll("[a-zA-Z]", ""));

                    produto.setCodigoProduto(dados[1]);
                    produto.setNome(dados[2]);
                    produto.setPreco(precoValidado);
                    produto.setUnidadeCaixa(dados[4]);
                    produto.setPesoUnidade(pesoValidado);
                    produto.setUnidadeMedida(dados[5].replaceAll("[0-9.]", ""));
                    produto.setValidade(LocalDate.parse(dados[6]));
                    produto.setLinhaCategoria(linhaService.findByCodigoLinha(dados[7]));

                    resultado.add(produto);
                    System.out.println(resultado);

                    iProdutoRepository.saveAll(resultado);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void importFornecedor(MultipartFile importacao, Long id) throws IOException {

        InputStreamReader inserir = new InputStreamReader(importacao.getInputStream());

        CSVReader leitor = new CSVReaderBuilder(inserir).withSkipLines(1).build();

        List<String[]> importLinha = leitor.readAll();
        List<Produto> result = new ArrayList<>();

        for (String[] lin : importLinha) {

            try {

                String[] dados = lin[0].replaceAll("\"", "").split(";");

                if (fornecedorService.existById(id) && fornecedorService.findByFornecedorCnpj(dados[11].replaceAll("[^0-9]", "")).getId().equals(id)) {

                    if (!categoriaService.existsByCodigoCategoria(dados[9])) {
                        Categoria categoria = new Categoria();

                        categoria.setNomeCategoria(dados[10]);
                        categoria.setCodigoCategoria(dados[9]);
                        categoria.setFornecedor(fornecedorService.findByFornecedorCnpj(dados[11].replaceAll("[^0-9]", "")));

                        categoriaService.saveParaImporte(CategoriaDTO.of(categoria));

                    }
                    if (categoriaService.existsByCodigoCategoria(dados[9])) {
                        Categoria categoria = this.categoriaService.findByCodigoCategoria(dados[9]);

                        Optional<Categoria> categoriaOptional = Optional.ofNullable(this.categoriaService.findByCodigoCategoria(dados[9]));
                        if (categoriaOptional.isPresent()) {
                            Categoria categoria1 = categoriaOptional.get();

                            categoria1.setNomeCategoria(categoria.getNomeCategoria());
                            categoria1.setFornecedor(fornecedorService.findOptionalById(categoria.getFornecedor().getId()));
                            categoria1.setCodigoCategoria(categoria.getCodigoCategoria());

                            categoriaService.updateCategoriaParaImportar(CategoriaDTO.of(categoria1), dados[9]);
                        }
                    }
                    if (!linhaService.existsByCodigolinha(dados[7])) {

                        Linha linha = new Linha();

                        linha.setCodigolinha(dados[7]);
                        linha.setNome(dados[8]);
                        linha.setCategoria(categoriaService.findByCodigoCategoria(dados[9]));
                        linhaService.saveParaImportar(LinhaDTO.of(linha));
                    }

                    if (linhaService.existsByCodigolinha(dados[7])) {

                        Linha linhaExisteteOptional = this.linhaService.findByCodigoLinha(dados[7]);

                        Categoria categoria = linhaExisteteOptional.getCategoria();

                        linhaExisteteOptional.setNome(linhaExisteteOptional.getNome());
                        linhaExisteteOptional.setCategoria(categoria);
                        linhaExisteteOptional.setCodigolinha(linhaExisteteOptional.getCodigolinha());

                        linhaService.updateLinhaParaImportar(LinhaDTO.of(linhaExisteteOptional), dados[7]);

                    }
                    if (!iProdutoRepository.existsByCodigoProduto(dados[1])) {
                        LOGGER.info("salvando produto novo");

                        String data = dados[6];
                        int dia = Integer.parseInt(data.substring(0,2));
                        int mes = Integer.parseInt(data.substring(3,5));
                        int ano = Integer.parseInt(data.substring(6,10));
                        LocalDate data1 = LocalDate.of(ano, mes, dia);

                        Produto produto = new Produto();

                        double precoValidado = Double.parseDouble(dados[3].replace(",", ".").replace("R$", ""));
                        float pesoValidado = Float.parseFloat(dados[5].replaceAll("[a-zA-Z]", ""));

                        produto.setCodigoProduto(dados[1]);
                        produto.setNome(dados[2]);
                        produto.setPreco(precoValidado);
                        produto.setUnidadeCaixa(dados[4]);
                        produto.setPesoUnidade(pesoValidado);
                        produto.setUnidadeMedida(dados[5].replaceAll("[0-9.]", ""));
                        produto.setValidade(data1);
                        produto.setLinhaCategoria(linhaService.findByCodigoLinha(dados[7]));

                        result.add(produto);
                        System.out.println(result);

                        iProdutoRepository.saveAll(result);
                    }
                    if (iProdutoRepository.existsByCodigoProduto(dados[1])) {
                        LOGGER.info("Alterando produto");


                        Optional<Produto> produtoOptional = Optional.ofNullable(this.iProdutoRepository.findByCodigoProduto(dados[1]));
                        if (produtoOptional.isPresent()) {
                            Produto produto1 = produtoOptional.get();

                            String data = dados[6];
                            int dia = Integer.parseInt(data.substring(0,2));
                            int mes = Integer.parseInt(data.substring(3,5));
                            int ano = Integer.parseInt(data.substring(6,10));
                            LocalDate data1 = LocalDate.of(ano, mes, dia);

                            double precoValidado = Double.parseDouble(dados[3].replace(",", ".").replace("R$", ""));
                            float pesoValidado = Float.parseFloat(dados[5].replaceAll("[a-zA-Z]", ""));

                            produto1.setCodigoProduto(dados[1]);
                            produto1.setNome(dados[2]);
                            produto1.setPreco(precoValidado);
                            produto1.setUnidadeCaixa(dados[4]);
                            produto1.setPesoUnidade(pesoValidado);
                            produto1.setUnidadeMedida(dados[5].replaceAll("[0-9.]", ""));
                            produto1.setValidade(data1);
                            produto1.setLinhaCategoria(linhaService.findByCodigoLinha(dados[7]));

                            updateProdutoParaImportar(ProdutoDTO.of(produto1),dados[1]);

                        }

                    }
                }
                } catch(IllegalArgumentException e){
                    e.printStackTrace();
                }
            }
        }

    public String formatarCnpj (String cnpj){
        String mask = "";

        mask = (cnpj.substring(0, 2) + "."
                + cnpj.substring(2, 5) + "."
                + cnpj.substring(5, 8) + "/"
                + cnpj.substring(8, 12) + "-"
                + cnpj.substring(12, 14));
        return mask;
    }

}
