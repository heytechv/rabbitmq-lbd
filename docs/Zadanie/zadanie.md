1. RabbitMQ w dockerze - uruchomić 
   1. Zobaczcie sobie strone managera RabbitMQ
2. Spring Cloud Stream - zapoznać się i użyć w aplikacji https://spring.io/pro=jects/spring-cloud-stream
   1. Spring Cloud Stream RabbitMQ
3. Zróbcie aplikacje Springową, która będzie miała endpointy:
   - POST /user - USER_CREATED
   - POST /article - ARTICLE_CREATED
   - PUT /article - ARTICLE_UPDATED
   - POST /comment - COMMENT_CREATED
   - PUT /comment - COMMENT_UPDATED
   - DELETE /comment - COMMENT_DELETED
4. Nadawajcie wiadomość na exchange typu topic (3 exchange dla kazdej domeny osobno)
5. Dodajce do aplikacji endpoint:
   - POST /emailToUser - command sendEmailToUser