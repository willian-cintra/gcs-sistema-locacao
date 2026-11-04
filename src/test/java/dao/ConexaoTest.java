package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import model.Cacamba;
import model.TipoCacamba;

class ConexaoTest extends BancoDeTeste {

    @Test
    void bancoNovoRecebeDadosDeExemplo() {
        assertEquals(1, new ClienteDAO().listar().size());
        assertEquals(2, new TipoCacambaDAO().listar().size());
        assertEquals(3, new CacambaDAO().listar().size());
        assertEquals(1, new CaminhaoDAO().listar().size());
        assertEquals(1, new MotoristaDAO().listar().size());
    }

    @Test
    void dadosDeExemploNaoVoltamAoReabrirOBanco() {
        new CacambaDAO().remover("PAT-003");

        Conexao.inicializarBanco(true);

        assertNull(new CacambaDAO().buscar("PAT-003"));
    }

    @Test
    void chaveEstrangeiraEVerificada() {
        TipoCacamba inexistente = new TipoCacamba();
        inexistente.setCodigo("NAO-EXISTE");
        Cacamba cac = new Cacamba();
        cac.setPatrimonio("PAT-999");
        cac.setSituacao("disponivel");
        cac.setTipoCacamba(inexistente);

        assertThrows(RuntimeException.class, () -> new CacambaDAO().inserir(cac));
    }
}
