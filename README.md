# Sistema Sagrado — Gestão Paroquial

Projeto final de **POO 2 (EC46A)** — UTFPR, Campus Cornélio Procópio.

**Autoras:** Vitória Camila do Carmo Pinafo (RA 2525933) e Tania Mara de Souza (RA 2768828)

## Sobre o sistema

Sistema para gerenciar as atividades de uma paróquia: fiéis (padres, coroinhas,
catequistas e paroquianos comuns), missas, sacramentos, casamentos, catequizandos,
doações, despesas e eventos.

## Tecnologias

- Java 17 e Java Swing (interface gráfica)
- Arquitetura MVC (`model`, `view`, `controller`, `repository`, `infra`)
- Maven
- MySQL hospedado na nuvem (Aiven), acessado por JDBC
- Apache NetBeans

## Como executar

Requisitos: **JDK 17 ou superior**, **Apache NetBeans** e **conexão com a internet**
(o banco de dados fica na nuvem, não precisa instalar MySQL).

1. Baixe ou clone este repositório
2. No NetBeans: **File > Open Project** e selecione a pasta do projeto
3. Execute a classe `br.com.paroquia.view.FormPrincipal`

## Banco de dados

- Os dados ficam em um MySQL no Aiven. A conexão está em
  `src/main/java/br/com/paroquia/infra/Conexao.java`
- Tabelas: `fiel` (padres, coroinhas, catequistas e fiéis comuns, diferenciados
  pela coluna `tipo`), `missa`, `missa_coroinha`, `sacramento`, `casamento`,
  `catequizando`, `doacao`, `despesa` e `evento`
- Script para criar as tabelas: `banco/script.sql`

Se o sistema mostrar erro de conexão, verifique a internet.

## Funcionalidades

- Cadastro de fiéis (paroquiano, padre, coroinha e catequista), missas,
  sacramentos, casamentos, catequizandos, doações, despesas e eventos
- Consulta de fiel por CPF, alteração de nome e exclusão
- Relatórios gerais e por função (fiéis, coroinhas, padres e catequistas)
