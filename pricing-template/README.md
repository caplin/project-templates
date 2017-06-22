# Caplin Pricing Adapter Template

This template project provides a starting point for writing pricing integration adapters based on Caplin's Java [DataSource API](http://www.caplin.com/developer/component/datasource).

This template is a [Gradle](https://gradle.org/) project. To avoid compatibility issues between the version of Gradle required by the project and the version of Gradle installed on your system, always run the project's Gradle tasks using the Gradle Wrapper from the root of the project: <code>./gradlew <em>task</em></code>

**Note**: the Gradle Wrapper requires an Internet connection. For more information, see [Executing a build with the Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html#using_wrapper_scripts) in the Gradle documentation.

## Getting started

Follow the instructions below to create a new adapter project based on the Pricing Adapter Template.

### Copy and customise the template

1. Clone, or download and extract the latest version of the Caplin Project Templates repository:

    * `wget http://github.com/caplin/project-templates/archive/master.zip`

        `unzip -qoa master.zip`

    * `git clone https://github.com/caplin/project-templates.git`

1. Copy the template directory `pricing-template` and rename it to the name of your new project (for example, MyPricingAdapter):

    ```bash
    cp -r ./pricing-template ~/src/MyPricingAdapter
    ```

1. Edit the file `~/src/MyPricingAdapter/settings.gradle`, and change the value of the `rootProject.name` variable to the name of your adapter project (MyPricingAdapter). When you later export your project as an [adapter blade](http://www.caplin.com/developer/component/deployment-framework/features-and-concepts/cdf-blade-types#Adapter-blade), the project name will be used as the name for the blade.

1. Edit the file `~/src/MyPricingAdapter/blade/blade_config/bootstrap.conf`. Set the value of the configuration variable `ROUTE_VIA_TRANSFORMER` to `TRUE` (default) to configure Liberator to route requests to the adapter via Transformer or `FALSE` to configure Liberator to route requests directly to the adapter.

    **Note**: to route trade messages to the adapter via Transformer requires Transformer version 7.0.3 or later.

1. Supply your project's dependencies manually, or configure Gradle to download them automatically from the Caplin software repository (coming soon).

    * **Manual download**: copy the latest versions of the following Caplin software libraries to the `~/src/MyPricingAdapter/lib` directory:

        * <code>datasource-java-<em>version</em>-jar-with-dependencies.jar</code>

    * **Caplin software repository**: (coming soon.) Edit the file `~/src/MyPricingAdapter/build.gradle`, and supply the credentials to your Caplin account in the `repositories` section.
    

### Import your new project into an IDE
Follow the instructions below to import your new adapter project into Eclipse or IntelliJ IDEA.

#### Eclipse
These instructions require the Buildship Gradle Integration plugin. To install the plugin, click **Help > Eclipse Marketplace** and search for `Buildship`.

To import your project into Eclipse, follow the steps below:

1. In Eclipse, click **File > Import**. The Import dialog appears.

1. Click **Existing Gradle Project**. The Import Gradle Project dialog appears.

1. Under **Project location**, deselect **Use default location**.

1. In the **Location** field, select your adapter's project directory: `~/src/MyPricingAdapter`

1. Click **Finish**.

#### IntelliJ IDEA

To import your project into IntelliJ IDEA, follow the steps below:

1. Click **File > New > Project from existing sources**

1. Select the project's Gradle build file: `~/src/MyPricingAdapter/build.gradle`

## Running your adapter within an IDE

Integration adapters are designed to run within the context of a [Deployment Framework](http://www.caplin.com/developer/component/deployment-framework) (DFW), but they can be configured to run within an IDE during their development. This saves you time and allows you to take advantage of your IDE's debugging tools.

Regardless of whether your adapter is running in your IDE or in a DFW, it needs a working directory and the configuration details of the Liberator or Transformer it  connects to. How these requirements are met within an IDE depends on whether the Liberator or Transformer is local or remote to your development machine.


### Running your adapter with a local Liberator or Transformer

This section describes how to connect an adapter in an IDE to a Liberator or Transformer in a [Deployment Framework](http://www.caplin.com/developer/component/deployment-framework) (DFW) on your local machine.

To provide Liberator or Transformer with your adapter's configuration, follow the steps below:

1. From the root of your project, run `./gradlew assemble -PconfigOnly`. This command packages your adapter's configuration (but not the binary) within a [config-only blade](http://www.caplin.com/developer/component/deployment-framework/features-and-concepts/cdf-blade-types#Config-blade) under `build/distributions/`.

1. Copy the config-only blade to the `kits` directory of your local DFW.

1. From the root of the local DFW, run the command `./dfw deploy` to deploy the config-only blade.

1. From the root of the local DFW, run the command `./dfw versions` to confirm that the config blade has been deployed.

**Note**: if you change your adapter's configuration, you must repeat the steps above.

To provide your adapter with a working directory and the configuration of the Liberator or Transformer it connects to, follow the steps below:

1. In your IDE, create a run configuration for the main class of your project:

    1. Set the run configuration's working directory to <code><em>dfw_location</em>/active_blades/<em>adapter_name</em>/DataSource</code>, where <code><em>dfw_location</em></code> is the path to your local DFW, and <code><em>adapter_name</em></code> is the name of your adapter.

        **Note**: on Microsoft Windows, which does not recognise Unix-style symbolic links, use the path <code><em>dfw_location</em>\\kits\\<em>adapter_name</em>\\<em>adapter_name-version</em>\\DataSource</code>

    1. Create a run-configuration environment variable `CONFIG_BASE` with the value <code><em>dfw_location</em>/global_config/</code>, where <code><em>dfw_location</em></code> is the path to your local DFW. This provides your adapter with the path to the configuration of the Liberator or Transformer it connects to.
    
        **Note**: the value of `CONFIG_BASE` must end with a trailing slash.

1. Run the adapter using the new run configuration.


### Running your adapter with a remote Liberator or Transformer

This section describes how to connect an adapter in an IDE to a Liberator or Transformer in a [Deployment Framework](http://www.caplin.com/developer/component/deployment-framework) (DFW) on a remote host.

To provide Liberator or Transformer with your adapter's configuration, follow the steps below:

1. From the root of your project, run `./gradlew assemble -PconfigOnly`. This command packages your adapter's configuration (but not the binary) within a [config-only blade](http://www.caplin.com/developer/component/deployment-framework/features-and-concepts/cdf-blade-types#Config-blade) under `build/distributions/`.

1. Copy the config-only blade to the `kits` directory of the remote DFW.

1. From the root of the remote DFW, run the command `./dfw deploy` to deploy the config-only blade.

1. From the root of the remote DFW, run the command `./dfw versions` to confirm that the config blade has been deployed.

To provide your adapter with a working directory and the configuration of the Liberator or Transformer it connects to, follow the steps below:

1. From the root of your project, run `./gradlew setupWorkingDirectory`, specifying one or more of the properties listed below.

    The `setupWorkingDirectory` task creates a minimal execution environment for your adapter under `build/env`. The environment includes a working directory and the minimal configuration required to connect to the remote Liberator or Transformer.

    The `setupWorkingDirectory` build task accepts the following [Gradle properties]( https://docs.gradle.org/current/userguide/build_environment.html#sec:gradle_properties_and_system_properties):

    * <code><strong>-PthisLeg=<em>value</em></strong></code>: defaults to `1`. Change this if you want to connect to the [failover leg](https://www.caplin.com/developer/component/deployment-framework/how-can-i/cdf-set-up-server-failover-capability#About-failover-legs).

    * <code><strong>-PliberatorHost=<em>value</em></strong></code>: defaults to `localhost`. This must be changed to the host that Liberator runs on.

    * <code><strong>-PliberatorDsPort=<em>value</em></strong></code>: defaults to `15001`. Change this if the Liberator uses a different DataSource port number.

    * <code><strong>-PtransformerHost=<em>value</em></strong></code>: defaults to `localhost`. This must be changed to the host Transformer runs on.

    * <code><strong>-PtransformerDsPort=<em>value</em></strong></code>: defaults to `15002`. Change this if the Transformer uses a different DataSource port number.

1. Open the generated configuration file `build/env/blade_config/environment-ide.conf` and check that the configuration has been generated correctly. Make manual corrections to the file as required.

1. In your IDE, create a run configuration with the working directory set to `build/env/DataSource`.

1. Run the adapter using the new run configuration.

**Note**: if you change your adapter's configuration, you must repeat the steps above.


## Setting JVM options

To pass options to the Java virtual machine (JVM) that runs your adapter in your IDE, add the JVM options to the adapter's run configuration.

To pass options to the Java virtual machine (JVM) that the Deployment Framework uses to run your adapter, edit the project file `blade/DataSource/bin/start-jar.sh`. Add the JVM options to the `java` command in the `else` block of the conditional below:

```bash
if [ $confreading = 1 ]; then
   java -jar "$jar" "$@"
   exit $?
else
   java -cp "$classpath" -jar "$jar" "$@" 2> "$LOGDIR"/java-$BLADENAME.log > /dev/null &
   echo $!
fi
```

For example, to specify that the JVM has an initial heap size of 128MB and a maximum heap size of 256MB, add `-Xms128m -Xmx256m` as options to the `java` command, as shown below:

```bash
java -Xms128m -Xmx256m -cp "$classpath" -jar "$jar" "$@" 2> "$LOGDIR"/java-$BLADENAME.log > /dev/null &
```

**Note**: The JVM heap sizes in this example are illustrative only. Profile your adapter to determine the optimal values for your use cases.

## Building and deploying the adapter blade

Follow the steps below to build and deploy your adapter.

1. From the root of your project, run `./gradlew assemble`. This command packages your adapter in an [adapter blade](http://www.caplin.com/developer/component/deployment-framework/features-and-concepts/cdf-blade-types#Adapter-blade) under `build/distributions/`.

1. Deploy the adapter blade to each Deployment Framework in your deployment infrastructure. For instructions on how to deploy an adapter blade to a Deployment Framework, see [Deploy a custom blade](https://caplinportal.caplin.com/developer/component/deployment-framework/how-can-i/cdf-deploy-a-custom-blade).


## How to report issues with the template
To report an issue with the template, please contact Caplin Support or [raise an issue](https://github.com/caplin/project-templates/issues) on GitHub.
