import sys
import caplin.datasrc as ds

from caplin.pricingtemplate.container import ContainerTemplateProvider
from caplin.pricingtemplate.pricing import PricingTemplateProvider


def main():
    ds.initialise("datasource.conf", "PythonTemplatePricingSource", sys.argv)

    pricing_provider = PricingTemplateProvider()
    container_provider = ContainerTemplateProvider()

    pricing_provider.initialise()
    container_provider.initialise()

    ds.start()

