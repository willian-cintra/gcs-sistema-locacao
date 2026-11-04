package controle;

import java.util.List;

import dao.CacambaDAO;
import dao.LocacaoDAO;
import dao.TipoCacambaDAO;
import model.TipoCacamba;

public class ControladoraTipoCacamba {

    private final TipoCacambaDAO tipoCacambaDAO = new TipoCacambaDAO();
    private final CacambaDAO cacambaDAO = new CacambaDAO();
    private final LocacaoDAO locacaoDAO = new LocacaoDAO();

    public void inserir(TipoCacamba tipo) {
        validar(tipo);
        if (tipoCacambaDAO.buscar(tipo.getCodigo()) != null) {
            throw new IllegalArgumentException("Já existe tipo de caçamba com o código " + tipo.getCodigo() + ".");
        }
        tipoCacambaDAO.inserir(tipo);
    }

    public void alterar(TipoCacamba tipo) {
        validar(tipo);
        exigirExistente(tipo.getCodigo());
        tipoCacambaDAO.alterar(tipo);
    }

    public void remover(String codigo) {
        exigirExistente(codigo);
        if (cacambaDAO.existePorTipo(codigo)) {
            throw new IllegalArgumentException("Há caçambas cadastradas deste tipo; ele não pode ser removido.");
        }
        if (locacaoDAO.existePorTipo(codigo)) {
            throw new IllegalArgumentException("Há locações deste tipo; ele não pode ser removido.");
        }
        tipoCacambaDAO.remover(codigo);
    }

    public List<TipoCacamba> listar() {
        return tipoCacambaDAO.listar();
    }

    private void validar(TipoCacamba tipo) {
        Validacoes.obrigatorio(tipo.getCodigo(), "Código");
        Validacoes.obrigatorio(tipo.getDescricao(), "Descrição");
        Validacoes.positivo(tipo.getCapacidadeVolumetrica(), "Capacidade volumétrica");
        Validacoes.positivo(tipo.getPrecoDiario(), "Preço diário");
    }

    private void exigirExistente(String codigo) {
        if (tipoCacambaDAO.buscar(codigo) == null) {
            throw new IllegalArgumentException("Tipo de caçamba " + codigo + " não encontrado.");
        }
    }
}
