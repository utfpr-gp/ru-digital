# ru-digital
É um sistema desenvolvido principalmente para automatizar saldos financeiros em restaurantes universitários. Inicialmente o sistema foi desenvolvido para suportar apenas um restaurante, após verificar a necessidade de permitir também outros estabelecimentos o sistema foi ampliado.


## Requisitos para rodar a aplicação

- Java instalado
- Maven para gerenciar as dependências
- Mysql instalado para armazenar os dados

## Tecnologias
- Spring
- Hibernate
- Materialize
- Jquery
- Cloudinary

## Ferramentas

- Spring
- Mysql
- Spring boot

## Instruções para rodar a aplicação

- Criar o banco no Mysql 
- Mudar as credencias do banco no arquivo properties (spring.datasource.username e spring.datasource.password)
- Mudar as credencias para envio de email com SMTP (spring.mail.username e spring.mail.password)
- Acessar a pasta do projeto via terminal e rodar mvn clean install para instalar as dependências
- Rodar mvn spring-boot:run no terminal
- Duas empresas já são cadastradas automaticamente no banco para uso RU e Lanchenete
- Acesso administrativo RU:
                              login:  1111111
                              senha: mudar123
                              
- Acesso administrativo Lanchonete:   
                              login:  0000000
                              senha: mudar123
## Aplicação online


Digital Ocean:
https://utfcoin.com

Heroku:
https://nameless-wave-82551.herokuapp.com


## Vídeo demonstrativo

https://www.youtube.com/watch?v=ZE1EOazff_4



