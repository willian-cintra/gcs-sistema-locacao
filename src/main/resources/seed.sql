-- Dados de exemplo, inseridos apenas quando o banco é criado.

INSERT INTO Cliente (cpfCnpj, nomeRazaoSocial, endereco, cidade, estado, telefone, email)
VALUES ('12345678901', 'Construtora Silva Ltda', 'Rua das Obras, 100', 'Campo Grande', 'MS', '67999990000', 'contato@silva.com');

INSERT INTO TipoCacamba (codigo, descricao, capacidadeVolumetrica, precoDiario)
VALUES ('TC-4M', 'Caçamba 4 m³', 4.0, 35.00);

INSERT INTO TipoCacamba (codigo, descricao, capacidadeVolumetrica, precoDiario)
VALUES ('TC-5M', 'Caçamba 5 m³', 5.0, 42.00);

INSERT INTO Cacamba (patrimonio, capacidade, cor, situacao, codigoTipoCacamba)
VALUES ('PAT-001', 4.0, 'amarela', 'disponivel', 'TC-4M');

INSERT INTO Cacamba (patrimonio, capacidade, cor, situacao, codigoTipoCacamba)
VALUES ('PAT-002', 4.0, 'amarela', 'disponivel', 'TC-4M');

INSERT INTO Cacamba (patrimonio, capacidade, cor, situacao, codigoTipoCacamba)
VALUES ('PAT-003', 5.0, 'verde', 'disponivel', 'TC-5M');

INSERT INTO Caminhao (placa, modelo, anoFabricacao, capacidadeCarga, situacao)
VALUES ('ABC1D23', 'Mercedes-Benz Atego 2430', 2019, 14.0, 'disponivel');

INSERT INTO Motorista (matricula, nome, cpf, cnh, categoriaCnh, telefone)
VALUES ('M001', 'João Pereira', '98765432100', '01234567890', 'D', '67988887777');
