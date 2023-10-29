package com.canaraswayam;

import com.facebook.react.modules.network.OkHttpClientFactory;
import com.facebook.react.modules.network.OkHttpClientProvider;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;

public class SSLPinnerFactory implements OkHttpClientFactory {

        private static String hostname = "hrmsuat.canarabank.in";
        // private static String hostname = "hrms.canarabank.in";

        public OkHttpClient createNewNetworkModuleClient() {

                CertificatePinner certificatePinner = new CertificatePinner.Builder()
                                // .add(hostname,
                                // "sha256/tc0M2LJvfhdSpZlXGIHS/yq22kslQ/47ufpvdJ9VPJA=").build();
                                .add(hostname, "sha256/7ZRcD0F0jlszw/MvbS03UcqJfoCbTBLuyWjPdBKeBCo=").build();

                // Get a OkHttpClient builder with all the React Native defaults

                OkHttpClient.Builder clientBuilder = OkHttpClientProvider.createClientBuilder();

                return clientBuilder
                                .certificatePinner(certificatePinner)
                                .build();
        }
}