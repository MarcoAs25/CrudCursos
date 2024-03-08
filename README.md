# CrudCursos
Este é um CRUD simples utilizado principalmente como apoio aos estudos de tecnologias complementares do Spring Boot.
## Até o momento, o sistema possui apenas duas tabelas que implementam um repositório com comportamento de CRUD, sendo elas:
 * **Course**: Representa um curso, possuindo os campos: {id: long, name: string, categoria: category}
 * **Category**: Representa uma categoria, possuindo os campos: {id: long, name: string}
## Tecnologias Utilizadas
 1. **Spring Boot 3.2.3 & Java 17**: Framework e linguagem de programação utilizados para o desenvolvimento do projeto.
 2. **Maven**: Gerenciador de pacotes utilizado no projeto.
 3. **Lombok**: Biblioteca utilizada para a geração automática de getters, setters, e outros métodos boilerplate.
 4. **Mockito & JUnit**: Bibliotecas utilizadas para a criação de testes automatizados.
 5. **Docker Compose**: Ferramenta utilizada para a criação de contêineres Docker.
 6. **Redis**: Utilizado para caching em memória.
 7. **MySQL**: SGBD utilizado para a persistência de dados do sistema principal.
 8. **H2 Database**: SGBD utilizado para os testes automatizados.
 9. **Swagger**: Utilizado para a documentação da API.
## Próximos Passos
 * Criação de novos testes automatizados para estudo.
 * Implementação de autenticação JWT com Spring Security.
 * Etc.
