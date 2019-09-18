package org.openchat;

public class OpenChatLauncher {

    private static Routes routes;

    public static void main(String[] args) {
        wireDependencies();
        new OpenChat(routes).start();
    }

    private static void wireDependencies() {
        routes = new Routes();
    }

}
