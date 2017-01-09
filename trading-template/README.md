# Caplin Trading Adapter Template

This project provides a starting point for writing trading integration Adapters based on the Caplin DataSource Java API and the Caplin Trading DataSource API. The build is written in gradle and requires either a local installation of gradle or an internet connection over which gradle can be downloaded. This is done seamlessly with the provided `gradlew` script files.

## Getting started
This section outlines the basic steps necessary to build, deploy and start the Adapter.

1. download/clone the repository
2. change the project name in settings.gradle. This will be used as the name for the artifact.
3. change the username and password in the `build.gradle` file to your caplin credentials
4. run `gradle assemble`
5. deploy the zip file created in `build/distributions/` into your Deployment Framework
6. configure the host for the deployed blade with the `./dfw hosts` command
7. start the adapter with `./dfw start <adapterName>`


## Development modes
In addition to creating an adapter blade that can be deployed using the Caplin Deployment Framework the build has two more tasks that make it easier to run the adapter from an IDE. The two modes modes are **run against a local DFW** and **run against a remote DFW** and are explained in more detail in the following two sections.

### Connect an adapter from an IDE to a local DFW
In order for the Liberator and Transformer to know abou the blade they need the configuration bundled with this blade. The concept of a **config only blade** is essentially a blade that contains only configuration and no binary. When this blade is deployed, the difference to a full blade is that `./dfw start` will not start up the adapter. At this point the integration adapter can be started from the IDE. The following bullet points outline the recommended way of setting this up.

1. run `gradle assemble -PconfigOnly`
2. deploy the zip file created in `build/distributions/` into your Deployment Framework
3. create a run configuration for the main class of your project
 * set the environment variable `CONFIG_BASE` to point to the `global_config/` folder in your DFW
 * set the working directory to `<dfw location>/active_blades/<AdapterName>/DataSource `
4. Run the adapter using the run configuration just created

### Connect an adapter from an IDE to a remote DFW
Sometimes, the Liberator and Transformer will not be on the host that is being developed on. In this case the above approach will not work. To solve this issue the build has a task `createWorkingDirectory` that generates necessary (limited) configuration to allow the adapter to connect to a remote host. The following steps are required to set this up.

1. run `gradle assemble -PconfigOnly`
2. deploy the zip file created in `build/distributions/` into your Deployment Framework (remote host)
3. run `gradle setupWorkingDirectory` which will create the folder `build/env` for use as a working directory. This will also require some default properties to be overridden with the -P command line switch. See the [Gradle Documentation]( https://docs.gradle.org/current/userguide/build_environment.html#sec:gradle_properties_and_system_properties). Supported options are:
 * thisLeg - defaults to `1` and only needs to be changed if you want to connect to the failover leg
 * liberatorHost - defaults to `localhost` and will need to be changed to the host Liberator runs on
 * liberatorDsPort - defaults to `15001` and might need to be changed to the Liberator datasrc port
 * transformerHost - defaults to `localhost` and will need to be changed to the host Transformer runs on
 * transformerDsPort - defaults to `15002` and might need to be changed to the Transformer datasrc port
4. Once the working directory has been created check the generated file `build/env/blade_config/environment-ide.conf` for correctnes of generated arguments
5. Create a run configuration with the directory `build/env/DataSource` as a working directory and `--trading-property-file=etc/trading-provider.properties` as program arguments
6. Start the Adapter using the run configuration just created

### Testing the Adapter
The template is very limited and does not really contain any valid business logic, but for illustration purposes a simple Trade model is included. Once the adapter is started and connected to Liberator, the following steps can be followed to make sure the template is connected and working.

1. Open the Liberator status page, typically `http://localhost:18080/status/` and check that the TemplateAdapter is shown as UP
2. Open the Liberator explorer, typically `http://localhost:18080/diagnostics/liberatorexplorer/index.html` and request the subject `/TEMPLATE/TRADE`
 * at this point the Trading adapter should receive a `Trade created` event and return an empty record to the request
3. Send a contrib with fields `{ MsgType=Open, TradingProtocol=ESP, RequestID=1, Price=1.234 }` to the subject `/TEMPLATE/TRADE`
 * this causes the trading library to transition from the state `Initial` to the state `Executing` via the Trigger `Open` sent in the contrib
 * the template implementation is to assume this is everything that a trade needs and it immediately sends back a `Confirm` message which completes the tarde defined by the state machine in `DataSource/etc/trademodels.xml`


## Issues
For issues with the templates please contact Caplin Support or raise an issue on Github.
