package com.chongwu.utils;

import java.io.File;
import java.util.List;

import com.chongwu.config.AuthParameters;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

 
public class TaobaoHttpClient {
        private static final int CONNECTION_TIMEOUT = 20000;
        private static Log log = LogFactory.getLog(TaobaoHttpClient.class);
        public TaobaoHttpClient() {

        }

        /**
         * GET method.
         *
         * @param url
         *            The remote URL.
         * @param queryString
         *            The query string containing parameters
         * @return Response string.
         * @throws Exception
         */
        public String httpGet(String url) throws Exception {
                String responseData = null;
                 
                log.info("httpGet [1]. url = "+url);
               
                HttpClient httpClient = new HttpClient();
                GetMethod httpGet = new GetMethod(url);
                httpGet.getParams().setParameter("http.socket.timeout",
                                new Integer(CONNECTION_TIMEOUT));
                 
                try {
                        int statusCode = httpClient.executeMethod(httpGet);
                        if (statusCode != HttpStatus.SC_OK) {
                                log.info("HttpGet [2] Method failed: "
                                                + httpGet.getStatusLine());
                        }
               
                        responseData = httpGet.getResponseBodyAsString();
                       
                        log.info(" httpGet [3] getResponseBodyAsString() = "
                                        + httpGet.getResponseBodyAsString());
                       
                } catch (Exception e) {
                        throw new Exception(e);
                } finally {
                        httpGet.releaseConnection();
                        httpClient = null;
                }

                return responseData;
        }

        /**
         * POST method.
         *
         * @param url
         *            The remote URL.
         * @param queryString
         *            The query string containing parameters
         * @return Response string.
         * @throws Exception
         */
        public String httpPost(String url, String queryString) throws Exception {
                String responseData = null;
                HttpClient httpClient = new HttpClient();

                log.info("QHttpClient httpPost [1] url = "+url);
                PostMethod httpPost = new PostMethod(url);
                httpPost.addParameter("Content-Type",
                                "application/x-www-form-urlencoded");
                httpPost.getParams().setParameter("http.socket.timeout",
                                new Integer(CONNECTION_TIMEOUT));
                if (queryString != null && !queryString.equals("")) {
                        httpPost.setRequestEntity(new ByteArrayRequestEntity(queryString
                                        .getBytes()));
                }

                try {
                        int statusCode = httpClient.executeMethod(httpPost);
                        if (statusCode != HttpStatus.SC_OK) {
                                System.err.println("HttpPost Method failed: "
                                                + httpPost.getStatusLine());
                        }
                        responseData = httpPost.getResponseBodyAsString();
                       
                        log.info("QHttpClient httpPost [2] responseData = "+responseData);
                } catch (Exception e) {
                        throw new Exception(e);
                } finally {
                        httpPost.releaseConnection();
                        httpClient = null;
                }

                return responseData;
        }

        /**
         * Using POST method with multiParts.
         *
         * @param url
         *            The remote URL.
         * @param queryString
         *            The query string containing parameters
         * @param files
         *            The list of image files
         * @return Response string.
         * @throws Exception
         */
        public String httpPostWithFile(String url, String queryString,
                        List<AuthParameters> files) throws Exception {

                String responseData = null;
                url += '?' + queryString;
                HttpClient httpClient = new HttpClient();
                PostMethod httpPost = new PostMethod(url);
                try {
                        List<AuthParameters> listParams = TaobaoHttpUtil
                                        .getQueryParameters(queryString);
                        int length = listParams.size() + (files == null ? 0 : files.size());
                        Part[] parts = new Part[length];
                        int i = 0;
                        for (AuthParameters param : listParams) {
                                parts[i++] = new StringPart(param.getName(),
                                                TaobaoHttpUtil.formParamDecode(param.getValue()), "UTF-8");
                        }

                        for (AuthParameters param : files) {
                                String filePath = param.getValue();
                                File file = new File(filePath);
                                String name = param.getName();
//                              String fileName = file.getName();
//                              String type = QHttpUtil.getContentType(file);
                               
                                // image/jpeg - image/png
                                parts[i++] = new FilePart(name, file, "image/jpeg", "utf-8");
                        }
               
                        httpPost.setRequestEntity(new MultipartRequestEntity(parts,
                                        httpPost.getParams()));

                        int statusCode = httpClient.executeMethod(httpPost);
                        if (statusCode != HttpStatus.SC_OK) {
                                System.err.println("HttpPost Method failed: "
                                                + httpPost.getStatusLine());
                        }
                        responseData = httpPost.getResponseBodyAsString();
                } catch (Exception e) {
                        throw new Exception(e);
                } finally {
                        httpPost.releaseConnection();
                        httpClient = null;
                }

                return responseData;
        }

}


