# Caplin Pricing Adapter Template for DotNet

This template project provides a starting point for writing pricing integration adapters based on Caplin's [C# DataSource API](https://www.caplin.com/developer/api/datasource_dotnet/latest).

## Getting started

Download and extract a Caplin DataSource.NET Release and copy the following files into the `lib` directory
 * DataSource.NET.dll
 * Datasource.NET.pdb
 * `On Windows`
 ** datasrc.dll
 ** datasrc.pdb
 ** hs\_regex.dll
 ** msvcr100.dll
 ** pthreadVC2.dll
 * `On Linux using Mono`
 ** libdatasrc.so

## Building
On Windows either with visual studio or msvcc, on Linux using `xbuild TemplatePricingAdapter.csproj`

## How to report issues with the template
To report an issue with the template, please contact Caplin Support or [raise an issue](https://github.com/caplin/project-templates/issues) on GitHub.
