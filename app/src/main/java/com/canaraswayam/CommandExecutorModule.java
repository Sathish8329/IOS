package com.canaraswayam; // Update to your package name

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

public class CommandExecutorModule extends ReactContextBaseJavaModule {
    private final ReactApplicationContext reactContext;

    public CommandExecutorModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "CommandExecutorModule";
    }

    @ReactMethod
    public void executeCommand(String command, Promise promise) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String info = in.readLine();
            if (info != null)
                promise.resolve(info);
            else
                promise.resolve("No Info");
        } catch (Exception e) {
            promise.reject("COMMAND_ERROR", e.getMessage());
        } finally {
            if (process != null)
                process.destroy();
        }
    }

    @ReactMethod
    public void checkPortRunning(int port, Promise promise) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("localhost", port), 1000); // 1000 ms timeout
            socket.close();
            promise.resolve(true);
        } catch (Exception e) {
            promise.resolve(false);
        }
    }
}
