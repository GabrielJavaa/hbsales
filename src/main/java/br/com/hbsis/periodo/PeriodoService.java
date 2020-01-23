package br.com.hbsis.periodo;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PeriodoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodoService.class);

    private final FornecedorService fornecedorService;
    private final IPeriodoRepository iPeriodoRepository;

    public PeriodoService(FornecedorService fornecedorService, IPeriodoRepository iPeriodoRepository) {
        this.fornecedorService = fornecedorService;
        this.iPeriodoRepository = iPeriodoRepository;
    }

    //CADASTRO
    public PeriodoDTO save(PeriodoDTO periodoDTO){

        Fornecedor fornecedor = fornecedorService.findByIdFornecedor(periodoDTO.getFornecedor());
        this.validaPeriodoEntreInicioFim(periodoDTO, fornecedor);
        this.validandoFimDoPeriodo(periodoDTO);
        this.validate(periodoDTO);

            LOGGER.info("Salvando produtos");
            LOGGER.debug("Produto: {}", periodoDTO);

            Periodo periodo = new Periodo();

            periodo.setId(periodoDTO.getId());
            periodo.setDataInicioVendas(periodoDTO.getDataInicioVendas());
            periodo.setDataFimVendas(periodoDTO.getDataFimVendas());
            periodo.setFornecedor(fornecedorService.findOptionalById(periodoDTO.getFornecedor()));
            periodo.setDataRetiradaPedidos(periodoDTO.getDataRetiradaPedidos());
            periodo.setDescricao(periodoDTO.getDescricao());

            periodo = this.iPeriodoRepository.save(periodo);
            return PeriodoDTO.of(periodo);
    }
    //VALIDAÇAO
    public void validate(PeriodoDTO periodoDTO){
        LOGGER.info("Validando produto");

        if (periodoDTO == null){
            throw new IllegalArgumentException("periodo não pode ser nulo");
        }
        if (StringUtils.isEmpty(periodoDTO.getDataInicioVendas().toString().replaceAll("/", "-"))){
            throw new IllegalArgumentException(String.format("Periodo inicial de vendas %s não pode ser nulo"));
        }
        if (StringUtils.isEmpty(periodoDTO.getDataFimVendas().toString().replaceAll("/", "-"))){
            throw new IllegalArgumentException(String.format("Periodo final de vendas %s não pode ser nulo"));
        }
        if (StringUtils.isEmpty(String.valueOf(periodoDTO.getFornecedor()))){
            throw new IllegalArgumentException(String.format("Fornecedor %s não informado"));
        }
        if (StringUtils.isEmpty(periodoDTO.getDataRetiradaPedidos().toString().replaceAll("/", "-"))){
            throw new IllegalArgumentException(String.format("Data de retirada de pedidos %s não pode ser nulo"));
        }
        if (StringUtils.isEmpty(String.valueOf(periodoDTO.getDescricao()))){
            throw new IllegalArgumentException(String.format("Descrição %s não pode ser nulo"));
        }
    }
    //PESQUISA
    public PeriodoDTO findById(Long id){
        Optional<Periodo> periodoOptional = this.iPeriodoRepository.findById(id);

        if (periodoOptional.isPresent()){
            return PeriodoDTO.of(periodoOptional.get());
        }
        throw new IllegalArgumentException(String.format("Periodo %s não existe",id));
    }
    public Periodo findByPeriodo( Long id){
        Optional <Periodo> periodoOptional = this.iPeriodoRepository.findById(id);
        if (periodoOptional.isPresent()) {
            return periodoOptional.get();
        }
        throw new IllegalArgumentException(String.format("id %s nao existe", id));
    }

    //ALTERAR
    public PeriodoDTO update(PeriodoDTO periodoDTO, Long id){
        Optional<Periodo>periodoExistenteOptional = this.iPeriodoRepository.findById(id);

        if (periodoExistenteOptional.isPresent()){
            Periodo periodoExistente = periodoExistenteOptional.get();

            this.validacaoUpdate(periodoExistente);

            LOGGER.info("Atualizando usuário... id: [{}]", periodoExistente.getId());
            LOGGER.debug("Payload: {}", periodoDTO);
            LOGGER.debug("Usuario Existente: {}", periodoExistente);

            periodoExistente.setDataInicioVendas(periodoDTO.getDataInicioVendas());
            periodoExistente.setDataFimVendas(periodoDTO.getDataFimVendas());
            periodoExistente.setDataRetiradaPedidos(periodoDTO.getDataRetiradaPedidos());
            periodoExistente.setDescricao(periodoDTO.getDescricao());

            periodoExistente = this.iPeriodoRepository.save(periodoExistente);

            return PeriodoDTO.of(periodoExistente);
        }
        throw new IllegalArgumentException(String.format("id %s do fornecedor não existe", id));
    }

    public void validacaoUpdate (Periodo periodoExistente){
        LocalDate hoje = LocalDate.now();
        if (periodoExistente.getDataFimVendas().isBefore(hoje)){
            LOGGER.info("O periodo do pedido nao pode ser alterado");
        }
    }
    public void validandoFimDoPeriodo(PeriodoDTO periodoDTO){
        LocalDate hoje = LocalDate.now();
         if (periodoDTO.getDataInicioVendas().isBefore(hoje)){
            LOGGER.info("O pedido nao pode ser criado antes do dia de hoje");
        }else if (periodoDTO.getDataFimVendas().isBefore(hoje)){
             LOGGER.info("O pedido nao pode ser criado");
         }
    }
    public void validaPeriodoEntreInicioFim(PeriodoDTO periodoDTO,Fornecedor fornecedor){

        List<Periodo> periodoExistentes = iPeriodoRepository.findByFornecedor(fornecedor);

        for (Periodo periodo : periodoExistentes) {
            if (periodoDTO.getDataInicioVendas().isBefore(periodo.getDataFimVendas()) && (periodoDTO.getDataFimVendas().isAfter(periodo.getDataFimVendas()))){
                throw new IllegalArgumentException("se o periodo final terminar entre os periodos existentes3");
            }if (periodoDTO.getDataFimVendas().isBefore(periodo.getDataFimVendas())&&(periodoDTO.getDataInicioVendas().isBefore(periodo.getDataInicioVendas()))) {
                throw new IllegalArgumentException("se o periodo inicial ja existe entre os periodos");
            }if (periodoDTO.getDataInicioVendas().isBefore(periodo.getDataInicioVendas()) && (periodoDTO.getDataFimVendas().isAfter(periodo.getDataFimVendas()))){
                throw new IllegalArgumentException("o periodo nao pode ser criando durante um periodo de venda existente");
            }

        }
    }

}
