# Caplin Project Templates

The Caplin Project Templates are a set of template projects to help you integrate your backend systems with the [Caplin Platform](https://www.caplin.com/developer/component/platform-architecture).

This repository includes the following templates:

| Template                                 | Directory                                                                                   |
|------------------------------------------|---------------------------------------------------------------------------------------------|
| Pricing Adapter Template                 | [./pricing-template](./pricing-template#caplin-pricing-adapter-template)                    |
| Trading Adapter Template                 | [./trading-template](./trading-template#caplin-trading-adapter-template)                    |
| Permissioning Adapter Template           | [./permissioning-template](./permissioning-template#caplin-permissioning-adapter-template)  |
| Notification Adapter Template            | [./notification-template](./notification-template#caplin-notification-adapter-template)     |
| Blotter Adapter Template                 | [./blotter-template](./blotter-template#caplin-blotter-adapter-template)                    |
| Java Transformer Module (Transformer 6)  | [./jtm-legacy-template](./jtm-legacy-template#caplin-legacy-jtm-template)                   |
| Java Transformer Module (Transformer 7)  | [./jtm-template](./jtm-template#caplin-jtm-template)                                        |

Each template is a [Gradle](https://gradle.org/) project that comprises:

* Source code and configuration files to build an example adapter or Transformer module.
* Gradle tasks to export your project as a Caplin Platform blade.

## Requirements

Caplin Project Templates have the following requirements:

* Oracle Java 8 64-bit
* An IDE or development environment with Gradle support
* Caplin software libraries

Caplin software libraries are available from the Caplin website and the Caplin repository (coming soon).

## Instructions

Detailed instructions are included in the `README.md` file inside each template directory.
