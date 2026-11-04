package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Tela inicial do sistema, com o menu e os atalhos para as funcionalidades.
 */
public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("Tchê Entulhos - Locação de Caçambas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 440);
        setLocationRelativeTo(null);

        setJMenuBar(criarBarraMenu());
        add(criarCabecalho(), BorderLayout.NORTH);
        add(criarPainelCartoes(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);
    }

    private JMenuBar criarBarraMenu() {
        JMenuBar barra = new JMenuBar();

        JMenu cadastros = new JMenu("Cadastros");
        cadastros.add(item("Nova locação", this::abrirNovaLocacao));
        cadastros.addSeparator();
        cadastros.add(item("Clientes", () -> new ClienteView(this).setVisible(true)));
        cadastros.add(item("Tipos de caçamba", () -> new TipoCacambaView(this).setVisible(true)));
        cadastros.add(item("Caçambas", () -> new CacambaView(this).setVisible(true)));
        cadastros.add(item("Caminhões", () -> new CaminhaoView(this).setVisible(true)));
        cadastros.add(item("Motoristas", () -> new MotoristaView(this).setVisible(true)));

        JMenu relatorios = new JMenu("Relatórios");
        relatorios.add(item("Relatórios", () -> mostrarEmDesenvolvimento("Relatórios")));

        JMenu ajuda = new JMenu("Ajuda");
        JMenuItem sobre = new JMenuItem("Sobre");
        sobre.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Tchê Entulhos\nSistema de locação de caçambas",
                "Sobre", JOptionPane.INFORMATION_MESSAGE));
        ajuda.add(sobre);

        barra.add(cadastros);
        barra.add(relatorios);
        barra.add(ajuda);
        return barra;
    }

    private JMenuItem item(String nome, Runnable acao) {
        JMenuItem item = new JMenuItem(nome);
        item.addActionListener(e -> acao.run());
        return item;
    }

    private JPanel criarCabecalho() {
        JPanel topo = new JPanel();
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        topo.setBorder(BorderFactory.createEmptyBorder(20, 24, 8, 24));

        JLabel titulo = new JLabel("Bem-vindo ao sistema");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 20f));
        JLabel sub = new JLabel("Selecione uma ação para começar.");
        sub.setForeground(new Color(0x6B, 0x6B, 0x6B));

        topo.add(titulo);
        topo.add(sub);
        return topo;
    }

    private JPanel criarPainelCartoes() {
        JPanel grade = new JPanel(new GridLayout(0, 4, 12, 12));
        grade.setBorder(BorderFactory.createEmptyBorder(8, 24, 16, 24));

        grade.add(criarCartao("Nova locação", "Registrar uma locação", true, this::abrirNovaLocacao));
        grade.add(criarCartao("Clientes", "Cadastro de clientes", false, () -> new ClienteView(this).setVisible(true)));
        grade.add(criarCartao("Tipos de caçamba", "Modelos e preços", false, () -> new TipoCacambaView(this).setVisible(true)));
        grade.add(criarCartao("Caçambas", "Controle do patrimônio", false, () -> new CacambaView(this).setVisible(true)));
        grade.add(criarCartao("Caminhões", "Frota de entrega", false, () -> new CaminhaoView(this).setVisible(true)));
        grade.add(criarCartao("Motoristas", "Cadastro de motoristas", false, () -> new MotoristaView(this).setVisible(true)));
        grade.add(criarCartao("Relatórios", "Locações e faturamento", false, () -> mostrarEmDesenvolvimento("Relatórios")));
        return grade;
    }

    private JPanel criarCartao(String titulo, String legenda, boolean destaque, Runnable acao) {
        JPanel cartao = new JPanel();
        cartao.setLayout(new BoxLayout(cartao, BoxLayout.Y_AXIS));
        Color borda = destaque ? new Color(0x37, 0x8A, 0xDD) : new Color(0xD6, 0xD6, 0xD6);
        cartao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borda, destaque ? 2 : 1, true),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        cartao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 15f));
        JLabel lblLegenda = new JLabel(legenda);
        lblLegenda.setForeground(new Color(0x8A, 0x8A, 0x8A));
        lblLegenda.setFont(lblLegenda.getFont().deriveFont(12f));

        cartao.add(lblTitulo);
        cartao.add(javax.swing.Box.createVerticalStrut(6));
        cartao.add(lblLegenda);

        cartao.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { acao.run(); }
        });
        return cartao;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 6));
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xE0, 0xE0, 0xE0)));
        JLabel status = new JLabel("Conectado: tche_entulhos.db   ·   Funcionário");
        status.setForeground(new Color(0x8A, 0x8A, 0x8A));
        rodape.add(status);
        return rodape;
    }

    private void abrirNovaLocacao() {
        new LocacaoView(this).setVisible(true);
    }

    private void mostrarEmDesenvolvimento(String nome) {
        JOptionPane.showMessageDialog(this,
                "\"" + nome + "\" será implementado.",
                "Em desenvolvimento", JOptionPane.INFORMATION_MESSAGE);
    }
}
