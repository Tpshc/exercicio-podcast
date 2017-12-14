- Adicionei comportamento ao clicar no botão baixar em algum dos podcats baseado no tipo de conexão no XmlFeedAdapter:
	-	WIFI: Baixa normalmente
	- 	Rede Móvel: Alerta o usuário de que ele está prestes a usar sua rede móvel para baixar
	- 	Sem conexão: Alerta o usuário de que não existe conexão
    
---------------------------------------------------------------------------------------------------------------------------------------------------------
    String networkStatus = Util.checkNetworkStatus(getContext());

    if(networkStatus.equals(Util.MOBILEDATA)){
        AlertDialog alertDialog = new AlertDialog.Builder(activityContext)
                .setTitle("Alerta")
                .setMessage("Você deseja realizar o download através da rede móvel?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Sim", new YesOnClickListener(b,v,position))
                .setNegativeButton("Não", null)
                .create();
        alertDialog.show();

    }else if(networkStatus.equals(Util.WIFI)){
        DownloadPodcast(b,v,position);
    }else {
        //no connection
        Toast.makeText(getContext(), "No connection", Toast.LENGTH_SHORT).show();
    }