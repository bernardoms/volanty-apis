# CAV APi
Uma api do CAV(Centro de Atendimento Volanty) para a volanty.

## Technologias usadas
* JAVA 11
* Spring boot 2.3.3
* Mongo 4.x
* Maven
* Junit 5
* ELK stack for logs
* Docker
* Docker-compose
* Swagger


## Como rodar

 * Os endpoints disponiveis e como chamar se encontram no endpoint http://localhost:8081/swagger-ui.html
 * É possivel checar o status da api usando o path `/actuator/health`
 * É possivel usar o new relic na aplicação precisa apenas passar o -e com app name e o new relic 
 key na hora de rodar o container docker

## Rodando :
* Buildar o projeto com `mvn clean package`
* Buildar imagem docker `docker-compose build`
* Rodar imagem docker `docker-compose up -d`

## Consideracoes
* O arquivo de calendario nao tem um dado muito bem estruturado, para nao precisar criar varias classes
com varios maps, usei o JSON NODE e naveguei por ele.
* Como o arquivo jar é somente leitura, precisei ler o arquivo e jogar em memoria no repository para ficar escrevendo sempre
nesse objeto.