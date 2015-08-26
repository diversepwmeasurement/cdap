/*
 * Copyright © 2015 Cask Data, Inc.
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

package co.cask.cdap.proto;

import co.cask.cdap.proto.artifact.ArtifactSummary;

import java.util.List;

/**
 * Represents an application returned for /apps/{app-id}.
 */
public class ApplicationDetail {
  private final String name;
  private final String version;
  private final String description;
  private final String configuration;
  private final List<StreamDetail> streams;
  private final List<DatasetDetail> datasets;
  private final List<ProgramRecord> programs;
  private final ArtifactSummary artifact;

  public ApplicationDetail(String name,
                           String description,
                           String configuration,
                           List<StreamDetail> streams,
                           List<DatasetDetail> datasets,
                           List<ProgramRecord> programs,
                           ArtifactSummary artifact) {
    this.name = name;
    this.version = artifact.getVersion();
    this.description = description;
    this.configuration = configuration;
    this.streams = streams;
    this.datasets = datasets;
    this.programs = programs;
    this.artifact = artifact;
  }

  public String getName() {
    return name;
  }

  /**
   * @deprecated use {@link #getArtifact()} instead
   *
   * @return the version of the artifact used to create the application
   */
  @Deprecated
  public String getVersion() {
    return version;
  }

  public String getDescription() {
    return description;
  }

  public String getConfiguration() {
    return configuration;
  }

  public List<StreamDetail> getStreams() {
    return streams;
  }

  public List<DatasetDetail> getDatasets() {
    return datasets;
  }

  public List<ProgramRecord> getPrograms() {
    return programs;
  }

  public ArtifactSummary getArtifact() {
    return artifact;
  }
}
