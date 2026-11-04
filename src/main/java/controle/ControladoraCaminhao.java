package controle;

import java.time.Year;
import java.util.List;

import dao.CaminhaoDAO;
import model.Caminhao;

public class ControladoraCaminhao {

    public static final List<String> SITUACOES = List.of("disponivel", "em uso", "manutencao");

    private final CaminhaoDAO caminhaoDAO = new CaminhaoDAO();

    public void inserir(Caminhao cam) {
        validar(cam);
        if (caminhaoDAO.buscar(cam.getPlaca()) != null) {
            throw new IllegalArgumentException("Já existe caminhão com a placa " + cam.getPlaca() + ".");
        }
        caminhaoDAO.inserir(cam);
    }

    public void alterar(Caminhao cam) {
        validar(cam);
        if (caminhaoDAO.buscar(cam.getPlaca()) == null) {
            throw new IllegalArgumentException("Caminhão " + cam.getPlaca() + " não encontrado.");
        }
        caminhaoDAO.alterar(cam);
    }

    public void remover(String placa) {
        if (caminhaoDAO.buscar(placa) == null) {
            throw new IllegalArgumentException("Caminhão " + placa + " não encontrado.");
        }
        caminhaoDAO.remover(placa);
    }

    public List<Caminhao> listar() {
        return caminhaoDAO.listar();
    }

    private void validar(Caminhao cam) {
        // Aceita o padrao antigo (ABC1234) e o Mercosul (ABC1D23)
        if (cam.getPlaca() == null || !cam.getPlaca().matches("[A-Z]{3}\\d[A-Z0-9]\\d{2}")) {
            throw new IllegalArgumentException("Placa inválida. Use 7 caracteres sem hífen (ex.: ABC1D23).");
        }
        Validacoes.obrigatorio(cam.getModelo(), "Modelo");
        int anoMaximo = Year.now().getValue() + 1;
        if (cam.getAnoFabricacao() < 1950 || cam.getAnoFabricacao() > anoMaximo) {
            throw new IllegalArgumentException("Ano de fabricação deve estar entre 1950 e " + anoMaximo + ".");
        }
        Validacoes.positivo(cam.getCapacidadeCarga(), "Capacidade de carga");
        Validacoes.opcao(cam.getSituacao(), SITUACOES, "Selecione a situação do caminhão.");
    }
}
