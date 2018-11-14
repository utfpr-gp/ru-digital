# ru-digital


## Requisitos para rodar a aplicação

- Java instalado
- Maven para gerenciar as dependências
- Mysql instalado para armazenar os dados

## Instruções para rodar a aplicação

- Criar o banco no Mysql 
- Mudar as credencias do banco no arquivo properties (spring.datasource.username e spring.datasource.password)
- Mudar as credencias para envio de email com SMTP (spring.mail.username e spring.mail.password)
- Acessar a pasta do projeto via terminal e rodar mvn clean install para instalar as dependências
- Rodar mvn spring-boot:run no terminal

## Aplicação online

https://nameless-wave-82551.herokuapp.com
