# Local Unit 

- **ItemFeed**: 
    * getFullContentValues
    * isIn
    * equals

    obs: Para poder testar o metodo _getFullContentValues_, foi necessário modificá-lo um pouco, em vez de declarar o __ContentValues__ dentro dele, é agora passado por parametro do metodo, podendo assim Mocka-lo antes de chamar o metodo.

- **XmlFeedParser**
    * readRss
    * readChannel
    * readItem
    * readData
    * readText
    * readEnclosure


# Instrumented tests

- **PodcastProvider**
    * insert
    * delete
    * query
    * update
