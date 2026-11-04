import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import dao.Conexao;
import view.MenuPrincipal;

public class Main {

    public static void main(String[] args) {
        // O tema precisa ser aplicado antes de criar qualquer componente Swing.
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Nao foi possivel aplicar o FlatLaf: " + e.getMessage());
        }

        Conexao.inicializarBanco(true);

        // Abre a interface na thread de eventos do Swing.
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
