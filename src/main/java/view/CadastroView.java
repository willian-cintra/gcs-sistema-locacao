package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Base das telas de cadastro: formulario em cima, lista embaixo.
 * Clicar numa linha carrega o registro para alteracao; "Novo" limpa o formulario para inclusao.
 * A subclasse cria os campos e deve chamar montarTela() no fim do seu construtor.
 */
public abstract class CadastroView<T> extends JDialog {

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");
    private static final Map<String, String> ROTULOS_SITUACAO = Map.of(
            "disponivel", "Disponível",
            "locada", "Locada",
            "em uso", "Em uso",
            "manutencao", "Em manutenção");

    private final DefaultTableModel modeloTabela = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int linha, int coluna) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modeloTabela);
    private final JPanel formulario = new JPanel(new GridBagLayout());
    private final List<JComponent> campos = new ArrayList<>();
    private final JLabel lblStatus = new JLabel(" ");

    private final JButton btnNovo = new JButton("Novo");
    private final JButton btnSalvar = new JButton("Salvar");
    private final JButton btnRemover = new JButton("Remover");
    private final JButton btnFechar = new JButton("Fechar");

    private List<T> itens = new ArrayList<>();
    private T selecionado;

    protected CadastroView(Window dono, String titulo) {
        super(dono, titulo, ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    protected abstract void montarFormulario();

    /** Campo da chave primaria; so pode ser editado na inclusao. */
    protected abstract JComponent campoChave();

    protected abstract String[] colunas();

    protected abstract Object[] linha(T item);

    protected abstract List<T> listar();

    protected abstract T lerFormulario();

    protected abstract void preencher(T item);

    protected abstract void inserir(T item);

    protected abstract void alterar(T item);

    protected abstract void remover(T item);

    /** Texto usado na confirmacao de remocao, ex.: "o cliente Fulano". */
    protected abstract String descricao(T item);

    protected void montarTela() {
        montarFormulario();
        modeloTabela.setColumnIdentifiers(colunas());
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selecionarLinha();
            }
        });
        JScrollPane rolagem = new JScrollPane(tabela);
        rolagem.setPreferredSize(new Dimension(640, 200));

        JPanel centro = new JPanel(new BorderLayout(0, 12));
        centro.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        centro.add(formulario, BorderLayout.NORTH);
        centro.add(rolagem, BorderLayout.CENTER);

        add(criarCabecalho(), BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);

        btnNovo.addActionListener(e -> novo());
        btnSalvar.addActionListener(e -> salvar());
        btnRemover.addActionListener(e -> removerSelecionado());
        btnFechar.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(btnSalvar);

        executar(this::recarregar);
        novo();
        pack();
        setLocationRelativeTo(getOwner());
    }

    protected void adicionarCampo(String rotulo, JComponent campo) {
        int linha = campos.size();
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = linha;
        c.insets = new Insets(3, 0, 3, 8);
        c.anchor = GridBagConstraints.LINE_END;
        formulario.add(new JLabel(rotulo), c);

        c.gridx = 1;
        c.insets = new Insets(3, 0, 3, 0);
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        formulario.add(campo, c);
        campos.add(campo);
    }

    protected void limparCampos() {
        for (JComponent campo : campos) {
            if (campo instanceof JTextField txt) {
                txt.setText("");
            } else if (campo instanceof JComboBox<?> combo) {
                combo.setSelectedIndex(combo.getItemCount() > 0 ? 0 : -1);
            }
        }
    }

    private JPanel criarCabecalho() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBorder(BorderFactory.createEmptyBorder(16, 20, 4, 20));
        JLabel titulo = new JLabel(getTitle());
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        topo.add(titulo, BorderLayout.WEST);
        return topo;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xE0, 0xE0, 0xE0)),
                BorderFactory.createEmptyBorder(0, 20, 0, 12)));
        lblStatus.setForeground(new Color(0x2E, 0x7D, 0x32));
        rodape.add(lblStatus, BorderLayout.WEST);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 12));
        botoes.add(btnNovo);
        botoes.add(btnRemover);
        botoes.add(btnSalvar);
        botoes.add(btnFechar);
        rodape.add(botoes, BorderLayout.EAST);
        return rodape;
    }

    private void recarregar() {
        itens = listar();
        modeloTabela.setRowCount(0);
        for (T item : itens) {
            modeloTabela.addRow(linha(item));
        }
    }

    private void selecionarLinha() {
        int i = tabela.getSelectedRow();
        if (i < 0) {
            return;
        }
        selecionado = itens.get(i);
        preencher(selecionado);
        campoChave().setEnabled(false);
        btnRemover.setEnabled(true);
        lblStatus.setText(" ");
    }

    private void novo() {
        tabela.clearSelection();
        selecionado = null;
        limparCampos();
        campoChave().setEnabled(true);
        campoChave().requestFocusInWindow();
        btnRemover.setEnabled(false);
    }

    private void salvar() {
        executar(() -> {
            T item = lerFormulario();
            if (selecionado == null) {
                inserir(item);
            } else {
                alterar(item);
            }
            recarregar();
            novo();
            lblStatus.setText("Registro salvo.");
        });
    }

    private void removerSelecionado() {
        if (selecionado == null) {
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(this, "Remover " + descricao(selecionado) + "?",
                "Confirmar remoção", JOptionPane.YES_NO_OPTION);
        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }
        executar(() -> {
            remover(selecionado);
            recarregar();
            novo();
            lblStatus.setText("Registro removido.");
        });
    }

    /** Executa a acao e mostra em uma mensagem os erros lancados pela controladora. */
    private void executar(Runnable acao) {
        try {
            acao.run();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected static String texto(JTextField campo) {
        return campo.getText().trim();
    }

    /** Devolve null quando o campo esta vazio, para gravar NULL no banco. */
    protected static String opcional(JTextField campo) {
        String valor = texto(campo);
        return valor.isEmpty() ? null : valor;
    }

    /** Aceita virgula ou ponto como separador decimal; campo vazio vale 0. */
    protected static double lerDecimal(JTextField campo, String nome) {
        String valor = texto(campo).replace(',', '.');
        if (valor.isEmpty()) {
            return 0;
        }
        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nome + " deve ser um número.");
        }
    }

    protected static int lerInteiro(JTextField campo, String nome) {
        String valor = texto(campo);
        if (valor.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nome + " deve ser um número inteiro.");
        }
    }

    protected static String decimal(double valor) {
        return String.format(PT_BR, "%.2f", valor);
    }

    protected static String rotuloSituacao(String situacao) {
        return ROTULOS_SITUACAO.getOrDefault(situacao, situacao);
    }

    /** Combo que guarda o valor gravado no banco (ex.: "manutencao") e mostra o rotulo. */
    protected static JComboBox<String> comboSituacao(List<String> situacoes) {
        JComboBox<String> combo = new JComboBox<>(situacoes.toArray(new String[0]));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
                                                          boolean selecionado, boolean foco) {
                return super.getListCellRendererComponent(lista, rotuloSituacao((String) valor), indice,
                        selecionado, foco);
            }
        });
        return combo;
    }
}
