package br.com.hbsis.produto;


import br.com.hbsis.linha.ILinhaRepository;
import br.com.hbsis.linha.Linha;
import br.com.hbsis.linha.LinhaDTO;
import br.com.hbsis.linha.LinhaService;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Optional;

@Service
public class ProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);

    private final IProdutoRepository iProdutoRepository;
    private final ILinhaRepository iLinhaRepository;
    private  final LinhaService linhaService;

    public ProdutoService(IProdutoRepository iProdutoRepository, ILinhaRepository iLinhaRepository, LinhaService linhaService) {
        this.iProdutoRepository = iProdutoRepository;
        this.iLinhaRepository = iLinhaRepository;
        this.linhaService = linhaService;
    }

//CADASTRO

    public ProdutoDTO save(ProdutoDTO produtoDTO) {

        Optional<Linha> linhaOptional = this.iLinhaRepository.findById(produtoDTO.getId());

        this.validate(produtoDTO);

        LOGGER.info("Salvando produtos");
        LOGGER.debug("Produto: {}", produtoDTO);

        Produto produto = new Produto();

        String codigoP = codigoValidar(produtoDTO.getCodigoProduto());

        produto.setCodigoProduto(codigoP.toUpperCase());
        produto.setNome(produtoDTO.getNome());

        produto.setPreco(produtoDTO.getPreco());

        LinhaDTO linhaDTO = linhaService.findLinha(produtoDTO.getLinhaCategoria());
        Linha linha = linhaService.converter(linhaDTO);
        produto.setLinhaCategoria(linha);

        produto.setUnidadeCaixa(produtoDTO.getUnidadeCaixa());
        produto.setPesoUnidade(produtoDTO.getPesoUnidade());
        produto.setUnidadeMedida(unidadeMedidaValida(produtoDTO.getUnidadeMedida()));
        produto.setValidade(produtoDTO.getValidade());
        produto = this.iProdutoRepository.save(produto);
        return ProdutoDTO.of(produto);
    }

    public String codigoValidar(String CodigoProduto) {
        String codigoProduto = StringUtils.leftPad(CodigoProduto, 9, "0");

        return codigoProduto;
    }
    public String unidadeMedidaValida(String unidadeMedida){
        unidadeMedida = unidadeMedida.toLowerCase();
        unidadeMedida = unidadeMedida.replaceAll("A-z","");
            if (unidadeMedida.equals("kg")||(unidadeMedida.equals("g")||(unidadeMedida.equals("mg")))){

                return unidadeMedida;
            }else{}
        throw new IllegalArgumentException("As unicas unidades de medida aceitadas são mg, g, kg");
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

        if (produtoOptional.isPresent()) {
            return ProdutoDTO.of(produtoOptional.get());
        }

        throw new IllegalArgumentException(String.format("id %s não existe", id));
    }

//ALTERAR PRODUTOS

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

            LinhaDTO linhaDTO = linhaService.findLinha(produtoDTO.getLinhaCategoria());
            Linha linha = linhaService.converter(linhaDTO);
            produtoExistente.setLinhaCategoria(linha);

            produtoExistente.setUnidadeCaixa(produtoDTO.getUnidadeCaixa());
            produtoExistente.setPesoUnidade(produtoDTO.getPesoUnidade());
            produtoExistente.setUnidadeMedida(produtoDTO.getUnidadeMedida());
            produtoExistente.setValidade(produtoDTO.getValidade());

            produtoExistente = this.iProdutoRepository.save(produtoExistente);

            return produtoDTO.of(produtoExistente);
        }
        throw new IllegalArgumentException(String.format("id %s nao existente", id));
    }

    void escreverProduto(HttpServletResponse reponde) throws Exception {

        String nomeArquivo = "arquivoProduto.csv";
        reponde.setContentType("text/csv");
        reponde.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"");

        PrintWriter escritor = reponde.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(escritor).withSeparator(';').withEscapeChar(
                CSVWriter.DEFAULT_ESCAPE_CHARACTER).
                withLineEnd(CSVWriter.DEFAULT_LINE_END).build();
        String[] tituloCSV = {"id","codigoProduto", "nome", "preco",  "unidadeCaixa", "pesoUnidade","validade", "codigoLinha", "nomeLinha", "codigoCategoria", "nomeCategoria", "cnpj", "razaoSocial" };
        icsvWriter.writeNext(tituloCSV);


        for (Produto linhas : iProdutoRepository.findAll()) {



            icsvWriter.writeNext(new String[]{String.valueOf(linhas.getId()),

                    linhas.getCodigoProduto(),
                    linhas.getNome(),
                    "R$" + linhas.getPreco(),
                    linhas.getUnidadeCaixa() + "Caixas",
                    String.valueOf(linhas.getPesoUnidade()),
                    String.valueOf(linhas.getValidade()),
                    String.valueOf(linhas.getLinhaCategoria().getCodigolinha()),
                    String.valueOf(linhas.getLinhaCategoria().getNome()),
                    String.valueOf(linhas.getLinhaCategoria().getCategoria().getCodigoCategoria()),
                    String.valueOf(linhas.getLinhaCategoria().getCategoria().getNomeCategoria()),
                    String.valueOf(linhas.getLinhaCategoria().getCategoria().getFornecedor().getCnpj()),
                    String.valueOf(linhas.getLinhaCategoria().getCategoria().getFornecedor().getRazaoSocial())});


        }
    }

//    public void lerProduto(MultipartFile importacao) throws Exception {
//        InputStreamReader inserir = new InputStreamReader(importacao.getInputStream());
//
//
//        CSVReader leitor = new CSVReaderBuilder(inserir).withSkipLines(1).build();
//
//        List<String[]> linhaLer = leitor.readAll();
//        List<Linha> resultado = new ArrayList<>();
//
//        for (String[] lin : linhaLer) {
//
//            try {
//
//                String[] dados = lin[0].replaceAll("\"", "").split(";");
//
//                if ( iProdutoRepository.existsBylinhaCategoria(dados [0])){
//                    LOGGER.info("codigo da categoria ja existe...");
//                }else if ( !iLinhaRepository.existsByCodigolinha(dados [0])){
//
//
//
//                    Produto produto = new Produto();
//
//                    produto.setCodigoProduto(dados[0]);
//                    produto.setNome(dados[1]);
//                    produto.getPreco(dados[2]);
//
////                    ProdutoDTO produtoDTO = linhaService.findByCodigoCategoria(dados[2]);
////                    Categoria categoria = categoriaService.converter(categoriaDTO);
////                    produto.setCategoria(categoria);
//
//                    resultado.add(produto);
//                    System.out.println(resultado);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        iLinhaRepository.saveAll(resultado);
//    }

}
