# Caplin JTM Template

This project template provides a starting point for writing a Transformer 7 module using the Java Transformer Module (JTM) API [com.caplin.jtm](http://www.caplin.com/developer/api/transformer_java_sdk_2/latest).

> To create a Transformer 6 module, use the [Caplin Legacy JTM Template](https://github.com/caplin/project-templates/tree/master/jtm-legacy-template).

This template is a [Gradle](https://gradle.org/) project. To avoid compatibility issues between the version of Gradle required by the project and the version of Gradle installed on your system, always run the project's Gradle tasks using the Gradle Wrapper from the root of the project: <code>./gradlew <em>task</em></code>

**Note**: the Gradle Wrapper requires an Internet connection. For more information, see [Executing a build with the Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html#using_wrapper_scripts) in the Gradle documentation.


## Getting started

Follow the instructions below to create a new Transformer module project based on the Legacy JTM Template.

### Copy and customise the template

1.  Clone, or download and extract the latest version of the Caplin Project Templates repository:

    *   `wget http://github.com/caplin/project-templates/archive/master.zip`

        `unzip -qoa master.zip`

    *   `git clone https://github.com/caplin/project-templates.git`

1.  Copy the template directory `jtm-template` and rename it to the name of your new project (for example, MyTransformerModule):

    ```bash
    cp -r ./jtm-template ~/src/MyTransformerModule
    ```

1.  Edit the file `~/src/MyTransformerModule/settings.gradle`, and change the value of the `rootProject.name` variable to the name of your project (MyTransformerModule). When you later export your project as an blade, the project name will be used as the name for the blade.

1.  Choose one of the options below to satisfy the project's dependencies:

    *   If you have Internet access to <https://repository.caplin.com> and a Caplin website account, add your Caplin account credentials to your `~/.gradle/gradle.properties` file:

        ```
        caplinNexusUser=<username>
        caplinNexusSecret=<password>
        ```

        Gradle will download the project's dependencies automatically from <https://repository.caplin.com>.

    *   If you don't have Internet access to <https://repository.caplin.com>, copy the following Caplin libraries to the project's `lib` directory:

        *   Transformer: <code>JTM-<em>version</em>.jar</code> (found in the `lib/java` directory inside the Transformer installation kit)


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
