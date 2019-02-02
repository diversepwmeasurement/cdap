/*
 * Copyright © 2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package co.cask.cdap.internal.app.deploy.pipeline;

import co.cask.cdap.pipeline.AbstractStage;
import co.cask.cdap.spi.data.StructuredTableAdmin;
import co.cask.cdap.spi.data.TableAlreadyExistsException;
import co.cask.cdap.spi.data.table.StructuredTableSpecification;
import com.google.common.reflect.TypeToken;

import java.io.IOException;

/**
 * This {@link co.cask.cdap.pipeline.Stage} is responsible for creating system tables
 */
public class CreateSystemTablesStage extends AbstractStage<ApplicationDeployable> {
  private final StructuredTableAdmin structuredTableAdmin;

  public CreateSystemTablesStage(StructuredTableAdmin structuredTableAdmin) {
    super(TypeToken.of(ApplicationDeployable.class));
    this.structuredTableAdmin = structuredTableAdmin;
  }

  /**
   * Deploys dataset modules specified in the given application spec.
   *
   * @param input An instance of {@link ApplicationDeployable}
   */
  @Override
  public void process(ApplicationDeployable input) throws IOException, TableAlreadyExistsException {
    for (StructuredTableSpecification spec : input.getSystemTables()) {
      StructuredTableSpecification existing = structuredTableAdmin.getSpecification(spec.getTableId());
      if (existing == null) {
        // it's possible this throws TableAlreadyExistsException if two apps are deployed at the same time and there
        // is a race. In that case, fail deployment. On re-deployment, the existing spec will get checked with the
        // desired spec here. If they are the same, things will continue. If they differ, deployment will fail again.
        structuredTableAdmin.create(spec);
      } else if (!existing.equals(spec)) {
        // don't allow deploying the app if the app expects a specification different than the one that exists
        throw new IllegalArgumentException(
          String.format("System table '%s' already exists, but with a different specification.",
                        spec.getTableId().getName()));
      }
    }

    // Emit the input to next stage.
    emit(input);
  }
}
