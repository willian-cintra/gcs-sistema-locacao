package dao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Gerencia a conexao JDBC com o banco SQLite.
 */
public class Conexao {

    // Caminho relativo: o arquivo do banco fica na pasta onde a aplicacao e executada.
    public static final String BANCO_PADRAO = "tche_entulhos.db";

    private static String url = "jdbc:sqlite:" + BANCO_PADRAO;

    /** Troca o arquivo de banco usado (ex.: banco de teste). */
    public static void usarBanco(String caminhoArquivo) {
        url = "jdbc:sqlite:" + caminhoArquivo;
    }

    public static Connection getConexao() {
        try {
            Connection con = DriverManager.getConnection(url);
            // O SQLite so verifica chaves estrangeiras quando isso e ligado em cada conexao.
            try (Statement st = con.createStatement()) {
                st.execute("PRAGMA foreign_keys = ON");
            }
            return con;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco: " + e.getMessage(), e);
        }
    }

    public static void fecharConexao(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }

    /**
     * Cria as tabelas (schema.sql). Os dados de exemplo (seed.sql) so sao inseridos quando
     * o banco esta sendo criado, para nao recriar registros removidos pelo usuario.
     */
    public static void inicializarBanco(boolean comDadosDeExemplo) {
        boolean bancoNovo = !existeTabela("Cliente");
        executarScript("/schema.sql");
        if (comDadosDeExemplo && bancoNovo) {
            executarScript("/seed.sql");
        }
    }

    private static boolean existeTabela(String nome) {
        String sql = "SELECT 1 FROM sqlite_master WHERE type = 'table' AND name = ?";
        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar tabelas: " + e.getMessage(), e);
        }
    }

    private static void executarScript(String recurso) {
        try (InputStream in = Conexao.class.getResourceAsStream(recurso)) {
            if (in == null) {
                System.err.println("Script não encontrado no classpath: " + recurso);
                return;
            }
            String sql = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));

            try (Connection con = getConexao(); Statement st = con.createStatement()) {
                for (String comando : sql.split(";")) {
                    if (!comando.trim().isEmpty()) {
                        st.execute(comando);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao executar " + recurso + ": " + e.getMessage(), e);
        }
    }
}
