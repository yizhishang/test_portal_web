package cn.vpclub.pinganquan.mobile.base.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * Created by Administrator on 2016/6/25 0025.
 */
public class HttpClientUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private static int iProviderType = 0; //0-Sun, 1-IBM
    private static final String ibmJavaVmVendor = "IBM Corporation";

    private static int getVendorType()
    {
        Properties tSysProperties = System.getProperties();
        String tJvmVendor = tSysProperties.getProperty("java.vm.vendor");

        if(tJvmVendor.equals(ibmJavaVmVendor)){
            iProviderType = 1;
        }

        logger.info("common_info Current Jvm Vendor Type is :[{}][{}]", iProviderType, tJvmVendor);
        return iProviderType;
    }

    private static HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String s, SSLSession sslSession) {

            logger.info("WARNING: Hostname is not matched for cert.");
            return true;
        }
    };

    private static TrustManager ignoreCertificationTrustManager = new X509TrustManager() {

        private X509Certificate[] certificates;

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String authType) throws CertificateException {
            if(this.certificates == null){
                this.certificates = x509Certificates;
                logger.info("init at checkClientTrusted");
            }
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            if(this.certificates == null)
            {
                this.certificates = x509Certificates;
                logger.info("init at checkServerTrusted.");
            }

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

    };


    public static String sendRequest(String targetUrl, Map<String, Object> params, boolean isProxy){
        StringBuilder sb = new StringBuilder();
        try{
            for (Map.Entry<String, Object> entry: params.entrySet()) {
                sb.append(entry.getKey().toString() + "=" + URLEncoder.encode(entry.getValue().toString(), "UTF-8") + "&");
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return sendRequest(targetUrl, sb.toString().getBytes(), isProxy);
    }

    public static String sendRequest(String urlString, byte[] sendData, boolean isProxy){
        getVendorType();
        SSLContext sslContext;

        ByteArrayOutputStream buffer = new ByteArrayOutputStream(512);
        try{
            URL url = new URL(urlString);
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);

            boolean isHttps = urlString.startsWith("https");
            HttpURLConnection connection = null;
            if(isHttps){
                connection = (HttpsURLConnection)url.openConnection();
            }else{
                connection = (HttpURLConnection) url.openConnection();
            }

            if(isProxy){
                connection = (HttpsURLConnection)url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PropertiesUtil.getPropValue("proxy.host"), PropertiesUtil.getIntPropValue("proxy.port"))));
            }

            if(isHttps){
                TrustManager[] tm = {ignoreCertificationTrustManager};

                if(iProviderType == 1){
                    sslContext = SSLContext.getInstance("TLS", "IBMJSSE2");
                }else{
                    sslContext = SSLContext.getInstance("TLS", "SunJSSE");
                }

                sslContext.init(null, tm, new java.security.SecureRandom());

                SSLSocketFactory ssf = sslContext.getSocketFactory();
                ((HttpsURLConnection)connection).setSSLSocketFactory(ssf);
            }

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setAllowUserInteraction(false);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "close");
            connection.setRequestProperty("User-Agent", "sdb client");
            connection.setRequestProperty("Content-Length", String.valueOf(sendData.length));

            connection.setConnectTimeout(3000);
            connection.setReadTimeout(10000);

            OutputStream out = connection.getOutputStream();

            logger.info("request url-->{}", urlString);
            logger.info("request sendData-->{}", new String(sendData));

            out.write(sendData);
            out.flush();

            InputStream reader = connection.getInputStream();
            int code = connection.getResponseCode();
            if(code != 200)
            {
                throw  new Exception("请求" + urlString + "服务异常");
            }

            int contentLength = connection.getContentLength();
            byte[] bytes = new byte[contentLength];
            int length = reader.read(bytes);

            do{
                buffer.write(bytes, 0, length);
                length = reader.read(bytes);
            }while (length > 0);

            reader.close();
            out.close();
            connection.disconnect();

        }catch (Exception e){

            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {

        }
        String repString;
        try{
           repString = new String(buffer.toByteArray(), "GBK");
        }catch (UnsupportedEncodingException e){

            logger.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
        return repString;
    }


    public static void main(String[] args) {
        String url = "http://kh.zszq.com/servlet/json";

        Map<String, Object> params = Maps.newHashMap();

        params.put("funcNo", "501520");

        String result = sendRequest(url,params,false);
        System.out.println(result);
    }
















}
