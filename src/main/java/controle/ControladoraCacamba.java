package controle;

import java.util.List;

import dao.CacambaDAO;
import dao.TipoCacambaDAO;
import model.Cacamba;
import model.TipoCacamba;

public class ControladoraCacamba {

    public static final List<String> SITUACOES = List.of("disponivel", "locada", "manutencao");

    private final CacambaDAO cacambaDAO = new CacambaDAO();
    private final TipoCacambaDAO tipoCacambaDAO = new TipoCacambaDAO();

    public void inserir(Cacamba cac) {
        validar(cac);
        if (cacambaDAO.buscar(cac.getPatrimonio()) != null) {
            throw new IllegalArgumentException("Já existe caçamba com o patrimônio " + cac.getPatrimonio() + ".");
        }
        cacambaDAO.inserir(cac);
    }

    public void alterar(Cacamba cac) {
        validar(cac);
        exigirExistente(cac.getPatrimonio());
        cacambaDAO.alterar(cac);
    }

    public void remover(String patrimonio) {
        Cacamba cac = exigirExistente(patrimonio);
        if ("locada".equals(cac.getSituacao())) {
            throw new IllegalArgumentException("Caçamba locada não pode ser removida.");
        }
        cacambaDAO.remover(patrimonio);
    }

    public List<Cacamba> listar() {
        return cacambaDAO.listar();
    }

    public List<TipoCacamba> listarTipos() {
        return tipoCacambaDAO.listar();
    }

    private void validar(Cacamba cac) {
        Validacoes.obrigatorio(cac.getPatrimonio(), "Patrimônio");
        Validacoes.positivo(cac.getCapacidade(), "Capacidade");
        Validacoes.opcao(cac.getSituacao(), SITUACOES, "Selecione a situação da caçamba.");
        if (cac.getTipoCacamba() == null || tipoCacambaDAO.buscar(cac.getTipoCacamba().getCodigo()) == null) {
            throw new IllegalArgumentException("Selecione um tipo de caçamba cadastrado.");
        }
    }

    private Cacamba exigirExistente(String patrimonio) {
        Cacamba cac = cacambaDAO.buscar(patrimonio);
        if (cac == null) {
            throw new IllegalArgumentException("Caçamba " + patrimonio + " não encontrada.");
        }
        return cac;
    }
}
