package dao;

import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

/**
 * Base dos testes que usam banco: cada teste roda num arquivo SQLite novo (schema + seed),
 * sem tocar no tche_entulhos.db da aplicacao.
 *
 * Seed: cliente 12345678901; TC-4M (R$ 35/dia, PAT-001 e PAT-002 disponiveis);
 * TC-5M (R$ 42/dia, PAT-003 disponivel); caminhao ABC1D23; motorista M001.
 */
public abstract class BancoDeTeste {

    @TempDir
    protected Path pastaTemporaria;

    @BeforeEach
    void criarBanco() {
        Conexao.usarBanco(pastaTemporaria.resolve("teste.db").toString());
        Conexao.inicializarBanco(true);
    }

    @AfterEach
    void restaurarBanco() {
        Conexao.usarBanco(Conexao.BANCO_PADRAO);
    }
}
