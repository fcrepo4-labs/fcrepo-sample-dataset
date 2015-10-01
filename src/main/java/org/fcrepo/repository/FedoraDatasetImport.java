/**
 * Copyright 2013 DuraSpace, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fcrepo.repository;

import java.io.File;

import org.apache.http.client.methods.HttpPut;
import org.slf4j.Logger;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.util.EntityUtils;
import static java.lang.Integer.MAX_VALUE;
import static org.slf4j.LoggerFactory.getLogger;
import static javax.ws.rs.core.Response.Status.CREATED;

/**
 * Tool to load the sample dataset into the Fedora repository Arguments: args[0]: Fedora repository, default
 * http://localhost:8080 args[1]: resource dataset, default resources/data args[2]: username, default null args[3]:
 * password, default null
 *
 * @author lsitu
 * @author whikloj
 * @since Apr 14, 2014
 */
public class FedoraDatasetImport {
    private static final Logger LOGGER = getLogger(FedoraDatasetImport.class);

    public static void main(final String[] args) {

        final String fcrepoUrl = System.getProperty("fcrepo.url", args[0]);
        final String dataDir = System.getProperty("dataset.resource", args[1]);
        final String username = System.getProperty("fcrepo.authUser", args[2]);
        final String password = System.getProperty("fcrepo.authPassword", args[3]);

        final HttpClient httpClient = HttpClientBuilder.create().setMaxConnPerRoute(MAX_VALUE)
            .setMaxConnTotal(MAX_VALUE).build();

        LOGGER.info("fcrepoUrl:" + fcrepoUrl);
        LOGGER.info("Dataset dir:" + new File(FedoraDatasetImport.class.getResource(dataDir).getFile()).getAbsolutePath());
        LOGGER.info("fcrepo.authUser: " + username);
        LOGGER.info("fcrepo.authPassword: " + password);

        try {
            importTransform(httpClient, fcrepoUrl, username, password);
        } catch (final Exception e) {
            LOGGER.error("Error importing transform", e);
        }

        final File[] files = new File(FedoraDatasetImport.class.getResource(dataDir).getFile()).listFiles();
        for(int i=0; i<files.length; i++){

            try {
                final boolean successful = importData(httpClient, fcrepoUrl, files[i], username, password);
                if (successful) {
                    LOGGER.info("Imported data file: " + files[i].getAbsolutePath());
                } else {
                    LOGGER.info("Failed to imported data file: " + files[i].getAbsolutePath());
                }
            } catch (final Exception e) {
                LOGGER.error("Error import file " + files[i].getPath(), e);
            }
        }
    }

    private static boolean importData(final HttpClient httpClient, final String repoUrl, final File src, final String username, final String password)
            throws Exception {
        LOGGER.debug("Importing Data file: " + src.getAbsolutePath());
        HttpPost post = null;
        HttpResponse res = null;

        try {

            post = new HttpPost(repoUrl + "/fcr:import");
            if (username != null && password != null) {
                setAuth(post, username, password);
            }
            post.setEntity(new FileEntity(src));
            res = httpClient.execute(post);
            LOGGER.debug("URL:" + repoUrl + "/fcr:import");
            LOGGER.debug("Response:" + res.toString());
            return res.getStatusLine().getStatusCode() == CREATED.getStatusCode();
        } catch (final Exception e) {
            if (res != null) {
                LOGGER.error(EntityUtils.toString(res.getEntity()));
            }
            throw e;
        } finally {
            post.releaseConnection();
        }
    }

    private static boolean importTransform(final HttpClient httpClient, String repoUrl, final String username, final String password)
            throws Exception {
        HttpPut put = null;
        HttpResponse res = null;
        repoUrl += "/fedora:system/fedora:transform/fedora:ldpath/default/nt:base";
        final String entity = "@prefix fcrepo : <http://fedora.info/definitions/v4/repository#>\n" +
                "id      = . :: xsd:string ;\n" +
                "title = dc:title :: xsd:string ;\n" +
                "resourcename = fcrepo:uuid :: xsd:string ;";
        LOGGER.debug("Transform body:" + entity);
        try {

            put = new HttpPut(repoUrl);
            if (username != null && password != null) {
                setAuth(put, username, password);
            }
            put.setEntity(new StringEntity(entity));
            res = httpClient.execute(put);
            LOGGER.debug("URL:" + repoUrl + "/fcr:import");
            LOGGER.debug("Response:" + res.toString());
            return res.getStatusLine().getStatusCode() == CREATED.getStatusCode();
        } catch (final Exception e) {
            if (res != null) {
                LOGGER.error(EntityUtils.toString(res.getEntity()));
            }
            throw e;
        } finally {
            put.releaseConnection();
        }
    }

    private static void setAuth(final AbstractHttpMessage method, final String username, final String password) {
        final String creds = username + ":" + password;
        final String encCreds = new String(Base64.encodeBase64(creds.getBytes()));
        final String basic = "Basic " + encCreds;
        method.setHeader("Authorization", basic);
    }
}
