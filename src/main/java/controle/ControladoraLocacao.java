package controle;

import java.time.LocalDate;
import java.util.List;

import dao.CacambaDAO;
import dao.ClienteDAO;
import dao.LocacaoDAO;
import dao.OrdemDeServicoDAO;
import dao.TipoCacambaDAO;
import model.Cliente;
import model.Locacao;
import model.OrdemDeServico;
import model.TipoCacamba;

/**
 * Dados invalidos geram IllegalArgumentException com a mensagem para o usuario.
 */
public class ControladoraLocacao {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final TipoCacambaDAO tipoCacambaDAO = new TipoCacambaDAO();
    private final CacambaDAO cacambaDAO = new CacambaDAO();
    private final LocacaoDAO locacaoDAO = new LocacaoDAO();
    private final OrdemDeServicoDAO ordemDeServicoDAO = new OrdemDeServicoDAO();

    public boolean existeCliente(String cpfCnpj) {
        return clienteDAO.existe(cpfCnpj);
    }

    public List<TipoCacamba> listarTiposCacamba() {
        return tipoCacambaDAO.listar();
    }

    /** Retorna quantas cacambas do tipo informado estao disponiveis. */
    public int consultaDisponibilidade(String tipoCacamba, int qtde) {
        validarQtde(qtde);
        buscarTipo(tipoCacamba);
        return cacambaDAO.contarDisponiveisPorTipo(tipoCacamba);
    }

    /** Calcula o valor total: preco diario x quantidade x dias. */
    public double calcularValorLocacao(String tipoCacamba, int qtde, int periodoDias) {
        validarQtde(qtde);
        validarPeriodo(periodoDias);
        return buscarTipo(tipoCacamba).getPrecoDiario() * qtde * periodoDias;
    }

    /** Cadastra a locacao e gera a ordem de servico de entrega. */
    public void cadastrarLocacao(String cpfCnpj, String tipoCacamba, int qtdeSolicitada, LocalDate dataEntrega,
                                 String endEntrega, String obs, int periodoDias, String formaPagamento) {
        Cliente cli = clienteDAO.buscar(cpfCnpj);
        if (cli == null) {
            throw new IllegalArgumentException("Cliente deve existir (CPF/CNPJ " + cpfCnpj + " não cadastrado).");
        }
        TipoCacamba tipo = buscarTipo(tipoCacamba);
        validarQtde(qtdeSolicitada);
        validarPeriodo(periodoDias);
        if (dataEntrega == null || dataEntrega.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data de entrega deve ser hoje ou uma data futura.");
        }
        Validacoes.obrigatorio(endEntrega, "Endereço de entrega");
        if (cacambaDAO.contarDisponiveisPorTipo(tipoCacamba) < qtdeSolicitada) {
            throw new IllegalArgumentException("Não há caçambas disponíveis suficientes do tipo " + tipoCacamba + ".");
        }

        Locacao loc = new Locacao();
        loc.setDataSolicitacao(LocalDate.now());
        loc.setDataEntrega(dataEntrega);
        loc.setEnderecoEntrega(endEntrega);
        loc.setPeriodoDias(periodoDias);
        loc.setQtdeSolicitada(qtdeSolicitada);
        loc.setObservacoes(obs);
        loc.setFormaPagamento(formaPagamento);
        loc.setValorDiariaContratado(tipo.getPrecoDiario());
        loc.setValorTotal(calcularValorLocacao(tipoCacamba, qtdeSolicitada, periodoDias));
        loc.setSituacao("ativa");
        loc.setCliente(cli);
        loc.setTipoCacamba(tipo);
        locacaoDAO.inserir(loc);

        OrdemDeServico os = new OrdemDeServico();
        os.setTipoOS("Entrega");
        os.setDataPrevista(dataEntrega);
        os.setDataHoraAgendada(dataEntrega.atStartOfDay());
        os.setSituacao("agendada");
        os.setLocacao(loc);
        ordemDeServicoDAO.inserir(os);
    }

    private TipoCacamba buscarTipo(String codigo) {
        Validacoes.obrigatorio(codigo, "Tipo de caçamba");
        TipoCacamba tipo = tipoCacambaDAO.buscar(codigo);
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de caçamba " + codigo + " não encontrado.");
        }
        return tipo;
    }

    private static void validarQtde(int qtde) {
        if (qtde <= 0) {
            throw new IllegalArgumentException("Quantidade solicitada deve ser maior que zero.");
        }
    }

    private static void validarPeriodo(int periodoDias) {
        if (periodoDias <= 0) {
            throw new IllegalArgumentException("Período deve ser de pelo menos 1 dia.");
        }
    }
}
