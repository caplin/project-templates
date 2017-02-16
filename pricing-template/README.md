# Caplin Pricing Adapter Template

This project provides a starting point for writing pricing integration adapters based on Caplin's Java [DataSource API](http://www.caplin.com/developer/component/datasource).

The build script, `gradle.build`, requires a local installation of [Gradle](https://gradle.org/). Alternatively, you can run the build commands using the provided Gradle Wrapper, `gradlew`. For more information on using a Gradle Wrapper, see [Executing a build with the Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html#using_wrapper_scripts) in the Gradle documentation.


## Getting started

Follow the instructions below to download and configure a Pricing Adapter Template.

1. Download or clone the Caplin Project Templates repository.

1. In the `pricing-template/settings.gradle` file, change the value of the `rootProject.name` property to the name of your adapter project. The project name will be used as the name for the [adapter blade](http://www.caplin.com/developer/component/deployment-framework/features-and-concepts/cdf-blade-types#Adapter-blade).

1. In the `pricing-template/build.gradle` file, change the username and password to your Caplin credentials.

The Pricing Adapter Template does not include the libraries required to build a Caplin Platform adapter. These libraries are downloaded when you build the adapter for the first time or when you import the template directory to an IDE with Gradle support.


## Running your adapter within an IDE

Integration adapters are designed to run within the context of a [Deployment Framework](http://www.caplin.com/developer/component/deployment-framework) (DFW), but they can be configured to run within an IDE during their development. This saves you time and allows you to take advantage of your IDE's debugging tools.

Regardless of whether your adapter is running in your IDE or in a DFW, it needs a working directory and the configuration details of the Liberator or Transformer it  connects to. How these requirements are met within an IDE depends on whether the Liberator or Transformer is local or remote to your development machine.


### Running your adapter with a local Liberator or Transformer

This section describes how to connect an adapter in an IDE to a Liberator or Transformer in a [Deployment Framework](http://www.caplin.com/developer/component/deployment-framework) (DFW) on your local machine.

To provide Liberator or Transformer with your adapter's configuration, follow the steps below:

1. From the pricing template root, run `gradle assemble -PconfigOnly`. This command packages your adapter's configuration (but not the binary) within a [config-only blade](http://www.caplin.com/developer/component/deployment-framework/features-and-concepts/cdf-blade-types#Config-blade) under `build/distributions/`.

1. Copy the config-only blade to the `kits` directory of your local DFW.

1. From the root of the local DFW, run the command `./dfw deploy` to deploy the config-only blade.

1. From the root of the local DFW, run the command `./dfw versions` to confirm that the config blade has been deployed.

**Note**: if you change your adapter's configuration, you must repeat the steps above.

To provide your adapter with a working directory and the configuration of the Liberator or Transformer it connects to, follow the steps below:

1. In your IDE, create a run configuration for the main class of your project:

    1. Set the run configuration's working directory to <code><em>dfw_location</em>/activate_blades/<em>adapter_name</em>/DataSource</code>, where <code><em>dfw_location</em></code> is the path to your local DFW, and <code><em>adapter_name</em></code> is the name of your adapter.

    1. Create a run-configuration environment variable `CONFIG_BASE` with the value <code><em>dfw_location</em>/global_config/</code>, where <code><em>dfw_location</em></code> is the path to your local DFW. This provides your adapter with the path to the configuration of the Liberator or Transformer it connects to.

1. Run the adapter using the new run configuration.


### Running your adapter with a remote Liberator or Transformer

This section describes how to connect an adapter in an IDE to a Liberator or Transformer in a [Deployment Framework](http://www.caplin.com/developer/component/deployment-framework) (DFW) on a remote host.

To provide Liberator or Transformer with your adapter's configuration, follow the steps below:

1. From the pricing template root, run `gradle assemble -PconfigOnly`. This command packages your adapter's configuration (but not the binary) within a [config-only blade](http://www.caplin.com/developer/component/deployment-framework/features-and-concepts/cdf-blade-types#Config-blade) under `build/distributions/`.

1. Copy the config-only blade to the `kits` directory of the remote DFW.

1. From the root of the remote DFW, run the command `./dfw deploy` to deploy the config-only blade.

1. From the root of the remote DFW, run the command `./dfw versions` to confirm that the config blade has been deployed.

**Note**: if you change your adapter's configuration, you must repeat the steps above.

To provide your adapter with a working directory and the configuration of the Liberator or Transformer it connects to, follow the steps below:

1. From the pricing template root, run `gradle setupWorkingDirectory`, specifying one or more of the properties listed below.

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


## Setting JVM options

To pass options to the Java virtual machine (JVM) that runs your adapter in your IDE, add the JVM options to the adapter's run configuration.

To pass options to the Java virtual machine (JVM) that the Deployment Framework uses to run your adapter, edit the file `pricing-template/blade/DataSource/bin/start-jar.sh`. Add the JVM options to the `java` command in the `else` block of the conditional below:

```bash
if [ $confreading = 1 ]; then
   java -jar "$jar" "$@"
   exit $?
else
   java -cp "$classpath" -jar "$jar" "$@" > "$LOGDIR"/java-$BLADENAME.log 2>&1 &
   echo $!
fi
```

For example, to specify that the JVM has an initial heap size of 128MB and a maximum heap size of 256MB, add `-Xms128m -Xmx256m` as options to the `java` command, as shown below:

```bash
java -Xms128m -Xmx256m -cp "$classpath" -jar "$jar" "$@" > "$LOGDIR"/java-$BLADENAME.log 2>&1 &
```

**Note**: The JVM heap sizes in this example are illustrative only. Profile your adapter to determine the optimal values for your use cases.

## Building and deploying the adapter blade

Follow the steps below to build and deploy your adapter.

1. From the root of the project template, run `gradle assemble`. This command packages your adapter in an [adapter blade](http://www.caplin.com/developer/component/deployment-framework/features-and-concepts/cdf-blade-types#Adapter-blade) under `build/distributions/`.

1. Deploy the adapter blade to each Deployment Framework in your deployment infrastructure. For instructions on how to deploy an adapter blade to a Deployment Framework, see [Deploy a custom blade](https://caplinportal.caplin.com/developer/component/deployment-framework/how-can-i/cdf-deploy-a-custom-blade).


## How to report issues with the template
To report an issue with the template, please contact Caplin Support or [raise an issue](https://github.com/caplin/project-templates/issues) on GitHub.
