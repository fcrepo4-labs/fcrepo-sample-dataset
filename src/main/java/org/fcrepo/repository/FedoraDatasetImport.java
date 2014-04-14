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
import org.slf4j.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import static java.lang.Integer.MAX_VALUE;
import static org.slf4j.LoggerFactory.getLogger;
import static javax.ws.rs.core.Response.Status.CREATED;

/**
 * Tool to load the sample dataset into the Fedora repository
 * 
 * Arguments: args[0]: Fedora repository, default http://localhost:8080 args[1]:
 * resource dataset, default resources/data
 * 
 * @author lsitu
 * @date Apr 14, 2014
 */
public class FedoraDatasetImport {
    private static final Logger LOGGER = getLogger(FedoraDatasetImport.class);

    public static void main(final String[] args) {

        final String fcrepoUrl = System.getProperty("fcrepo.url", args[0]);
        final String dataDir = System.getProperty("dataset.resource", args[1]);

        final HttpClient httpClient = HttpClientBuilder.create().setMaxConnPerRoute(MAX_VALUE)
            .setMaxConnTotal(MAX_VALUE).build();

        final File[] files = new File(FedoraDatasetImport.class.getResource(dataDir).getFile()).listFiles();
        LOGGER.info("Dataset dir: " + new File(dataDir).getAbsolutePath());
        for(int i=0; i<files.length; i++){

            try {
                final boolean successful = importData(httpClient, fcrepoUrl, files[i]);
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

    private static boolean importData(final HttpClient httpClient, final String repoUrl, final File src)
        throws Exception {
        LOGGER.debug("Importing Data file: " + src.getAbsolutePath());
        HttpPost post = null;
        HttpResponse res = null;

        try {

            post = new HttpPost(repoUrl + "/fcr:import");
            post.setEntity(new FileEntity(src));
            res = httpClient.execute(post);
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
}
