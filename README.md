<h1 align="center"> :microscope: Back end - Science Fair System :microscope: </h1>
<p align="center">
  <img src="https://user-images.githubusercontent.com/62837683/226001451-a53c72be-f2c6-4f5c-9524-f0026338563b.png" />
</p>
<p align="center">
  <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white" />
  <img src="https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white" />
  <img src="https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white" />
  <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white" />
  <img src="https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white" />
  <img src="https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white" />
</p>
<h3 align="center"> 
    :construction:  Project in Development  :construction:
</h3>

![tests](https://img.shields.io/badge/passed%20tests-100-informational)

![version](https://img.shields.io/badge/version-v0.1.6-brightgreen)

<h2 align="center"> :alembic: Introduction / Introdução :alembic: </h2>
<p> :us: Hello! And welcome to this project for a school science fair. The main objective behind this project is to study and to use as portfolio.
Hope you enjoy as much as I do.

:brazil: Olá! E bem-vindo a este projeto para uma feira de ciências escolar. O principal objetivo do projeto é estudar e usá-lo como portfólio. Espero que goste tanto quanto eu!
</p>

<h2 align="center"> :woman_technologist: Technologies, APIs and Libraries / Tecnologias, APIs e Bibliotecas :man_technologist: </h2>
<p>

:us:
- Contract First
- Java 11
- Maven
- Swagger OpenApi 3.0
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- JUnit 5
- Mockito
- H2Console
- MySQL
- Mapstruct
- Lombok

:brazil:
- Abordagem contract first
- Java 11
- Maven
- Swagger OpenApi 3.0
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- JUnit 5
- Mockito
- H2Console
- MySQL
- Mapstruct
- Lombok
</p>
<h2 align="center"> :book: Problem Description / Descrição do Problema :book:</h2>
<p> 

:us: Problem: We need a web system developed in the form of a Rest API to control school science fairs. The fairs take place annually, from the fifth year of elementary school to the third year of high school.
- We need to register, change and delete classes.
- Students are enrolled in classes annually.
- We need to register teachers because they will guide the groups.
- We need to register assessments and assessment systems as these can change every year.
- Each edition of the science fair will have its own evaluation system.
- We would like students to be able to register their own work and group and be able to follow their group's assessments.
- Each registered work will be linked to an edition of the fair, will have a list of evaluations and a list of students, as well as which teacher is responsible for the group.
-Students cannot access groups that they do not own, but organizing teachers can access and grade groups.

The front end of this application can be found here: **** WILL BE DEVELOPED AFTER BACK END IS FINISHED ****

:brazil: Problema: precisamos de um sistema web desenvolvido na forma de uma API Rest para controle de feiras de ciências na escola. As feiras acontecem anualmente, das turmas de quinto ano do ensino fundamental até o terceiro ano do ensino médio.
- Precisamos cadastrar, alterar e deletar turmas.
- Anualmente os estudantes são cadastrados nas turmas.
- Precisamos cadastrar professores pois estes serão orientadores dos grupos.
- Precisamos cadastrar avaliações e sistemas avaliativos pois estes podem mudar todos os anos.
- Cada edição da feira de ciências terá um sistema avaliativo próprio.
- Gostaríamos que os estudantes pudessem cadastrar seus próprios trabalhos e grupos e pudessem acompanhar as avaliações do seu grupo.
- Cada trabalho cadastrado será vinculado a uma edição da feira, terá uma lista de avaliações e uma lista de estudantes, bem como qual professor é responsável pelo grupo.
- Estudantes não podem acessar grupos que não são seus, mas os professores organizadores podem acessar e avaliar os grupos.

O front end da aplicação pode ser encontrado aqui: **** SERÁ DESENVOLVIDO APÓS O TÉRMINO DO BACK END ****
</p>

<h2 align="center"> :hammer_and_wrench: How can I build this project locally? / Como posso rodar esse projeto localmente? </h2>
<p>

:us: To run it in the current state of development, you can just clone this repository, under the main branch you will find the latest stable release, you can open it using the IDE you like, download dependencies and run it (OBS: it is important to run mvn clean compile before running the project)! Then, use Postman to make requests to the API.
It runs in the localhost 8080 port, so you can use the links:

- Swagger: http://localhost:8080/swagger-ui/index.html
- h2-console: http://localhost:8080/h2-console

Future releases will come with more info and ways to run, with a version hosted so you can see how it works remotely.

:brazil: Para executá-lo no estado atual de desenvolvimento, você pode apenas clonar este repositório, nabranch main você encontrará a versão funcional mais recente, você pode abri-lo usando a IDE que você gosta, baixar as dependências e executá-lo (OBS: é importante executar mvn clean compile antes de executar o projeto)! Então é só usar Postman para fazer suas requisições à API.
Ele roda na porta localhost 8080, então você pode usar os links:

- Swagger: http://localhost:8080/swagger-ui/index.html
- h2-console: http://localhost:8080/h2-console

Versões futuras virão com mais informações e formas de rodar, com uma versão hospedada para você ver como funciona remotamente.
</p>
<h2 align="center"> :rocket:Features already implemented / Funcionalidades já implementadas :rocket:</h2>
<p>

:us:
- Creation, Read, Update and Deletion of classes
- Creation, Read, Update and Deletion of students
- Creation, Read, Update and Deletion of Areas of Knowledge
- Creation, Read, Update and Deletion of Grade Systems

:brazil:
- Criação, edição, deleção e listagem de turmas
- Criação, edição, deleção e detalhes de estudante
- Criação, edição, deleção, listagem e detalhes de área do conhecimento
- Criação, edição, deleção, listagem e detalhes de Sistemas Avaliativos
</p>
<h2 align="center"> :bicyclist: Upcoming Features / Próximas funcionalidades :bicyclist:</h2>
<p>

:us:
- creating, listing, editing and deleting assessments
- create, list, edit and delete science fair edition
- link students in assessments and assessments in science fair issues
- creation of teachers, users and permissions

:brazil:
- criação, listagem, edição e deleção de avaliações
- criação, listagem, edição e deleção de edição da feira de ciências
- vincular estudantes em trabalhos e trabalhos em edições de feiras de ciências
- criação de professores, usuários e permissões
</p>
