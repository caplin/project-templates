# Caplin Project Templates

The Caplin Project Templates are set of template Java projects to get you started in creating the following types of integration adapters and [Transformer](https://www.caplin.com/developer/component/transformer) modules:

| Template | Directory |
|--------------------|---------|
| Pricing Adapter Template | [./pricing-template](./pricing-template) |
| Trading Adapter Template | [./trading-template](./trading-template) |
| Permissioning Adapter Template | [./permissioning-template](./permissioning-template) |
| Notification Adapter Template | [./notification-template](./notification-template) |
| Blotter Adapter Template | [./blotter-template](./blotter-template) |
| Java Transformer Module (Tranformer 6) | [./jtm-legacy-template](./jtm-legacy-template) |
| Java Transformer Module (Transformer 7) | [./jtm-template](./jtm-template)

Each template is a [Gradle](https://gradle.org/) project that comprises:

* Source code and configuration files to build an example adapter or Transformer module.
* Gradle tasks to export your project as a Caplin Platform blade.

## Instructions

At a high-level, the instructions for using each template are as follows:

1. Clone this repository, or download it as a ZIP file and extract it.
1. Make a copy of a template and customise it:
    1. Set the project name in the file `settings.gradle`.
    1. Add dependencies to the template's `lib` directory, or configure Gradle to download dependencies automatically from the Caplin software repository (coming soon).
1. Import your customised template into your IDE as a new project.
1. [optional] Import your project into your own version control system.

For detailed instructions, see the `README.md` file inside each template directory.
