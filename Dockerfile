# Plugin dla RabbitMQ zeby wlaczyc delay w DLQ
# - https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/
# - [Ktos zrobil dockera](https://github.com/heidiks/rabbitmq-delayed-message-exchange)
# - [z linku wyzej wlasny Dockerfile](https://hub.docker.com/r/heidiks/rabbitmq-delayed-message-exchange/dockerfile)
#
# Tutaj wlasnie mamy wlasny Dockerfile z pluginem
FROM rabbitmq:3.10.2-management

RUN apt-get update

RUN apt-get install -y curl

RUN curl -L https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases/download/3.10.2/rabbitmq_delayed_message_exchange-3.10.2.ez > $RABBITMQ_HOME/plugins/rabbitmq_delayed_message_exchange-plugin.ez

RUN chown rabbitmq:rabbitmq $RABBITMQ_HOME/plugins/rabbitmq_delayed_message_exchange-plugin.ez

RUN rabbitmq-plugins enable --offline rabbitmq_delayed_message_exchange

RUN rabbitmq-plugins enable --offline rabbitmq_consistent_hash_exchange