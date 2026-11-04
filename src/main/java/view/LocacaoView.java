package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import controle.ControladoraLocacao;
import model.TipoCacamba;

/**
 * Tela de cadastro de locacao. Os botoes liberam as etapas em ordem: verificar cliente,
 * consultar disponibilidade, calcular valor e confirmar. Alterar um campo ja verificado
 * volta para a etapa correspondente.
 */
public class LocacaoView extends JDialog {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat FORMATO_MOEDA = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
    private static final Color COR_OK = new Color(0x2E, 0x7D, 0x32);
    private static final Color COR_ERRO = new Color(0xC6, 0x28, 0x28);

    // Etapas ja concluidas do cadastro
    private static final int INICIO = 0;
    private static final int CLIENTE_OK = 1;
    private static final int DISPONIBILIDADE_OK = 2;
    private static final int VALOR_OK = 3;

    private final ControladoraLocacao controladora = new ControladoraLocacao();
    private int etapa = INICIO;

    private final JTextField txtCpfCnpj = new JTextField(14);
    private final JComboBox<TipoCacamba> cbTipoCacamba = new JComboBox<>();
    private final JSpinner spnQtde = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
    private final JTextField txtDataEntrega = new JTextField(10);
    private final JTextField txtEndEntrega = new JTextField(30);
    private final JTextArea txtObs = new JTextArea(3, 30);
    private final JSpinner spnPeriodo = new JSpinner(new SpinnerNumberModel(1, 1, 365, 1));
    private final JComboBox<String> cbFormaPagamento =
            new JComboBox<>(new String[] {"PIX", "Boleto", "Cartão", "Dinheiro"});

    private final JLabel lblCliente = new JLabel(" ");
    private final JLabel lblDisponibilidade = new JLabel(" ");
    private final JLabel lblValor = new JLabel(" ");

    private final JButton btnVerificarCliente = new JButton("Verificar cliente");
    private final JButton btnConsultarDisponibilidade = new JButton("Consultar disponibilidade");
    private final JButton btnCalcularValor = new JButton("Calcular valor");
    private final JButton btnConfirmar = new JButton("Confirmar");
    private final JButton btnCancelar = new JButton("Cancelar");

    public LocacaoView(Window dono) {
        super(dono, "Nova locação", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        txtCpfCnpj.putClientProperty("JTextField.placeholderText", "somente números");
        for (TipoCacamba tipo : controladora.listarTiposCacamba()) {
            cbTipoCacamba.addItem(tipo);
        }
        txtDataEntrega.setText(LocalDate.now().plusDays(1).format(FORMATO_DATA));
        txtObs.setLineWrap(true);
        txtObs.setWrapStyleWord(true);

        add(criarCabecalho(), BorderLayout.NORTH);
        add(criarFormulario(), BorderLayout.CENTER);
        add(criarBotoes(), BorderLayout.SOUTH);

        configurarAcoes();
        atualizarBotoes();

        pack();
        setResizable(false);
        setLocationRelativeTo(dono);
    }

    private JPanel criarCabecalho() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBorder(BorderFactory.createEmptyBorder(16, 20, 4, 20));
        JLabel titulo = new JLabel("Cadastrar locação");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        topo.add(titulo, BorderLayout.WEST);
        return topo;
    }

    private JPanel criarFormulario() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        int linha = 0;

        adicionarLinha(form, linha++, "CPF/CNPJ do cliente:", linhaComBotao(txtCpfCnpj, btnVerificarCliente));
        adicionarLinha(form, linha++, "", lblCliente);

        adicionarLinha(form, linha++, "Tipo de caçamba:", cbTipoCacamba);
        adicionarLinha(form, linha++, "Quantidade:", linhaComBotao(spnQtde, btnConsultarDisponibilidade));
        adicionarLinha(form, linha++, "", lblDisponibilidade);

        adicionarLinha(form, linha++, "Data de entrega:", txtDataEntrega);
        adicionarLinha(form, linha++, "Endereço de entrega:", txtEndEntrega);
        adicionarLinha(form, linha++, "Período (dias):", linhaComBotao(spnPeriodo, btnCalcularValor));
        adicionarLinha(form, linha++, "", lblValor);

        adicionarLinha(form, linha++, "Forma de pagamento:", cbFormaPagamento);
        adicionarLinha(form, linha, "Observações:", new JScrollPane(txtObs));
        return form;
    }

    private void adicionarLinha(JPanel form, int linha, String rotulo, JComponent campo) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = linha;
        c.insets = new Insets(3, 0, 3, 8);
        c.anchor = GridBagConstraints.LINE_END;
        form.add(new JLabel(rotulo), c);

        c.gridx = 1;
        c.insets = new Insets(3, 0, 3, 0);
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        form.add(campo, c);
    }

    private JPanel linhaComBotao(JComponent campo, JButton botao) {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.add(campo, BorderLayout.CENTER);
        p.add(botao, BorderLayout.EAST);
        return p;
    }

    private JPanel criarBotoes() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 12));
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xE0, 0xE0, 0xE0)));
        rodape.add(btnCancelar);
        rodape.add(btnConfirmar);
        getRootPane().setDefaultButton(btnConfirmar);
        return rodape;
    }

    private void configurarAcoes() {
        btnVerificarCliente.addActionListener(e -> verificarCliente());
        btnConsultarDisponibilidade.addActionListener(e -> consultarDisponibilidade());
        btnCalcularValor.addActionListener(e -> calcularValor());
        btnConfirmar.addActionListener(e -> confirmar());
        btnCancelar.addActionListener(e -> dispose());

        // Alterar um dado ja verificado invalida as etapas que dependem dele
        aoAlterar(txtCpfCnpj, INICIO);
        cbTipoCacamba.addActionListener(e -> voltarPara(CLIENTE_OK));
        spnQtde.addChangeListener(e -> voltarPara(CLIENTE_OK));
        spnPeriodo.addChangeListener(e -> voltarPara(DISPONIBILIDADE_OK));
    }

    private void aoAlterar(JTextField campo, int etapaDestino) {
        campo.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { voltarPara(etapaDestino); }
            @Override public void removeUpdate(DocumentEvent e) { voltarPara(etapaDestino); }
            @Override public void changedUpdate(DocumentEvent e) { voltarPara(etapaDestino); }
        });
    }

    private void voltarPara(int etapaDestino) {
        if (etapa <= etapaDestino) {
            return;
        }
        etapa = etapaDestino;
        if (etapa < CLIENTE_OK) {
            lblCliente.setText(" ");
        }
        if (etapa < DISPONIBILIDADE_OK) {
            lblDisponibilidade.setText(" ");
        }
        if (etapa < VALOR_OK) {
            lblValor.setText(" ");
        }
        atualizarBotoes();
    }

    private void avancarPara(int novaEtapa) {
        etapa = novaEtapa;
        atualizarBotoes();
    }

    private void atualizarBotoes() {
        btnConsultarDisponibilidade.setEnabled(etapa >= CLIENTE_OK);
        btnCalcularValor.setEnabled(etapa >= DISPONIBILIDADE_OK);
        btnConfirmar.setEnabled(etapa >= VALOR_OK);
    }

    private void verificarCliente() {
        executar(() -> {
            if (controladora.existeCliente(cpfCnpj())) {
                mostrar(lblCliente, "Cliente encontrado.", COR_OK);
                avancarPara(CLIENTE_OK);
            } else {
                mostrar(lblCliente, "Cliente não cadastrado.", COR_ERRO);
            }
        });
    }

    private void consultarDisponibilidade() {
        executar(() -> {
            int qtde = qtde();
            int disponiveis = controladora.consultaDisponibilidade(tipoCacamba(), qtde);
            if (disponiveis >= qtde) {
                mostrar(lblDisponibilidade, disponiveis + " disponível(is).", COR_OK);
                avancarPara(DISPONIBILIDADE_OK);
            } else {
                mostrar(lblDisponibilidade, "Apenas " + disponiveis + " disponível(is) para " + qtde + " solicitada(s).",
                        COR_ERRO);
            }
        });
    }

    private void calcularValor() {
        executar(() -> {
            double valor = controladora.calcularValorLocacao(tipoCacamba(), qtde(), periodoDias());
            mostrar(lblValor, "Valor total: " + FORMATO_MOEDA.format(valor), COR_OK);
            avancarPara(VALOR_OK);
        });
    }

    private void confirmar() {
        LocalDate dataEntrega;
        try {
            dataEntrega = LocalDate.parse(txtDataEntrega.getText().trim(), FORMATO_DATA);
        } catch (DateTimeParseException ex) {
            aviso("Data de entrega inválida. Use o formato dd/mm/aaaa.");
            return;
        }
        executar(() -> {
            controladora.cadastrarLocacao(cpfCnpj(), tipoCacamba(), qtde(), dataEntrega,
                    txtEndEntrega.getText().trim(), txtObs.getText().trim(), periodoDias(),
                    (String) cbFormaPagamento.getSelectedItem());
            JOptionPane.showMessageDialog(this, "Locação cadastrada com sucesso.\nOrdem de serviço de entrega gerada.",
                    "Locação cadastrada", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
    }

    /** Executa a acao e mostra em uma mensagem os erros lancados pela controladora. */
    private void executar(Runnable acao) {
        try {
            acao.run();
        } catch (IllegalArgumentException ex) {
            aviso(ex.getMessage());
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    private static void mostrar(JLabel lbl, String texto, Color cor) {
        lbl.setText(texto);
        lbl.setForeground(cor);
    }

    private String cpfCnpj() {
        return txtCpfCnpj.getText().trim();
    }

    private String tipoCacamba() {
        TipoCacamba tipo = (TipoCacamba) cbTipoCacamba.getSelectedItem();
        return tipo != null ? tipo.getCodigo() : null;
    }

    private int qtde() {
        return (Integer) spnQtde.getValue();
    }

    private int periodoDias() {
        return (Integer) spnPeriodo.getValue();
    }
}
