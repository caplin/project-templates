import sys
import caplin.datasrc as ds

from pricingtemplate.container import ContainerTemplateProvider
from pricingtemplate.pricing import PricingTemplateProvider


def main():
    ds.initialise("datasource.conf", "PythonTemplatePricingSource", sys.argv)

    pricing_provider = PricingTemplateProvider()
    container_provider = ContainerTemplateProvider()

    pricing_provider.initialise()
    container_provider.initialise()

    ds.start()

if __name__ == "__main__":
    main()
