# Caplin Project Templates

The Caplin Project Templates are set of template Java projects to get you started in creating the following types of integration adapters and [Transformer](https://www.caplin.com/developer/component/transformer) modules:

| Template | Directory |
|--------------------|---------|
| Pricing Adapter Template | [./pricing-template](https://github.com/caplin/project-templates/tree/master/pricing-template) |
| Trading Adapter Template | [./trading-template](https://github.com/caplin/project-templates/tree/master/trading-template) |
| Permissioning Adapter Template | [./permissioning-template](https://github.com/caplin/project-templates/tree/master/permissioning-template) |
| Notification Adapter Template | [./notification-template](https://github.com/caplin/project-templates/tree/master/notification-template) |
| Blotter Adapter Template | [./blotter-template](https://github.com/caplin/project-templates/tree/master/blotter-template) |
| Java Transformer Module (Tranformer 6) | [./jtm-legacy-template](https://github.com/caplin/project-templates/tree/master/jtm-legacy-template) |
| Java Transformer Module (Transformer 7) | [./jtm-template](https://github.com/caplin/project-templates/tree/master/jtm-template)

Each template is a [Gradle](https://gradle.org/) project that comprises:

* Source code and configuration files to build an example adapter or Transformer module.
* Gradle tasks to export your project as a Caplin Platform blade.

## Instructions

At a high-level, the instructions for each template follow the tasks below:

1. Clone this repository, or download it as a ZIP file and extract it.
1. Make a copy of a template and customise it:
    1. Set the project name.
    1. Add dependencies to the template's `lib` directory, or configure Gradle to download dependencies automatically from the Caplin software repository (coming soon).
1. Import your customised copy into your IDE as a new project.
1. [optional] Import your project into your own version control system.

For detailed instructions, see the `README.md` file inside each template directory.
