## Volanty - APIS
São as apis feitas para o desafios da volante
esse projeto é composto de duas apis
 - car-api
 - cav-api
 
para rodar cada uma sepada acessar a pasta dos projetos das apis e ler o README.md

## Como Rodar
entrar na pasta de cada projeto e dar `mvn clean package` para ter o jar buildado
depois `docker-compose build` 
e para rodar `docker-compose up -d `
* É possível ver os logs do kibana usando o endpoint http://localhost:5601 , precisa somente criar um index.
* É possível mudar o docker compose adicionando a key do newrelic e fazer monitoria por ele.

## Melhorias
* Adicionar cache na api do cav.
* Adicionar app id no filebeat para diferenciar cada app.
* Adicionar doc do fluxo