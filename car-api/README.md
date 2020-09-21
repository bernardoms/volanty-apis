# Car APi
Uma api de carros para a volanty.

## Technologias usadas
* JAVA 11
* Spring boot 2.3.3
* Cache(Caffeine)
* Mongo 4.x
* Maven
* Junit 5
* ELK stack for logs
* Docker
* Swagger


## Como rodar

 * Os endpoints disponiveis e como chamar se encontram no endpoint http://localhost:8080/swagger-ui.html
 * É possivel checar o status da api usando o path `/actuator/health`
 * É possivel usar o new relic na aplicação precisa apenas passar o -e com app name e o new relic 
 key na hora de rodar o container docker

## Rodando no container :
* Buildar o projeto com `mvn clean package`
* Buildar imagem docker `docker build -t car-api .`
* Rodar imagem docker `docker run  -p 8080:8080 car-api`

## Rodando local
  `./mvnw clean package` 
  `cd deps`
  `java -jar target/car-api-0.0.1`

## Consideracoes
* O id dos carros eu nao usaria como um id sequencial mas sim o objectId que o mongo já
gera para mim, como estava com id sequencial no JSON de carro disponibilizado tive que deixar assim

* Como o pré requisito é usar o arquivo JSON como banco de dados, nessa api deixei apenas um endpoint de 
get por id ou de get geral

* Para nao ficar lendo do arquivo toda hora, coloquei cache usando o caffeine. Poderia
ter colocado um @Bean que quando subisse a aplicação leria do JSON e ja teria tudo em memoria,
mas para o futuro removendo O JSON e colocando um banco como o mongo, fica mais facil se nao for dessa forma
usando o @Bean. Para esse caso do desafio, não faz diferença de performance ter o cache pois o arquivo é pequeno.