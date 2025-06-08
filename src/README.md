Новые/измененные файлы
utility
    CommandSerializer - для сериализации и десериализации объектов

server
    ConnectionManager - управляет подключениями клиента 
    RequestReader - читает запросы от клиента 
    CommandProcessor - обрабатывает команды 
    ResponseSender - отправляет ответы клиенту 
    Server - точка входа для сервера 

models 
    Все модели теперь реализуют интерфейс Serializable

commands
    CommandWrapper - класс-обертка для команд и их аргументов

client
    ClientConnectionManager - управляет подключением к серверу
    CommandReader - читает команды из консоли 
    ResponseHandler - обрабатывает ответы от сервера
    Client - точка входа для клиента 

