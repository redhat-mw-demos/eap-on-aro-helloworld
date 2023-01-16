/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.helloworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import java.io.IOException;

/**
 * A simple REST service which is able to say hello to someone using HelloService Please take a look at the web.xml where JAX-RS
 * is enabled And notice the @PathParam which expects the URL to contain /json/David or /xml/Mary
 *
 * @author bsutter@redhat.com
 */

 @ApplicationScoped
@Path("/")
public class HelloWorld {

    @GET
    @Path("/javadetails")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> geJavaDetails() throws IOException, ApiException  {
        ApiClient client = ClientBuilder.cluster().build();

        // if you prefer not to refresh service account token, please use:
        // ApiClient client = ClientBuilder.oldCluster().build();
    
        // set the global default api-client to the in-cluster one from above
        Configuration.setDefaultApiClient(client);
    
        // the CoreV1Api loads default api-client from global configuration.
        CoreV1Api api = new CoreV1Api();
    
        // invokes the CoreV1Api client
        V1PodList list =
            api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        for (V1Pod item : list.getItems()) {
          System.out.println(item.getMetadata().getName());
        }

        Map<String, String> dets = new HashMap<String, String>();
        dets.put("Vendor", System.getProperty("java.vendor"));
        dets.put("Vendor Link", System.getProperty("java.vendor.url"));
        dets.put("Version", System.getProperty("java.version"));
        return dets;
    }

    @GET
    @Path("/serverconfig/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getServerConfig(@PathParam("name") String name) {
        return System.getProperty(name);
    }

    @GET
    @Path("/serverconfig")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> getAll() {
        return (Map)System.getProperties();
    }

}
