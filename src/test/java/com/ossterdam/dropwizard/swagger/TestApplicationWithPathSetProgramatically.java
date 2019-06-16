/*
 * Copyright © 2019 Ossterdam (mohamed.o.alnagdy@gmail.com)
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
package com.ossterdam.dropwizard.swagger;

import io.dropwizard.Application;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TestApplicationWithPathSetProgramatically extends Application<TestConfiguration> {

  public static final String BASE_PATH = "/api";

  @Override
  public void initialize(Bootstrap<TestConfiguration> bootstrap) {
    bootstrap.addBundle(
        new SwaggerBundle<TestConfiguration>() {
          @Override
          protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(
              TestConfiguration configuration) {
            SwaggerBundleConfiguration swaggerBundleConfiguration =
                new SwaggerBundleConfiguration();
            swaggerBundleConfiguration.setResourcePackage("com.ossterdam.dropwizard.swagger");

            // since this Application sets the root path in the run()
            // method, we need to tell the bundle what that path is because
            // by the time the bundle initializes it will not have the
            // necessary info to derive the path that is later going to be
            // set below in the run() method.
            swaggerBundleConfiguration.setUriPrefix(BASE_PATH);
            return swaggerBundleConfiguration;
          }
        });
  }

  @Override
  public void run(TestConfiguration configuration, final Environment environment) throws Exception {
    ((DefaultServerFactory) configuration.getServerFactory()).setJerseyRootPath(BASE_PATH + "/*");
    environment.jersey().register(new TestResource());
  }
}