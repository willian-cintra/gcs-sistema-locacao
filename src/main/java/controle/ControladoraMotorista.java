package controle;

import java.util.List;

import dao.MotoristaDAO;
import model.Motorista;

public class ControladoraMotorista {

    // Categorias de CNH que permitem dirigir caminhao
    public static final List<String> CATEGORIAS_CNH = List.of("C", "D", "E", "AC", "AD", "AE");

    private final MotoristaDAO motoristaDAO = new MotoristaDAO();

    public void inserir(Motorista mot) {
        validar(mot);
        if (motoristaDAO.buscar(mot.getMatricula()) != null) {
            throw new IllegalArgumentException("Já existe motorista com a matrícula " + mot.getMatricula() + ".");
        }
        motoristaDAO.inserir(mot);
    }

    public void alterar(Motorista mot) {
        validar(mot);
        if (motoristaDAO.buscar(mot.getMatricula()) == null) {
            throw new IllegalArgumentException("Motorista " + mot.getMatricula() + " não encontrado.");
        }
        motoristaDAO.alterar(mot);
    }

    public void remover(String matricula) {
        if (motoristaDAO.buscar(matricula) == null) {
            throw new IllegalArgumentException("Motorista " + matricula + " não encontrado.");
        }
        motoristaDAO.remover(matricula);
    }

    public List<Motorista> listar() {
        return motoristaDAO.listar();
    }

    private void validar(Motorista mot) {
        Validacoes.obrigatorio(mot.getMatricula(), "Matrícula");
        Validacoes.obrigatorio(mot.getNome(), "Nome");
        Validacoes.digitos(mot.getCpf(), "CPF deve ter 11 dígitos, sem pontuação.", 11);
        Validacoes.digitos(mot.getCnh(), "CNH deve ter 11 dígitos.", 11);
        Validacoes.opcao(mot.getCategoriaCnh(), CATEGORIAS_CNH,
                "Categoria da CNH não permite dirigir caminhão (use C, D, E, AC, AD ou AE).");
    }
}
