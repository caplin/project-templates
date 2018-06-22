# Caplin Pricing Adapter Template for DotNet

This template project provides a starting point for writing pricing integration adapters based on Caplin's [C# DataSource API](https://www.caplin.com/developer/api/datasource_dotnet/latest).

## Getting started

Follow the instructions below to create a new adapter project based on the Pricing Adapter Template for DotNet.

### Copy and customise the template

1.  Clone, or download and extract the latest version of the Caplin Project Templates repository:

    *   `wget http://github.com/caplin/project-templates/archive/master.zip`

        `unzip -qoa master.zip`

    *   `git clone https://github.com/caplin/project-templates.git`

1.  Copy the template directory `pricing-template` and rename it to the name of your new project (for example, MyPricingAdapter):

    ```bash
    cp -r ./pricing-template-dotnet ~/src/MyPricingAdapter
    ```

1.  Edit the file `~/src/MyPricingAdapter/settings.gradle`, and change the value of the `rootProject.name` variable to the name of your adapter project (MyPricingAdapter). When you later export your project as an [adapter blade](http://www.caplin.com/developer/caplin-platform/deployment-framework/cdf-blade-types#adapter-blade), the project name will be used as the name for the blade.

1.  Edit the file `~/src/MyPricingAdapter/blade/blade_config/bootstrap.conf`. Set the value of the configuration variable `ROUTE_VIA_TRANSFORMER` to `TRUE` (default) to configure Liberator to route requests to the adapter via Transformer or `FALSE` to configure Liberator to route requests directly to the adapter.

    **Note**: to route trade messages to the adapter via Transformer requires Transformer version 7.0.3 or later.

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

    1.  In this project's `build.gradle` add artifactory as a dependency source :

        ```groovy
        repositories {
    		mavenCentral()
    		maven { url "http://artifactory.caplin.com:8081/artifactory/caplin-qa" }
	}```



### Import your new project into Visual Studio

To import your project into Visual Studio, follow the steps below:

1.  In VS, click **File > Open > Project/Solution**. The File Manager should appear.

1.  Navigate to the DotNet Template in your project templates, Go into **Project**.

1.  Select the **TemplatePricingAdapter.csproj**.

1.  Click **Open**.

### Reference the DataSource.NET In your solution

You will need to link your DSDK dll to use the IDE to build/run your adapter without gradle, to do this follow the steps below:

1. Download the latest DotNetDSDK from the Caplin Website: https://caplin.com/developer/downloads

1. Create a directory in your **Project** folder for the DSDK E.G. Project/Lib/

1. Extract your DataSource.NET to your new directory

1. From within Visual Studio, in the Solution Explorer right click the **References** icon, and select **Add Reference...**

1. Click the **Browse** option and navigate to your new lib directory where you have extracted the DataSource.NET kit

1. Include the DataSource.NET.dll and Click **Ok**

## Building and deploying the adapter blade

Follow the steps below to build and deploy your adapter.

1.  From the root of your project, run `./gradlew assemble`. This command packages your adapter in an [adapter blade](http://www.caplin.com/developer/caplin-platform/deployment-framework/cdf-blade-types#adapter-blade) under `build/distributions/`.

1.  Deploy the adapter blade to each Deployment Framework in your deployment infrastructure. For instructions on how to deploy an adapter blade to a Deployment Framework, see [Deploy a custom blade](https://www.caplin.com/developer/caplin-platform/deployment-framework/cdf-deploy-a-custom-blade).


### Running your adapter with a local Liberator or Transformer

This section describes how to connect an adapter in an IDE to a Liberator or Transformer in a [Deployment Framework](http://www.caplin.com/developer/caplin-platform/deployment-framework) (DFW) on your local machine.

To provide Liberator or Transformer with your adapter's configuration, follow the steps below:

1.  From the root of your project, run `./gradlew assemble -PconfigOnly`. This command packages your adapter's configuration (but not the binary) within a [config-only blade](http://www.caplin.com/developer/caplin-platform/deployment-framework/cdf-blade-types#config-blade) under `build/distributions/`.

1.  Copy the config-only blade to the `kits` directory of your local DFW.

1.  From the root of the local DFW, run the command `./dfw deploy` to deploy the config-only blade.

1.  From the root of the local DFW, run the command `./dfw versions` to confirm that the config blade has been deployed.

**Note**: if you change your adapter's configuration, you must repeat the steps above.

To provide your adapter with a working directory and the configuration of the Liberator or Transformer it connects to, follow the steps below:

1.  In your Solution Explorer, right click your soluton Properties > Debug :

    1.  Set the run configuration's working directory to <code><em>dfw_location</em>/active_blades/<em>adapter_name</em>/DataSource</code>, where <code><em>dfw_location</em></code> is the path to your local DFW, and <code><em>adapter_name</em></code> is the name of your adapter.

        **Note**: on Microsoft Windows, which does not recognise Unix-style symbolic links, use the path <code><em>dfw_location</em>\\kits\\<em>adapter_name</em>\\<em>adapter_name-version</em>\\DataSource</code>

    1.  In the soltuon explorer, open your app.config file and Create an XML run-configuration environment variable `CONFIG_BASE` with the value <code><em>dfw_location</em>/global_config/</code>, where <code><em>dfw_location</em></code> is the path to your local DFW. This provides your adapter with the path to the configuration of the Liberator or Transformer it connects to. Your app config should look something similar to the following:


```<?xml version="1.0" encoding="utf-8"?>
<configuration>
	<startup><supportedRuntime version="v4.0" sku=".NETFramework,Version=v4.6"/>
  		<environmentVariables>
    			<environmentVariable name="CONFIG_BASE" value ="C:/users/Desktop/"/>
  		</environmentVariables>
	</startup>
</configuration>
```


        **Note**: the value of `CONFIG_BASE` must end with a trailing slash.

1.  Run the adapter using the new run configuration.


### Running your adapter with a remote Liberator or Transformer

This section describes how to connect an adapter in an IDE to a Liberator or Transformer in a [Deployment Framework](http://www.caplin.com/developer/caplin-platform/deployment-framework) (DFW) on a remote host.

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

## How to report issues with the template
To report an issue with the template, please contact Caplin Support or [raise an issue](https://github.com/caplin/project-templates/issues) on GitHub.
