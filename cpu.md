- Refatorei método mergeListsAndInsertNewItensIntoDB() da main activity, para fazer um bulk insert no lugar de vários inserts no caso do SQLiteOpenHelper, para mostrar essa refatoração optei por manter os dois tipos de acesso ao db (room).
- Para reduzir um pouco o processamento, é exibido apenas uma parte da lista de itens na tela principal, e a partir de então, ao dar scroll até o fim, ele gera mais itens a ser mostrados.
    * Uso do CPU antes da modificação: 37.18%
    * Uso do CPU depois da modificação: 29.74%
----------------------------------------------------------------------------------------------------------------------------------------------------------
if(itemsToBeInserted.size() > 0){

    ContentValues[] contentValues = new ContentValues[itemsToBeInserted.size()];
    for (int i = 0; i < itemsToBeInserted.size(); i++){
        ContentValues contentValueTemp = new ContentValues();
        itemsToBeInserted.get(i).getFullContentValues(contentValueTemp);
        contentValues[i] = contentValueTemp;
    }
    provider.bulkInsert(PodcastProviderContract.EPISODE_LIST_URI,contentValues);
}


