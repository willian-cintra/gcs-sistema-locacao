-- Tabelas do sistema (SQLite)

CREATE TABLE IF NOT EXISTS Cliente (
    cpfCnpj          VARCHAR(14)  PRIMARY KEY,
    nomeRazaoSocial  VARCHAR(100) NOT NULL,
    endereco         VARCHAR(100),
    cidade           VARCHAR(50),
    estado           CHAR(2),
    telefone         VARCHAR(15),
    email            VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS TipoCacamba (
    codigo                 VARCHAR(20)   PRIMARY KEY,
    descricao              VARCHAR(100),
    capacidadeVolumetrica  DECIMAL(5,2),
    precoDiario            DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS Cacamba (
    patrimonio         VARCHAR(20)  PRIMARY KEY,
    capacidade         DECIMAL(5,2),
    cor                VARCHAR(30),
    situacao           VARCHAR(20)  NOT NULL,
    codigoTipoCacamba  VARCHAR(20)  NOT NULL,
    FOREIGN KEY (codigoTipoCacamba) REFERENCES TipoCacamba(codigo)
);

CREATE TABLE IF NOT EXISTS Caminhao (
    placa            VARCHAR(7)    PRIMARY KEY,
    modelo           VARCHAR(50)   NOT NULL,
    anoFabricacao    INTEGER,
    capacidadeCarga  DECIMAL(6,2),
    situacao         VARCHAR(20)   NOT NULL
);

CREATE TABLE IF NOT EXISTS Motorista (
    matricula     VARCHAR(20)   PRIMARY KEY,
    nome          VARCHAR(100)  NOT NULL,
    cpf           VARCHAR(11)   NOT NULL,
    cnh           VARCHAR(11)   NOT NULL,
    categoriaCnh  VARCHAR(2)    NOT NULL,
    telefone      VARCHAR(15)
);

CREATE TABLE IF NOT EXISTS Locacao (
    codigo                 VARCHAR(20)   PRIMARY KEY,
    enderecoEntrega        VARCHAR(100),
    dataSolicitacao        DATE,
    dataPrevistaEntrega    DATE,
    periodoPrevistoDias    INTEGER,
    qtdeSolicitada         INTEGER,
    observacoes            VARCHAR(255),
    valorDiariaContratado  DECIMAL(10,2),
    valorTotal             DECIMAL(10,2),
    formaPagamento         VARCHAR(30),
    dataEncerramento       DATE,
    situacao               VARCHAR(20)   NOT NULL,
    cpfCnpjCliente         VARCHAR(14)   NOT NULL,
    codigoTipoCacamba      VARCHAR(20)   NOT NULL,
    FOREIGN KEY (cpfCnpjCliente)    REFERENCES Cliente(cpfCnpj),
    FOREIGN KEY (codigoTipoCacamba) REFERENCES TipoCacamba(codigo)
);

CREATE TABLE IF NOT EXISTS OrdemDeServico (
    codigo            VARCHAR(20)  PRIMARY KEY,
    tipoOS            VARCHAR(20),
    dataPrevista      DATE,
    dataHoraAgendada  TIMESTAMP,
    dataHoraExecucao  TIMESTAMP,
    observacoes       VARCHAR(255),
    situacao          VARCHAR(20)  NOT NULL,
    codigoLocacao     VARCHAR(20)  NOT NULL,
    FOREIGN KEY (codigoLocacao) REFERENCES Locacao(codigo)
);
