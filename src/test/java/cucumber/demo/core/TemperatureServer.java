/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package cucumber.demo.core;

import java.io.IOException;

import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;
import org.webbitserver.WebSocketConnection;
import org.webbitserver.handler.EmbeddedResourceHandler;

public class TemperatureServer {
    private final WebServer webServer;

    public TemperatureServer(int port) {
        webServer = WebServers.createWebServer(port);
        webServer.add(new EmbeddedResourceHandler("web"));
        webServer.add("/temperature", new BaseWebSocketHandler() {
            @Override
            public void onMessage(WebSocketConnection connection, String msg) throws Throwable {
                String[] parts = msg.split(":");
                double t = Double.parseDouble(parts[1]);
                if (parts[0].equals("celcius")) {
                    double f = (9.0 / 5.0) * t + 32;
                    connection.send("fahrenheit:" + f);
                }
            }
        });
    }

    public void start() throws IOException {
        webServer.start();
    }

    public void stop() throws IOException {
        webServer.stop();
    }

    public static void main(String[] args) throws IOException {
        new TemperatureServer(9988).start();
    }
}
