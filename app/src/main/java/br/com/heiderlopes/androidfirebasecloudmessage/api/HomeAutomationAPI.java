package br.com.heiderlopes.androidfirebasecloudmessage.api;

import br.com.heiderlopes.androidfirebasecloudmessage.model.Message;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by heiderlopes on 03/09/16.
 */
public interface HomeAutomationAPI {

    @GET("/led/ligar")
    public void ligarLed(Callback<Message> resposta);

    @GET("/led/desligar")
    public void desligarLed(Callback<Message> resposta);

    @GET("/temperatura")
    public void getTemperatura(Callback<Message> resposta);

    @GET("/alarme/disparar")
    public void dispararAlarme(Callback<Message> resposta);

    @GET("/alarme/desativar")
    public void desativarAlarme(Callback<Message> resposta);
}
