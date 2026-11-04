package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Cliente;

public class ClienteDAO {

    public void inserir(Cliente cli) {
        String sql = "INSERT INTO Cliente (cpfCnpj, nomeRazaoSocial, endereco, cidade, estado, telefone, email) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cli.getCpfCnpj());
            ps.setString(2, cli.getNomeRazaoSocial());
            ps.setString(3, cli.getEndereco());
            ps.setString(4, cli.getCidade());
            ps.setString(5, cli.getEstado());
            ps.setString(6, cli.getTelefone());
            ps.setString(7, cli.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir cliente: " + e.getMessage(), e);
        }
    }

    public void alterar(Cliente cli) {
        String sql = "UPDATE Cliente SET nomeRazaoSocial = ?, endereco = ?, cidade = ?, estado = ?, "
                + "telefone = ?, email = ? WHERE cpfCnpj = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cli.getNomeRazaoSocial());
            ps.setString(2, cli.getEndereco());
            ps.setString(3, cli.getCidade());
            ps.setString(4, cli.getEstado());
            ps.setString(5, cli.getTelefone());
            ps.setString(6, cli.getEmail());
            ps.setString(7, cli.getCpfCnpj());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar cliente: " + e.getMessage(), e);
        }
    }

    public void remover(String cpfCnpj) {
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement("DELETE FROM Cliente WHERE cpfCnpj = ?")) {
            ps.setString(1, cpfCnpj);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover cliente: " + e.getMessage(), e);
        }
    }

    /** Devolve null se o cliente nao existir. */
    public Cliente buscar(String cpfCnpj) {
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Cliente WHERE cpfCnpj = ?")) {
            ps.setString(1, cpfCnpj);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montar(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente: " + e.getMessage(), e);
        }
    }

    public boolean existe(String cpfCnpj) {
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement("SELECT 1 FROM Cliente WHERE cpfCnpj = ?")) {
            ps.setString(1, cpfCnpj);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar cliente: " + e.getMessage(), e);
        }
    }

    public List<Cliente> listar() {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Cliente ORDER BY nomeRazaoSocial");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clientes.add(montar(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes: " + e.getMessage(), e);
        }
        return clientes;
    }

    private Cliente montar(ResultSet rs) throws SQLException {
        Cliente cli = new Cliente();
        cli.setCpfCnpj(rs.getString("cpfCnpj"));
        cli.setNomeRazaoSocial(rs.getString("nomeRazaoSocial"));
        cli.setEndereco(rs.getString("endereco"));
        cli.setCidade(rs.getString("cidade"));
        cli.setEstado(rs.getString("estado"));
        cli.setTelefone(rs.getString("telefone"));
        cli.setEmail(rs.getString("email"));
        return cli;
    }
}
