# Caplin Legacy JTM Template

This project template provides a starting point for writing a Transformer 6 module using the now deprecated Java Transformer Module (JTM) API [com.caplin.transformer.module](http://www.caplin.com/developer/api/transformer_java_sdk/latest).

> To create a Transformer 8 module, use the [Caplin JTM Template](https://github.com/caplin/project-templates/tree/master/jtm-template).

This template is a [Gradle](https://gradle.org/) project. To avoid compatibility issues between the version of Gradle required by the project and the version of Gradle installed on your system, always run the project's Gradle tasks using the Gradle Wrapper from the root of the project: <code>./gradlew <em>task</em></code>

**Note**: the Gradle Wrapper requires an Internet connection. For more information, see [Executing a build with the Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html#using_wrapper_scripts) in the Gradle documentation.


## Getting started

Follow the instructions below to create a new Transformer module project based on the Legacy JTM Template.

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

1.   Copy the template directory `jtm-legacy-template` and rename it to the name of your new project (for example, MyTransformerModule):

    ```
    $ cp -r ./jtm-legacy-template ~/src/MyTransformerModule
    ```

1.   Edit the file `~/src/MyTransformerModule/settings.gradle`, and change the value of the `rootProject.name` variable to the name of your project (MyTransformerModule). When you later export your project as an blade, the project name will be used as the name for the blade.

1.  If you have a Caplin website account and Internet access to <https://repository.caplin.com>, follow the steps below to enable automatic downloading of this project's Caplin dependencies:

    1.  In your `~/.gradle/gradle.properties` file (create it if it does not exist), add the following lines, replacing `<username>` and `<password>` with your Caplin credentials:

        ```
        caplinNexusUser=<username>
        caplinNexusSecret=<password>
        ```

1.  If you _don't_ have a Caplin website account and Internet access to <https://repository.caplin.com>, follow the steps below to manage this project's Caplin dependencies manually:

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

        *   Transformer 8.0.x: <code>TransformerModule-<em>version</em>.jar</code> (found in the `lib/java` directory inside the Transformer installation kit)



### Import your new project into an IDE
Follow the instructions below to import your new adapter project into Eclipse or IntelliJ IDEA.

#### Eclipse
These instructions require the Buildship Gradle Integration plugin. To install the plugin, click **Help > Eclipse Marketplace** and search for `Buildship`.

To import your project into Eclipse, follow the steps below:

1.  In Eclipse, click **File > Import**. The Import dialog appears.

1.  Click **Existing Gradle Project**. The Import Gradle Project dialog appears.

1.  Under **Project location**, deselect **Use default location**.

1.  In the **Location** field, select your adapter's project directory: `~/src/MyTransformerModule`

1.  Click **Finish**.

#### IntelliJ IDEA

To import your project into IntelliJ IDEA, follow the steps below:

1.  Click **File > New > Project from existing sources**

1.  Select the project's Gradle build file: `~/src/MyTransformerModule/build.gradle`


## Running your JTM during development
A Java Transformer Module (JTM) can be run only within Transformer's embedded JVM; it cannot be run within your IDE. To run and debug your JTM during development, deploy your JTM to the [Deployment Framework](http://www.caplin.com/developer/caplin-platform/deployment-framework) (DFW) that hosts your Transformer, and then use remote debugging to monitor its execution.

**To setup remote debugging:**

1.  In the Deployment Framework file `global_config/overrides/servers/Transformer/etc/java.conf`, set the configuration item `TRANSFORMER_JVM_DEBUGGER_PORT` to a valid port number.

    **Note**: if your Transformer is on a remote server, check that the server's firewall does not block the port number you assign to `TRANSFORMER_JVM_DEBUGGER_PORT`.

1.  From the root of your DFW, run the command `./dfw start Transformer` to start (or restart) Transformer.

1.  In your IDE, create a 'remote debugging' configuration for your project.

    *   Set the host to Transformer's network address.

    *   Set the port to the number you assigned to `TRANSFORMER_JVM_DEBUGGER_PORT`.

**To deploy your JTM:**

1.  From the root of your project, run the command `./gradlew assemble`. This command packages your JTM within a new [service blade](http://www.caplin.com/developer/caplin-platform/deployment-framework/cdf-blade-types#service-blade) under the `build` directory.

1.  Deploy the service blade to the DFW that hosts your Transformer. For instructions on how to deploy an adapter blade to a DFW, see [Deploy a custom blade](https://www.caplin.com/developer/caplin-platform/deployment-framework/cdf-deploy-a-custom-blade).

    **Tip**: If you are deploying the service blade to a local DFW, then follow this tip to skip this step in future. Replace the JAR file <code><em>dfw-root</em>/active_blades/<em>your_module</em>/Transformer/lib/java/<em>your_module</em>.jar</code> with a symlink to the JAR file generated by Gradle in the `build/distributions` directory.

1.  From the root of the DFW, run `./dfw start Transformer` to start (or restart) Transformer.

## Building and deploying the JTM's service blade

Follow the steps below to deploy your service blade:

1.  From the root of your project, run the command `./gradlew assemble`. This command packages your JTM within a new [service blade](http://www.caplin.com/developer/caplin-platform/deployment-framework/cdf-blade-types#service-blade) under the `build` directory.

1.  Deploy the service blade to each DFW in your deployment infrastructue. For instructions on how to deploy an adapter blade to a DFW, see [Deploy a custom blade](https://www.caplin.com/developer/caplin-platform/deployment-framework/cdf-deploy-a-custom-blade).


## Issues
To report an issue with the template, please contact Caplin Support or [raise an issue](https://github.com/caplin/project-templates/issues) on GitHub.
