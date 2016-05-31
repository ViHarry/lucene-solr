package org.apache.solr.handler;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.apache.solr.api.SpecProvider;
import org.apache.solr.common.SolrException;
import org.apache.solr.api.Api;
import org.apache.solr.api.ApiBag;
import org.apache.solr.common.util.Map2;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;


public class UpdateRequestHandlerApi extends UpdateRequestHandler  {


  @Override
  public Collection<Api> getApis() {
    return Collections.singleton(getApiImpl());
  }

  private Api getApiImpl() {
    return new Api(ApiBag.getSpec("core.Update")) {
      @Override
      public void call(SolrQueryRequest req, SolrQueryResponse rsp) {
        String path = req.getPath();
        String target =  mapping.get(path);
        if(target != null) req.getContext().put("path", target);
        try {
          handleRequest(req, rsp);
        } catch (RuntimeException e) {
          throw e;
        } catch (Exception e){
          throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,e );
        }
      }
    };
  }

  @Override
  public Boolean registerV1() {
    return Boolean.FALSE;
  }

  @Override
  public Boolean registerV2() {
    return Boolean.TRUE;
  }

  private static final Map<String, String> mapping = ImmutableMap.<String,String>builder()
      .put("/update", DOC_PATH)
      .put(JSON_PATH, DOC_PATH)
      .put("/update/json/commands", JSON_PATH)
      .build();
}