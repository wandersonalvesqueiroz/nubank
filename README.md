# API de Ganho de Capital

API RESTful desenvolvida com Spring Boot para calcular o imposto 
sobre operações de compra e venda de ações.

---

## 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot 3.5.4
- Swagger/OpenAPI (Springdoc)
- Maven

---

# 💰 Cálculo de Imposto sobre Operações

Este projeto implementa a lógica de cálculo de imposto sobre operações financeiras,
utilizando o padrão de projeto **Strategy** para garantir flexibilidade, testabilidade e extensibilidade.

---

## 🧠 Padrão Strategy

https://refactoring.guru/design-patterns/strategy

O padrão Strategy foi adotado para encapsular diferentes formas de cálculo de imposto,
permitindo que o comportamento seja alterado dinamicamente conforme o tipo de operação
(venda, compra, etc.).

---

## 📁 Estrutura do Projeto

```
src/
└── main/
└── java/
└── com/nubank/capitalgain/
├── controller/                 # Camada de entrada da aplicação (API/REST Controllers)
│   └── impl/                   # Implementações concretas dos controllers
├── domain/                     # Regras de negócio e núcleo do sistema
│   ├── constants/              # Constantes globais (mensagens e valores fixos)
│   ├── model/                  # Entidades e objetos de domínio (ex: Portfolio, Operation)
│   ├── service/                # Interfaces e contratos de serviços de negócio
│   │   └── impl/               # Implementações dos serviços
│   ├── strategy/               # Estratégias de cálculo (padrão Strategy)
│   │   ├── config/             # Configuração e injeção das estratégias
│   │   └── impl/               # Implementações das estratégias de cálculo
│   ├── util/                   # Funções utilitárias e helpers
│   ├── validation/             # Validações de entrada e regras complementares
│   └── exception/              # Exceções customizadas e manipuladores globais
└── resources/                  # Arquivos de configuração e recursos do projeto
├── application.properties      # Configurações da aplicação Spring Boot
└── swagger.yaml                # Especificação OpenAPI/Swagger
```

---

## ✅ Testes

Os testes foram escritos com JUnit 5, cobrindo os principais cenários:

- Lucro acima do limite de isenção
- Lucro abaixo do limite (isenção)
- Prejuízo (sem imposto)
- Cálculo com diferentes custos médios

## 📌 Regras de Negócio

- **Isenção**: operações com receita abaixo de R$20.000 no mês são isentas de imposto.
- **Alíquota**: 20% sobre o lucro líquido.
- **Lucro**: calculado como receita - custo médio ponderado.
- **Prejuízo**: não gera imposto, mas pode ser compensado em operações futuras.

### Pré-requisitos

- Java 21
- Maven 3.8+

---

### Passos

```bash
# Clone o projeto
git clone https://github.com/wandersonalvesqueiroz/nubank.git
cd capitalgain

# Compile e execute o projeto
./mvnw spring-boot:run
```

> A aplicação será executada em: `http://localhost:8080`

---

## 🧪 Testando a API

Você pode usar o Swagger para testar os endpoints diretamente no navegador.

### Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

---
---

## 🔄 Endpoints Disponíveis

| Método | Endpoint                  | Descrição                                   |
|--------|---------------------------|---------------------------------------------|
| GET    | `/api/capital-gains`      | Calcula o imposto sobre operações agrupadas |

---


Desenvolvido por **Wanderson Alves**  
🔗 [LinkedIn](https://www.linkedin.com/in/wandersonalvesqueiroz/)  
📧 wandersonmg18@gmail.com