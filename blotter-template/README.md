# Caplin Blotter Adapter Template

This project provides a starting point for writing blotter integration adapters based on Caplin's [Java DataSource API](http://www.caplin.com/developer/api/datasource_java/latest/) and Caplin's [Java Blotter API](http://www.caplin.com/developer/api/blotter_java/latest/). For a detailed guide on using the Java Blotter API, see [Serving blotter data with the Java Blotter API](http://www.caplin.com/developer/caplin-platform/cis/cis-blotter-integration-api-java).

This template is a [Gradle](https://gradle.org/) project. To avoid compatibility issues between the version of Gradle required by the project and the version of Gradle installed on your system, always run the project's Gradle tasks using the Gradle Wrapper from the root of the project: <code>./gradlew <em>task</em></code>

**Note**: the Gradle Wrapper requires an Internet connection. For more information, see [Executing a build with the Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html#using_wrapper_scripts) in the Gradle documentation.


## Getting started

Follow the instructions below to create a new adapter project based on the Blotter Adapter Template.

### Copy and customise the template

1.  Clone, or download and extract the latest version of the Caplin Project Templates repository:

    *   **Clone**:

        ```
        $ git clone https://github.com/caplin/project-templates.git
        ```
    
    *   **Download**:

        ```
        $ wget http://github.com/caplin/project-templates/archive/master.zip
        $ unzip -qoa master.zip
        ```

1.  Copy the template directory `blotter-template` and rename it to the name of your new project (for example, MyBlotterAdapter):

    ```
    $ cp -r ./blotter-template ~/src/MyBlotterAdapter
    ```

1.  Edit the file `~/src/MyBlotterAdapter/settings.gradle`, and change the value of the `rootProject.name` variable to the name of your adapter project (MyBlotterAdapter). When you later export your project as an [adapter blade](http://www.caplin.com/developer/caplin-platform/deployment-framework/cdf-blade-types#adapter-blade), the project name will be used as the name for the blade.

1.  If you have a Caplin website account and Internet access to <https://repository.caplin.com>, follow the steps below to enable automatic downloading of this project's Caplin dependencies:

    1.  In your `~/.gradle/gradle.properties` file (create it if it does not exist), add the following lines, replacing `<username>` and `<password>` with your Caplin credentials:

        ```
        caplinNexusUser=<username>
        caplinNexusSecret=<password>
        ```

1.  If you _don't_ have a Caplin website account and Internet access to <https://repository.caplin.com>, follow the steps below to manage this project's Caplin dependencies manually from the shipped zip file:

    1.  Extract the contents of <code>blotter-<em>version</em>.zip</code> into a destination folder of your choice.

    1.  In this project's `build.gradle` file, comment out the `maven` block for <https://repository.caplin.com>:

        ```groovy
        /*maven {
            credentials {
                username "$caplinNexusUser"
                password "$caplinNexusSecret"
            }
            url "https://repository.caplin.com"
        }*/
        ```

       and add the following block instead, but remember to replace the `<path-to-directory>` with the path to the directory you extracted in the previous step:
        ```groovy
        maven {
           url = uri("file://<path-to-directory>")
        }
        ```

    1.  Still within the `build.gradle` file, locate the `implementation(group: 'com.caplin.platform.integration.java', name: 'blotter', version: '8.0.+')` line under the `dependencies` block. Modify the version parameter to match the exact version of the zip file you possess (for example, `8.0.1-1186-d5c663d9`).

1.  Alternatively, you can also manually insert the individual jar dependencies into the `lib` folder

    1.  In this project's `build.gradle` file, comment out the `maven` block for <https://repository.caplin.com>:

        ```groovy
        /*maven {
            credentials {
                username "$caplinNexusUser"
                password "$caplinNexusSecret"
            }
            url "https://repository.caplin.com"
        }*/
        ```
        
    1.  In this project's `build.gradle` file, uncomment the `implementation fileTree(...)` line in the `dependencies` block:

        ```groovy
        dependencies {
            implementation fileTree(dir: 'lib', include: '*.jar')
            ...
        }
        ```

    1.  Copy the following Caplin libraries to this project's `lib` directory:

        *   Java Blotter API 8.0.x: <code>BlotterJava-<em>version</em>.jar</code>


### Import your new project into an IDE
Follow the instructions below to import your new adapter project into Eclipse or IntelliJ IDEA.

#### Eclipse
These instructions require the Buildship Gradle Integration plugin. To install the plugin, click **Help > Eclipse Marketplace** and search for `Buildship`.

To import your project into Eclipse, follow the steps below:

1.  In Eclipse, click **File > Import**. The Import dialog appears.

1.  Click **Existing Gradle Project**. The Import Gradle Project dialog appears.

1.  Under **Project location**, deselect **Use default location**.

1.  In the **Location** field, select your adapter's project directory: `~/src/MyBlotterAdapter`

1.  Click **Finish**.

#### IntelliJ IDEA

To import your project into IntelliJ IDEA, follow the steps below:

1.  Click **File > New > Project from existing sources**

1.  Select the project's Gradle build file: `~/src/MyBlotterAdapter/build.gradle`


## Running your adapter within an IDE

Integration adapters are designed to run within the context of a [Deployment Framework](http://www.caplin.com/developer/caplin-platform/deployment-framework/) (DFW), but they can be configured to run within an IDE during their development. This saves you time and allows you to take advantage of your IDE's debugging tools.

Regardless of whether your adapter is running in your IDE or in a DFW, it needs a working directory and the configuration details of the Liberator or Transformer it  connects to. How these requirements are met within an IDE depends on whether the Liberator or Transformer is local or remote to your development machine.


### Running your adapter with a local Liberator or Transformer

This section describes how to connect an adapter in an IDE to a Liberator or Transformer in a [Deployment Framework](http://www.caplin.com/developer/caplin-platform/deployment-framework/) (DFW) on your local machine.

To provide Liberator or Transformer with your adapter's configuration, follow the steps below:

1.  From the root of your project, run `./gradlew assemble -PconfigOnly`. This command packages your adapter's configuration (but not the binary) within a [config-only blade](http://www.caplin.com/developer/caplin-platform/deployment-framework/cdf-blade-types#config-blade) under `build/distributions/`.

1.  Copy the config-only blade to the `kits` directory of your local DFW.

1.  From the root of the local DFW, run the command `./dfw deploy` to deploy the config-only blade.

1.  From the root of the local DFW, run the command `./dfw versions` to confirm that the config blade has been deployed.

**Note**: if you change your adapter's configuration, you must repeat the steps above.

To provide your adapter with a working directory and the configuration of the Liberator or Transformer it connects to, follow the steps below:

1.  In your IDE, create a run configuration for the main class of your project:

    1.  Set the run configuration's working directory to <code><em>dfw_location</em>/active_blades/<em>adapter_name</em>/DataSource</code>, where <code><em>dfw_location</em></code> is the path to your local DFW, and <code><em>adapter_name</em></code> is the name of your adapter.
    
        **Note**: on Microsoft Windows, which does not recognise Unix-style symbolic links, use the path <code><em>dfw_location</em>\\kits\\<em>adapter_name</em>\\<em>adapter_name-version</em>\\DataSource</code>

    1.  Create a run-configuration environment variable `CONFIG_BASE` with the value <code><em>dfw_location</em>/global_config/</code>, where <code><em>dfw_location</em></code> is the path to your local DFW. This provides your adapter with the path to the configuration of the Liberator or Transformer it connects to.
    
        **Note**: the value of `CONFIG_BASE` must end with a trailing slash.

1.  Run the adapter using the new run configuration.


### Running your adapter with a remote Liberator or Transformer

This section describes how to connect an adapter in an IDE to a Liberator or Transformer in a [Deployment Framework](http://www.caplin.com/developer/caplin-platform/deployment-framework/) (DFW) on a remote host.

To provide Liberator or Transformer with your adapter's configuration, follow the steps below:

1.  From the root of your project, run `./gradlew assemble -PconfigOnly`. This command packages your adapter's configuration (but not the binary) within a [config-only blade](http://www.caplin.com/developer/caplin-platform/deployment-framework/cdf-blade-types#config-blade) under `build/distributions/`.

1.  Copy the config-only blade to the `kits` directory of the remote DFW.

1.  From the root of the remote DFW, run the command `./dfw deploy` to deploy the config-only blade.

1.  From the root of the remote DFW, run the command `./dfw versions` to confirm that the config blade has been deployed.

To provide your adapter with a working directory and the configuration of the Liberator or Transformer it connects to, follow the steps below:

1.  From the root of your project, run `./gradlew setupWorkingDirectory`, specifying one or more of the properties listed below.

    The `setupWorkingDirectory` task creates a minimal execution environment for your adapter under `build/env`. The environment includes a working directory and the minimal configuration required to connect to the remote Liberator or Transformer.

    The `setupWorkingDirectory` build task accepts the following [Gradle properties]( https://docs.gradle.org/current/userguide/build_environment.html#sec:gradle_properties_and_system_properties):

    *   <code><strong>-PthisLeg=<em>value</em></strong></code>: defaults to `1`. Change this if you want to connect to the [failover leg](https://www.caplin.com/developer/caplin-platform/deployment-framework/cdf-set-up-server-failover-capability#about-failover-legs).

    *   <code><strong>-PliberatorHost=<em>value</em></strong></code>: defaults to `localhost`. This must be changed to the host that Liberator runs on.

    *   <code><strong>-PliberatorDsPort=<em>value</em></strong></code>: defaults to `15001`. Change this if the Liberator uses a different DataSource port number.

    *   <code><strong>-PtransformerHost=<em>value</em></strong></code>: defaults to `localhost`. This must be changed to the host Transformer runs on.

    *   <code><strong>-PtransformerDsPort=<em>value</em></strong></code>: defaults to `15002`. Change this if the Transformer uses a different DataSource port number.

1.  Open the generated configuration file `build/env/blade_config/environment-ide.conf` and check that the configuration has been generated correctly. Make manual corrections to the file as required.

1.  In your IDE, create a run configuration with the working directory set to `build/env/DataSource`.

1.  Run the adapter using the new run configuration.

**Note**: if you change your adapter's configuration, you must repeat the steps above.

## Setting JVM options

To pass options to the Java virtual machine (JVM) that runs your adapter in your IDE, add the JVM options to the adapter's run configuration.

To pass options to the Java virtual machine (JVM) that the Deployment Framework uses to run your adapter, export environment variable **CAPLIN_BLADE_JAVA_OPTIONS**. For example, to specify that the JVM has an initial heap size of 128MB and a maximum heap size of 256MB, add `-Xms128m -Xmx256m` as options to the `java` command, as shown below:

```bash
export CAPLIN_BLADE_JAVA_OPTIONS="-Xms128m -Xmx256m"
```

The JVM heap sizes in this example are illustrative only. Profile your adapter to determine the optimal values for your use cases.

## Building and deploying the adapter blade

Follow the steps below to build and deploy your adapter.

1.  From the root of your project, run `./gradlew assemble`. This command packages your adapter in an [adapter blade](http://www.caplin.com/developer/caplin-platform/deployment-framework/cdf-blade-types#adapter-blade) under `build/distributions/`.

1.  Deploy the adapter blade to each Deployment Framework in your deployment infrastructure. For instructions on how to deploy an adapter blade to a Deployment Framework, see [Deploy a custom blade](https://www.caplin.com/developer/caplin-platform/deployment-framework/cdf-deploy-a-custom-blade).


## How to report issues with the template
To report an issue with the template, please contact Caplin Support or [raise an issue](https://github.com/caplin/project-templates/issues) on GitHub.
