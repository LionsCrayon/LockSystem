package org.elox.locksystem.config;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    @Value("${mqtt.broker}")
    private String broker;

    @Value("${mqtt.client-id}")
    private String clientId;


    @Bean
    public MqttClient mqttClient() throws Exception {
        MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        client.connect(options);
        return client;
    }


    private MqttConnectOptions getOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        return options;
    }

    @Bean(name = "mqttClientForSimulator")
    public MqttClient mqttClientForSimulator() throws MqttException {
        MqttClient client = new MqttClient(broker, clientId + "-sim", new MemoryPersistence());
        client.connect(getOptions());
        return client;
    }

    @Bean(name = "mqttClientForListener")
    public MqttClient mqttClientForListener() throws MqttException {
        MqttClient client = new MqttClient(broker, clientId + "-listener", new MemoryPersistence());
        client.connect(getOptions());
        return client;
    }
}
